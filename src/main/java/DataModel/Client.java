/**
 * Clasa care reprezinta entitatea Client.
 */

package DataModel;

public class Client {
    private int id;
    private String nume;
    private String email;
    // Getteri si setteri

    public Client(int id, String nume, String email) {
        this.id = id;
        this.nume = nume;
        this.email = email;
    }
    public Client() {}

    // Getters & setters (important pentru Reflection!)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Client #" + id + ": " + nume + " (" + email + ")";
    }
}

