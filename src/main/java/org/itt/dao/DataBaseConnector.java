package org.itt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    private static Connection connection;
    private static DataBaseConnector instance = null;
    private static final String URL = "jdbc:mysql://localhost:3306/recommendationEngine";
    private static String username;
    private static String password;

    private DataBaseConnector() {}

    public static synchronized DataBaseConnector getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            username ="root"; //System.getenv("DB_USERNAME");
            password = "Hemapriya@1";//System.getenv("DB_PASSWORD");
            connection = DriverManager.getConnection(URL, username, password);
            instance = new DataBaseConnector();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
