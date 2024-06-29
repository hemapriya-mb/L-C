package org.itt.dao;

import org.itt.entity.Item;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    public List<Item> getAllItems() throws DatabaseException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM item";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getInt("item_id"));
                item.setItemName(resultSet.getString("item_name"));
                item.setPrice(resultSet.getDouble("price"));
                item.setAvailabilityStatus(resultSet.getString("availability_status"));
                item.setMealType(resultSet.getString("meal_type"));
                item.setDescription(resultSet.getString("description"));
                items.add(item);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve all items", e);
        }

        return items;
    }

    public void addItem(Item item) throws DatabaseException {
        String query = "INSERT INTO item (item_name, price, availability_status, meal_type, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getItemName());
            statement.setDouble(2, item.getPrice());
            statement.setString(3, item.getAvailabilityStatus());
            statement.setString(4, item.getMealType());
            statement.setString(5, item.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add item", e);
        }
    }

    public boolean updateItem(Item item) throws DatabaseException {
        String query = "UPDATE item SET item_name = ?, price = ?, availability_status = ?, meal_type = ?, description = ? WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getItemName());
            statement.setDouble(2, item.getPrice());
            statement.setString(3, item.getAvailabilityStatus());
            statement.setString(4, item.getMealType());
            statement.setString(5, item.getDescription());
            statement.setInt(6, item.getItemId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update item", e);
        }
    }

    public boolean checkItemPresent(int itemId) throws DatabaseException {
        String query = "SELECT 1 FROM item WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if item is present", e);
        }
    }

    public boolean deleteItem(int itemId) throws DatabaseException {
        String query = "DELETE FROM item WHERE item_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete item", e);
        }
    }

    public List<Item> getTopRatedItems() throws DatabaseException {
        List<Item> topItems = new ArrayList<>();
        String query = "SELECT item.item_id, item.item_name, item.price, item.availability_status, " +
                "item.meal_type, item.description, AVG(feedback.rating) as avg_rating " +
                "FROM item " +
                "JOIN feedback ON item.item_id = feedback.item_id " +
                "GROUP BY item.item_id, item.item_name, item.price, item.availability_status, " +
                "item.meal_type, item.description " +
                "ORDER BY avg_rating DESC " +
                "LIMIT 10";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Item item = new Item();
                item.setItemId(resultSet.getInt("item_id"));
                item.setItemName(resultSet.getString("item_name"));
                item.setPrice(resultSet.getDouble("price"));
                item.setAvailabilityStatus(resultSet.getString("availability_status"));
                item.setMealType(resultSet.getString("meal_type"));
                item.setDescription(resultSet.getString("description"));
                item.setAverageRating(resultSet.getDouble("avg_rating"));
                topItems.add(item);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve top-rated items", e);
        }

        return topItems;
    }

    public Item getItemById(int itemId) throws DatabaseException {
        String query = "SELECT * FROM item WHERE item_id = ?";
        Item item = null;

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, itemId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    item = new Item();
                    item.setItemId(resultSet.getInt("item_id"));
                    item.setItemName(resultSet.getString("item_name"));
                    item.setPrice(resultSet.getDouble("price"));
                    item.setAvailabilityStatus(resultSet.getString("availability_status"));
                    item.setMealType(resultSet.getString("meal_type"));
                    item.setDescription(resultSet.getString("description"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve item by ID", e);
        }

        return item;
    }
}
