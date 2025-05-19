package Presentation;
import BusinessLogic.ClientBLL;
import DataModel.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Util.ReflectionTableGenerator;
/**
 * Fereastra grafica pentru gestionarea clientilor.
 * Permite vizualizarea, adaugarea si stergerea clientilor.
 */
public class ClientView extends JFrame {
    // Componente si logica pentru interfata de clienti
    private final ClientBLL clientBLL = new ClientBLL();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JTextField tfNume = new JTextField(15);
    private final JTextField tfEmail = new JTextField(15);

    public ClientView() {
        setTitle("Client Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        tableModel = new DefaultTableModel(new Object[]{"ID", "Nume", "Email"}, 0);
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                tfNume.setText(table.getValueAt(row, getColumnIndex("nume")).toString());
                tfEmail.setText(table.getValueAt(row, getColumnIndex("email")).toString());
            }
        });

        refreshTable();

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Nume:"));
        formPanel.add(tfNume);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(tfEmail);

        JButton addBtn = new JButton("Adaugă Client");
        JButton deleteBtn = new JButton("Șterge Client");
        JButton editBtn = new JButton("Editeaza Client");
        JButton backBtn = new JButton("Înapoi la Meniu Principal");

        addBtn.addActionListener(e -> addClient());
        deleteBtn.addActionListener(e -> deleteClient());
        editBtn.addActionListener(e -> editClient());
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
        List<Client> clients = clientBLL.getAllClients();
        table.setModel(ReflectionTableGenerator.generateTable(clients));
    }


    private void addClient() {
        try {
            String nume = tfNume.getText();
            String email = tfEmail.getText();
            Client c = new Client(0, nume, email);
            clientBLL.addClient(c);
            refreshTable();
            tfNume.setText("");
            tfEmail.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage(), "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selectează un client pentru ștergere.");
            return;
        }


        int columnIndex = -1;
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equalsIgnoreCase("id")) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex == -1) {
            JOptionPane.showMessageDialog(this, "Coloana 'id' nu a fost găsită.");
            return;
        }

        Object idObj = table.getValueAt(row, columnIndex);
        int id = Integer.parseInt(idObj.toString());

        Client c = clientBLL.findClientById(id);
        if (c != null) {
            clientBLL.deleteClient(c);
            refreshTable();
        }
    }

    private void editClient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecteaza un client pentru editare.");
            return;
        }

        try {
            int id = Integer.parseInt(table.getValueAt(row, getColumnIndex("id")).toString());
            String nume = tfNume.getText();
            String email = tfEmail.getText();

            Client c = new Client(id, nume, email);
            clientBLL.updateClient(c);

            refreshTable();
            table.clearSelection();
            tfNume.setText("");
            tfEmail.setText("");

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
