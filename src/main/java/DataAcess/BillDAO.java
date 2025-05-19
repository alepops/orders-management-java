package DataAcess;

import DataModel.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillDAO {
    private static final Logger LOGGER = Logger.getLogger(BillDAO.class.getName());

    public void insert(Bill bill) {
        String query = "INSERT INTO Log (text) VALUES (?)";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, bill.text());
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:insert " + e.getMessage());
        }
    }

    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Log";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String text = resultSet.getString("text");
                bills.add(new Bill(id, text));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findAll " + e.getMessage());
        }

        return bills;
    }
}
