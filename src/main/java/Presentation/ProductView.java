package Presentation;
import BusinessLogic.ProductBLL;
import DataModel.Product;
import Util.ReflectionTableGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 * Fereastra grafica pentru gestionarea produselor.
 * Permite vizualizarea, adaugarea si stergerea produselor.
 */
public class ProductView extends JFrame {
    // Componente si logica pentru interfata de produse
    private final ProductBLL productBLL = new ProductBLL();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField tfNume = new JTextField(12);
    private final JTextField tfStoc = new JTextField(6);
    private final JTextField tfPret = new JTextField(6);

    public ProductView() {
        setTitle("Product Management");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nume", "Stoc", "Preț"}, 0);
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                tfNume.setText(table.getValueAt(row, getColumnIndex("nume")).toString());
                tfStoc.setText(table.getValueAt(row, getColumnIndex("stoc")).toString());
                tfPret.setText(table.getValueAt(row, getColumnIndex("pret")).toString());
            }
        });

        refreshTable();

        // Formulare
        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Nume:"));
        formPanel.add(tfNume);
        formPanel.add(new JLabel("Stoc:"));
        formPanel.add(tfStoc);
        formPanel.add(new JLabel("Preț:"));
        formPanel.add(tfPret);

        // Butoane
        JButton addBtn = new JButton("Adauga Produs");
        JButton deleteBtn = new JButton("Sterge Produs");
        JButton editBtn = new JButton("Editeaza Produs");
        JButton backBtn = new JButton("Inapoi la Meniu Principal");

        addBtn.addActionListener(e -> addProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        editBtn.addActionListener(e -> editProduct());
        backBtn.addActionListener(e -> {
            this.dispose();
            new MainMenu();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(backBtn);

        // Layout
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void refreshTable() {
        List<Product> products = productBLL.getAllProducts();
        table.setModel(ReflectionTableGenerator.generateTable(products));

    }

    private void addProduct() {
        try {
            String nume = tfNume.getText().trim();
            int stoc = Integer.parseInt(tfStoc.getText().trim());
            double pret = Double.parseDouble(tfPret.getText().trim());

            Product p = new Product(0, nume, stoc, pret);
            productBLL.addProduct(p);
            refreshTable();

            tfNume.setText("");
            tfStoc.setText("");
            tfPret.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectează un produs pentru ștergere.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        Product p = productBLL.findProductById(id);
        if (p != null) {
            productBLL.deleteProduct(p);
            refreshTable();
        }
    }
    private void editProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecteaza un produs pentru editare.");
            return;
        }

        try {
            int id = Integer.parseInt(table.getValueAt(row, getColumnIndex("id")).toString());
            String nume = tfNume.getText().trim();
            int stoc = Integer.parseInt(tfStoc.getText().trim());
            double pret = Double.parseDouble(tfPret.getText().trim());

            Product p = new Product(id, nume, stoc, pret);
            productBLL.updateProduct(p);

            refreshTable();
            table.clearSelection();
            tfNume.setText("");
            tfStoc.setText("");
            tfPret.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la editare: " + e.getMessage());
        }
    }
    private int getColumnIndex(String columnName) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

}
