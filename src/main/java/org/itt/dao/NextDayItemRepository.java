package org.itt.dao;

import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NextDayItemRepository {

    public void addNextDayItems(int[] itemIds) throws DatabaseException {
        String query = "INSERT INTO next_day_item (item_id) VALUES (?) " +
                "ON DUPLICATE KEY UPDATE poll_count = poll_count + 1";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int itemId : itemIds) {
                statement.setInt(1, itemId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add next day items", e);
        }
    }

    public List<Integer> getNextDayItemIds() throws DatabaseException {
        List<Integer> itemIds = new ArrayList<>();
        String query = "SELECT item_id FROM next_day_item";

        try (Connection connection = DataBaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                itemIds.add(resultSet.getInt("item_id"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve next day item IDs", e);
        }

        return itemIds;
    }

    public void incrementPollCount(int itemId) throws DatabaseException {
        String query = "UPDATE next_day_item SET poll_count = poll_count + 1 WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to increment poll count", e);
        }
    }
}
