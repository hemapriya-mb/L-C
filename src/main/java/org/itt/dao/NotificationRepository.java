package org.itt.dao;

import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

    public void addNotification(String sender, String message) throws DatabaseException {
        String query = "INSERT INTO notification (sender, message) VALUES (?, ?)";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, sender);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add notification", e);
        }
    }

    public List<String> getNotifications() throws DatabaseException {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT sender, message, timestamp FROM notification ORDER BY timestamp DESC";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String sender = resultSet.getString("sender");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getTimestamp("timestamp").toString();
                notifications.add("[" + timestamp + "] " + sender + ": " + message);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve notifications", e);
        }

        return notifications;
    }
}
