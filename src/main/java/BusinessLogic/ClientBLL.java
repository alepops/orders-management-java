package BusinessLogic;
import DataAcess.ClientDAO;
import DataModel.Client;

import java.util.List;
/**
 * Clasa care gestioneaza logica de business pentru clienti.
 * Ofera metode pentru adaugarea, actualizarea, stergerea si cautarea clientilor.
 */
public class ClientBLL {
    /**
     * Returneaza lista tuturor clientilor din baza de date.
     * @return Lista de clienti
     */

    private final ClientDAO clientDAO = new ClientDAO();

    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }
    /**
     * Cauta un client dupa ID.
     * @param id ID-ul clientului
     * @return Obiectul Client gasit
     */

    public Client findClientById(int id) {
        return clientDAO.findById(id);
    }
    /**
     * Adauga un client nou.
     * @param c Clientul de adaugat
     * @return Clientul inserat
     */

    public Client addClient(Client c) {
        if (c.getNume() == null || c.getNume().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele nu poate fi gol.");
        }
        if (c.getEmail() == null || !c.getEmail().contains("@")) {
            throw new IllegalArgumentException("Email invalid.");
        }
        return clientDAO.insert(c);
    }
    /**
     * Actualizeaza un client existent.
     * @param c Clientul de actualizat
     * @return Clientul actualizat
     */

    public Client updateClient(Client c) {
        return clientDAO.update(c);
    }

    public void deleteClient(Client c) {
        clientDAO.delete(c);
    }
}
