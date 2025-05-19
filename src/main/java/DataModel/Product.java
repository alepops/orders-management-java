package DataModel;
/**
 * Clasa care reprezinta un produs din depozit.
 */
public class Product {
    private int id;
    private String nume;
    private int stoc;
    private double pret;
    // Getteri si setteri

    public Product() {}

    public Product(int id, String nume, int stoc, double pret) {
        this.id = id;
        this.nume = nume;
        this.stoc = stoc;
        this.pret = pret;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public int getStoc() { return stoc; }
    public void setStoc(int stoc) { this.stoc = stoc; }

    public double getPret() { return pret; }
    public void setPret(double pret) { this.pret = pret; }

    @Override
    public String toString() {
        return "Product #" + id + ": " + nume + " [" + stoc + " buc, " + pret + " lei]";
    }
}
