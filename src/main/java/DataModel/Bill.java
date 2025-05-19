package DataModel;
/**
 * Clasa record care reprezinta o factura generata pentru fiecare comanda.
 * Facturile sunt imutabile si sunt salvate in tabela Log.
 */
public record Bill(int id, String text) {
    @Override
    public String toString() {
        return "Factura #" + id + "\n" + text;
    }
}
