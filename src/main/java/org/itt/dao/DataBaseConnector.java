package org.itt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    private static volatile DataBaseConnector instance = null;
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Hemapriya@1";
    private Connection connection;

    private DataBaseConnector() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static DataBaseConnector getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            synchronized (DataBaseConnector.class) {
                if (instance == null) {
                    instance = new DataBaseConnector();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            synchronized (DataBaseConnector.class) {
                if (connection == null || connection.isClosed()) {
                    instance = new DataBaseConnector();
                }
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
