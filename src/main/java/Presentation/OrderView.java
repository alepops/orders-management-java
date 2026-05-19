package Presentation;
import BusinessLogic.ClientBLL;
import BusinessLogic.OrderBLL;
import BusinessLogic.ProductBLL;
import DataModel.Client;
import DataModel.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * Fereastra grafica pentru plasarea comenzilor.
 * Permite selectarea clientului, produsului si cantitatii.
 * Afiseaza factura generata dupa plasarea comenzii.
 */
public class OrderView extends JFrame {
    // Componente si logica pentru plasarea comenzilor

    private final ClientBLL clientBLL = new ClientBLL();
    private final ProductBLL productBLL = new ProductBLL();
    private final OrderBLL orderBLL = new OrderBLL();

    private final JComboBox<Client> clientBox = new JComboBox<>();
    private final JComboBox<Product> productBox = new JComboBox<>();
    private final JTextField tfCantitate = new JTextField(6);
    private final JTextArea facturaText = new JTextArea(8, 40);

    public OrderView() {
        setTitle("Plasare Comanda");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        populateCombos();

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Date comanda"));
        formPanel.add(new JLabel("Client:"));
        formPanel.add(clientBox);
        formPanel.add(new JLabel("Produs:"));
        formPanel.add(productBox);
        formPanel.add(new JLabel("Cantitate:"));
        formPanel.add(tfCantitate);

        JButton placeBtn = new JButton("Plaseaza Comanda");
        placeBtn.addActionListener(e -> placeOrder());
        formPanel.add(placeBtn);
        JButton backBtn = new JButton("Inapoi la Meniu Principal");
        backBtn.addActionListener(e -> {
            this.dispose();
            new MainMenu();
        });
        formPanel.add(backBtn);

        facturaText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(facturaText);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void populateCombos() {
        List<Client> clients = clientBLL.getAllClients();
        List<Product> products = productBLL.getAllProducts();
        clientBox.removeAllItems();
        productBox.removeAllItems();

        for (Client c : clients) clientBox.addItem(c);
        for (Product p : products) productBox.addItem(p);
    }

    private void placeOrder() {
        try {
            Client client = (Client) clientBox.getSelectedItem();
            Product product = (Product) productBox.getSelectedItem();
            int quantity = Integer.parseInt(tfCantitate.getText().trim());

            if (client == null || product == null) {
                throw new IllegalArgumentException("Selecteaza un client si un produs.");
            }

            String rezultat = orderBLL.placeOrder(client.getId(), product.getId(), quantity);
            facturaText.setText(rezultat.contains("Comanda a fost procesata") ? getFacturaText(client, product, quantity) : rezultat);
            populateCombos(); // actualizare stocuri
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantitatea trebuie sa fie un numar intreg!", "Eroare", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFacturaText(Client client, Product product, int qty) {
        double total = qty * product.getPret();
        return "Factura generata:\n" +
                "Client: " + client.getNume() + "\n" +
                "Produs: " + product.getNume() + "\n" +
                "Cantitate: " + qty + "\n" +
                "Pret unitar: " + product.getPret() + " lei\n" +
                "Total: " + total + " lei\n";
    }
}
