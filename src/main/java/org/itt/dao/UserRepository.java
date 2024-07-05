package org.itt.dao;

import org.itt.entity.User;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public User getUserByUserIdAndPassword(int userId, String password) throws DatabaseException {
        User user = null;
        String query = "SELECT * FROM user WHERE user_id = ? AND password = ?";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String role = resultSet.getString("role");
                    user = new User(userId, name, role, "");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve user by user ID and password", e);
        }

        return user;
    }

    public void addUser(User user) throws DatabaseException {
        String query = "INSERT INTO user (name, role, password) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getRole());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add user", e);
        }
    }

    public List<Integer> getAllEmployeeUserIds() throws DatabaseException {
        List<Integer> userIds = new ArrayList<>();
        String query = "SELECT user_id FROM user WHERE role = 'Employee'";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                userIds.add(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve employee user IDs", e);
        }

        return userIds;
    }
}
