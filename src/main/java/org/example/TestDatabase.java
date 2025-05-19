package org.example;

import DataAcess.ConnectionFactory;

import java.sql.Connection;

public class TestDatabase {
    public static void main(String[] args) {
        Connection connection = ConnectionFactory.getConnection();

        if(connection != null) {
            System.out.println("Connected successfully!");
            ConnectionFactory.close(connection);
        } else {
            System.out.println("Failed to connect!");
        }
    }
}