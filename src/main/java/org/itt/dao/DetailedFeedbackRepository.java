package org.itt.dao;

import org.itt.entity.DetailedFeedback;
import org.itt.exception.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DetailedFeedbackRepository {

    private static final String INSERT_DETAILED_FEEDBACK_SQL = "INSERT INTO detailed_feedback (user_id, item_id, answer1, answer2, answer3) VALUES (?, ?, ?, ?, ?)";

    public void addDetailedFeedback(DetailedFeedback detailedFeedback) throws DatabaseException {
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DETAILED_FEEDBACK_SQL)) {
            preparedStatement.setInt(1, detailedFeedback.getUserId());
            preparedStatement.setInt(2, detailedFeedback.getItemId());
            preparedStatement.setString(3, detailedFeedback.getAnswer1());
            preparedStatement.setString(4, detailedFeedback.getAnswer2());
            preparedStatement.setString(5, detailedFeedback.getAnswer3());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert detailed feedback, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding detailed feedback: " + e.getMessage(), e);
        }
    }
}
