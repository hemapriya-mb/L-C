package org.itt.dao;

import org.itt.entity.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackRepository {

    public void addFeedback(Feedback feedback) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement statement = null;

            connection = DataBaseConnector.getInstance().getConnection();

            if (!isFeedbackExists(feedback.getUserId(), feedback.getItemId(), connection)) {
                String query = "INSERT INTO feedback (user_id, item_id, rating, comment) VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(query);

                statement.setInt(1, feedback.getUserId());
                statement.setInt(2, feedback.getItemId());
                statement.setInt(3, feedback.getRating());
                statement.setString(4, feedback.getComment());

                statement.executeUpdate();
            } else {
                throw new SQLException("Feedback already exists for this user and item.");
            }

    }

    private boolean isFeedbackExists(int userId, int itemId, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM feedback WHERE user_id = ? AND item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
