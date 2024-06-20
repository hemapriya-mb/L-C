package org.itt.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NextDayItemRepository {

    public void addNextDayItems(int[] itemIds) throws SQLException, ClassNotFoundException {
        String INSERT_NEXT_DAY_ITEM_QUERY = "INSERT INTO next_day_item (item_id) VALUES (?) " +
                "ON DUPLICATE KEY UPDATE poll_count = poll_count + 1";
        Connection connection = DataBaseConnector.getInstance().getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(INSERT_NEXT_DAY_ITEM_QUERY)) {
            for (int itemId : itemIds) {
                stmt.setInt(1, itemId);
                stmt.executeUpdate();
            }
        }
    }

    public List<Integer> getNextDayItemIds() throws SQLException, ClassNotFoundException {
        List<Integer> itemIds = new ArrayList<>();
        String query = "SELECT item_id FROM next_day_item";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                itemIds.add(resultSet.getInt("item_id"));
            }
        }
        return itemIds;
    }

    public void incrementPollCount(int itemId) throws SQLException, ClassNotFoundException {
        String query = "UPDATE next_day_item SET poll_count = poll_count + 1 WHERE item_id = ?";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.executeUpdate();
        }
    }

}

