package org.itt.dao;

import org.itt.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRepository {
    public User authenticate(int userId, String password) throws SQLException, ClassNotFoundException {
        User user = null;
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, password);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String role = resultSet.getString("role");
            user = new User(userId, name, role,"");
        }

        resultSet.close();
        statement.close();

        return user;
    }

    public void addUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DataBaseConnector.getInstance().getConnection();
            String query = "INSERT INTO users (name, role, password) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getRole());
            statement.setString(3, user.getPassword());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
