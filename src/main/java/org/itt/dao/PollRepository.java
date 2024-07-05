package org.itt.dao;

import org.itt.entity.Poll;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PollRepository {

    public void addPoll(Poll poll) throws DatabaseException {
        String query = "INSERT INTO poll (user_id, item_id, poll_date) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll.getUserId());
            statement.setInt(2, poll.getItemId());
            statement.setDate(3, poll.getPollDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add poll", e);
        }
    }

    public boolean hasPolledForItem(int userId, int itemId) throws DatabaseException {
        String query = "SELECT COUNT(*) FROM poll WHERE user_id = ? AND item_id = ?";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check poll status", e);
        }

        return false;
    }

    public List<Integer> getNextDayItemIds() throws DatabaseException {
        List<Integer> itemIds = new ArrayList<>();
        String query = "SELECT item_id FROM next_day_item";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                itemIds.add(resultSet.getInt("item_id"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve next day items", e);
        }

        return itemIds;
    }

    public void incrementPollCount(int itemId) throws DatabaseException {
        String query = "UPDATE next_day_item SET poll_count = poll_count + 1 WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to increment poll count", e);
        }
    }
}
