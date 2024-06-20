package org.itt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    private static Connection connection;
    private static DataBaseConnector instance = null;
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria";
    private static String username;
    private static String password;

    private DataBaseConnector() {}

    public static synchronized DataBaseConnector getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            username ="root";
            password = "Hemapriya@1";
            connection = DriverManager.getConnection(URL, username, password);
            instance = new DataBaseConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
