package org.itt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {
    public void addNotification(String sender, String message) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO notifications (sender, message) VALUES (?, ?)";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();
        }
    }

    public List<String> getNotifications() throws SQLException, ClassNotFoundException {
        List<String> notifications = new ArrayList<String>();
        String query = "SELECT sender, message, timestamp FROM notifications ORDER BY timestamp DESC";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);

             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String sender = resultSet.getString("sender");
                String message = resultSet.getString("message");
                String timestamp = resultSet.getTimestamp("timestamp").toString();
                notifications.add("[" + timestamp + "] " + sender + ": " + message);
            }
        }
        return notifications;
    }
}
