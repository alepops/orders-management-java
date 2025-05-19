/**
 * Meniul principal al aplicatiei Orders Management.
 * Permite navigarea catre ferestrele de clienti, produse si comenzi.
 */


package Presentation;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    // Butoane de navigare catre functionalitati

    public MainMenu() {
        setTitle("Orders Management - Meniu Principal");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnClienti = new JButton("Gestionează Clienți");
        JButton btnProduse = new JButton("Gestionează Produse");
        JButton btnComenzi = new JButton("Plasează Comenzi");

        btnClienti.addActionListener(e -> new ClientView());
        btnProduse.addActionListener(e -> new ProductView());
        btnComenzi.addActionListener(e -> new OrderView());

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.add(btnClienti);
        panel.add(btnProduse);
        panel.add(btnComenzi);

        add(panel);
        setVisible(true);
    }
}
