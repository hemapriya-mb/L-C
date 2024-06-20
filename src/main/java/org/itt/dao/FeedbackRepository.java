package org.itt.dao;

import org.itt.entity.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {

    public void addFeedback(Feedback feedback) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO feedback (user_id, order_id, item_id, rating, comment) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedback.getUserId());
            statement.setInt(2, feedback.getOrderId());
            statement.setInt(3, feedback.getItemId());
            statement.setInt(4, feedback.getRating());
            statement.setString(5, feedback.getComment());

            statement.executeUpdate();
        }
    }

    private boolean isFeedbackExists(int userId, int orderId, int itemId, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM feedback WHERE user_id = ? AND order_id = ? AND item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, orderId);
            statement.setInt(3, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public List<Feedback> getFeedbackByItemId(int itemId) throws SQLException, ClassNotFoundException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE item_id = ?";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, itemId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Feedback feedback = new Feedback();
                    feedback.setUserId(resultSet.getInt("user_id"));
                    feedback.setOrderId(resultSet.getInt("order_id"));
                    feedback.setItemId(resultSet.getInt("item_id"));
                    feedback.setRating(resultSet.getInt("rating"));
                    feedback.setComment(resultSet.getString("comment"));
                    feedbackList.add(feedback);
                }
            }
        }
        return feedbackList;
    }

    public List<Feedback> getAllFeedback() throws SQLException, ClassNotFoundException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";
        try (
             Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setUserId(resultSet.getInt("user_id"));
                feedback.setOrderId(resultSet.getInt("order_id"));
                feedback.setItemId(resultSet.getInt("item_id"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setComment(resultSet.getString("comment"));
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }
}
