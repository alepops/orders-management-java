package DataModel;
/**
 * Clasa care reprezinta o comanda efectuata de un client.
 */
public class Orders {
    private int id;
    private int idClient;
    private int idProduct;
    private int cantitate;

    public Orders() {}

    public Orders(int id, int idClient, int idProduct, int cantitate) {
        this.id = id;
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.cantitate = cantitate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public int getIdProduct() { return idProduct; }
    public void setIdProduct(int idProduct) { this.idProduct = idProduct; }

    public int getCantitate() { return cantitate; }
    public void setCantitate(int cantitate) { this.cantitate = cantitate; }

    @Override
    public String toString() {
        return "Order #" + id + ": Client " + idClient + ", Product " + idProduct + ", Cantitate " + cantitate;
    }
}
