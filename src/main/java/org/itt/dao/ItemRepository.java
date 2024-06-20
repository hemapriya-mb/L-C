package org.itt.dao;

import org.itt.entity.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    public List<Item> getAllItems() throws SQLException, ClassNotFoundException {
        List<Item> items = new ArrayList<>();
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT * FROM item";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
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

        return items;
    }

    public void addItem(Item item) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "INSERT INTO item (item_name, price, availability_status, meal_type, description) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, item.getItemName());
        statement.setDouble(2, item.getPrice());
        statement.setString(3, item.getAvailabilityStatus());
        statement.setString(4, item.getMealType());
        statement.setString(5, item.getDescription());

        statement.executeUpdate();
    }

    public boolean updateItem(Item item) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "UPDATE item SET item_name = ?, price = ?, availability_status = ?, meal_type = ?, description = ? WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, item.getItemName());
        statement.setDouble(2, item.getPrice());
        statement.setString(3, item.getAvailabilityStatus());
        statement.setString(4, item.getMealType());
        statement.setString(5, item.getDescription());
        statement.setInt(6, item.getItemId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }

    public boolean checkItemPresent(int itemId) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT 1 FROM item WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);

        ResultSet resultSet = statement.executeQuery();
        boolean isItemPresent = resultSet.next();

        return isItemPresent;
    }

    public boolean deleteItem(int itemId) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "DELETE FROM item WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    }


    public List<Item> getTopRatedItems() throws SQLException, ClassNotFoundException {
        String TOP_ITEMS_QUERY = "SELECT item.item_id, item.item_name, item.price, item.availability_status, " +
                "item.meal_type, item.description, AVG(feedback.rating) as avg_rating " +
                "FROM item " +
                "JOIN feedback ON item.item_id = feedback.item_id " +
                "GROUP BY item.item_id, item.item_name, item.price, item.availability_status, " +
                "item.meal_type, item.description " +
                "ORDER BY avg_rating DESC " +
                "LIMIT 10";

        List<Item> topItems = new ArrayList<>();
        Connection connection = DataBaseConnector.getInstance().getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(TOP_ITEMS_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                double price = rs.getDouble("price");
                String availabilityStatus = rs.getString("availability_status");
                String mealType = rs.getString("meal_type");
                String description = rs.getString("description");
                double avgRating = rs.getDouble("avg_rating");
                topItems.add(new Item(itemId, itemName, price, availabilityStatus, mealType, description, avgRating));
            }
        }

        return topItems;
    }

    public Item getItemById(int itemId) throws SQLException, ClassNotFoundException {
        Item item = null;
        String query = "SELECT * FROM item WHERE item_id = ?";
        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, itemId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
        }
        return item;
    }
}
