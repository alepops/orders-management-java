package BussinessLogic;

import DataAcess.BillDAO;
import DataAcess.OrderDAO;
import DataAcess.ProductDAO;
import DataModel.Bill;
import DataModel.Order;
import DataModel.Product;
/**
 * Clasa care gestioneaza logica de plasare a comenzilor.
 * Verifica stocul disponibil, actualizeaza cantitatile si genereaza facturi.
 */

public class OrderBLL {
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;
    private final BillDAO billDAO;

    public OrderBLL() {
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.billDAO = new BillDAO();
    }

    public String placeOrder(int clientId, int productId, int quantity) {
        Product product = productDAO.findById(productId);

        if (product == null) {
            return "Produsul nu există!";
        }

        if (quantity <= 0) {
            return "Cantitate invalidă!";
        }

        if (product.getStoc() < quantity) {
            return "Stoc insuficient pentru produsul: " + product.getNume();
        }

        // actualizare stoc
        int newStock = product.getStoc() - quantity;
        product.setStoc(newStock);
        productDAO.update(product);

        // creare comandă
        Order order = new Order();
        order.setIdClient(clientId);
        order.setIdProduct(productId);
        order.setCantitate(quantity);
        orderDAO.insert(order);

        // generare factură
        double total = quantity * product.getPret();
        String billText = "Factura pentru clientul #" + clientId + "\n" +
                "Produs: " + product.getNume() + "\n" +
                "Cantitate: " + quantity + "\n" +
                "Pret unitar: " + product.getPret() + "\n" +
                "Total: " + total + " lei\n";

        Bill bill = new Bill(0, billText);  // ID-ul este autogenerat
        billDAO.insert(bill);

        return "Comanda a fost procesată cu succes!";
    }
}
