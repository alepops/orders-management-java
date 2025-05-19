package BusinessLogic;

import DataAcess.ProductDAO;
import DataModel.Product;

import java.util.List;
/**
 * Clasa care gestioneaza logica de business pentru produse.
 */

public class ProductBLL {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public Product findProductById(int id) {
        return productDAO.findById(id);
    }

    public Product addProduct(Product p) {
        if (p.getNume() == null || p.getNume().trim().isEmpty()) {
            throw new IllegalArgumentException("Numele produsului nu poate fi gol.");
        }
        if (p.getPret() <= 0) {
            throw new IllegalArgumentException("Prețul trebuie să fie pozitiv.");
        }
        if (p.getStoc() < 0) {
            throw new IllegalArgumentException("Stocul nu poate fi negativ.");
        }
        return productDAO.insert(p);
    }

    public Product updateProduct(Product p) {
        return productDAO.update(p);
    }

    public void deleteProduct(Product p) {
        productDAO.delete(p);
    }
}
