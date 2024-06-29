package org.itt.dao;

import org.itt.entity.Feedback;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {

    public void addFeedback(Feedback feedback) throws DatabaseException {
        String query = "INSERT INTO feedback (user_id, order_id, item_id, rating, comment) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, feedback.getUserId());
            statement.setInt(2, feedback.getOrderId());
            statement.setInt(3, feedback.getItemId());
            statement.setInt(4, feedback.getRating());
            statement.setString(5, feedback.getComment());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add feedback", e);
        }
    }

    public List<Feedback> getFeedbackByItemId(int itemId) throws DatabaseException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
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
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve feedback by item ID", e);
        }

        return feedbackList;
    }

    public List<Feedback> getAllFeedback() throws DatabaseException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Feedback feedback = new Feedback();
                feedback.setUserId(resultSet.getInt("user_id"));
                feedback.setOrderId(resultSet.getInt("order_id"));
                feedback.setItemId(resultSet.getInt("item_id"));
                feedback.setRating(resultSet.getInt("rating"));
                feedback.setComment(resultSet.getString("comment"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all feedback", e);
        }

        return feedbackList;
    }
}
