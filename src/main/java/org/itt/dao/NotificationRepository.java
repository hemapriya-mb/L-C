package org.itt.dao;

import org.itt.entity.Notification;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

    public void addNotification(int userId, String message) throws DatabaseException {
        String query = "INSERT INTO notification (user_id, message) VALUES (?, ?)";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add notification", e);
        }
    }

    public List<Notification> getNotificationsByUserId(int userId) throws DatabaseException {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE user_id = ? AND is_read = FALSE";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setNotificationId(resultSet.getInt("notification_id"));
                    notification.setUserId(resultSet.getInt("user_id"));
                    notification.setMessage(resultSet.getString("message"));
                    notification.setIsRead(resultSet.getBoolean("is_read"));
                    notification.setCreatedDate(resultSet.getTimestamp("created_date"));
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve notifications", e);
        }

        return notifications;
    }

    public void markNotificationsAsRead(int userId) throws DatabaseException {
        String query = "UPDATE notification SET is_read = TRUE WHERE user_id = ? AND is_read = FALSE";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to mark notifications as read", e);
        }
    }
}
