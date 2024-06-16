package org.itt.dao;

import org.itt.entity.Feedback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {

    public List<Feedback> getFeedbackByItemId(int itemId) throws SQLException, ClassNotFoundException {
        List<Feedback> feedbackList = new ArrayList<>();
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT * FROM feedback WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Feedback feedback = new Feedback();
            feedback.setFeedbackId(resultSet.getInt("feedback_id"));
            feedback.setUserId(resultSet.getInt("user_id"));
            feedback.setItemId(resultSet.getInt("item_id"));
            feedback.setRating(resultSet.getInt("rating"));
            feedback.setComments(resultSet.getString("comments"));
            feedbackList.add(feedback);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return feedbackList;
    }

    public void addFeedback(Feedback feedback) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "INSERT INTO feedback (user_id, item_id, rating, comments) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, feedback.getUserId());
        statement.setInt(2, feedback.getItemId());
        statement.setInt(3, feedback.getRating());
        statement.setString(4, feedback.getComments());

        statement.executeUpdate();
        statement.close();
        connection.close();
    }
}
