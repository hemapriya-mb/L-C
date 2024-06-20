package org.itt.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.itt.entity.Item;
import org.itt.entity.Notification;
import org.itt.entity.User;

public class NextDayItemsRepository {

    public void incrementPollCount(int itemId) throws SQLException, ClassNotFoundException {
        String query = "UPDATE next_day_item SET poll_count = poll_count + 1 WHERE item_id = ?";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        }
    }

    public List<Item> getNextDayItems() throws SQLException, ClassNotFoundException {
        String query = "SELECT item_id, item_name, poll_count FROM next_day_item";
        try (Connection connection =  DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<Item> nextDayItems = new ArrayList<>();
            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getInt("item_id"));
                item.setItemName(resultSet.getString("item_name"));
                item.setPollCount(resultSet.getInt("poll_count"));
                nextDayItems.add(item);
            }
            return nextDayItems;
        }
    }

    public List<Item> getTopRatedItems() throws SQLException, ClassNotFoundException {
        String query = "SELECT i.item_id, i.item_name, i.meal_type, AVG(f.rating) as avg_rating " +
                "FROM Item i JOIN feedback f ON i.item_id = f.item_id " +
                "GROUP BY i.item_id, i.item_name, i.meal_type " +
                "ORDER BY avg_rating DESC LIMIT 10";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<Item> topRatedItems = new ArrayList<>();
            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getInt("item_id"));
                item.setItemName(resultSet.getString("item_name"));
                item.setMealType(resultSet.getString("meal_type"));
                item.setAverageRating(resultSet.getDouble("avg_rating"));
                topRatedItems.add(item);
            }
            return topRatedItems;
        }
    }

    public void addItemForNextDay(int itemId) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO next_day_item (item_id, item_name, poll_count) " +
                "SELECT item_id, item_name, 0 FROM Item WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item with ID " + itemId + " added for next day successfully.");
            } else {
                System.out.println("Failed to add item with ID " + itemId + " for next day.");
            }
        }
    }

    public List<Item> getTopItemsByPollCount(int limit) throws SQLException, ClassNotFoundException {
        String query = "SELECT item_id, item_name, poll_count FROM next_day_item ORDER BY poll_count DESC LIMIT ?";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Item> topItems = new ArrayList<>();
                while (resultSet.next()) {
                    Item item = new Item();
                    item.setItemId(resultSet.getInt("item_id"));
                    item.setItemName(resultSet.getString("item_name"));
                    item.setPollCount(resultSet.getInt("poll_count"));
                    topItems.add(item);
                }
                return topItems;
            }
        }
    }

}
