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

        resultSet.close();
        statement.close();

        return items;
    }

    public void addItem(Item item) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "INSERT INTO Item (item_name, price, availability_status, meal_type, description) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, item.getItemName());
        statement.setDouble(2, item.getPrice());
        statement.setString(3, item.getAvailabilityStatus());
        statement.setString(4, item.getMealType());
        statement.setString(5, item.getDescription());

        statement.executeUpdate();
        statement.close();
    }

    public boolean updateItem(Item item) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "UPDATE Item SET item_name = ?, price = ?, availability_status = ?, meal_type = ?, description = ? WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, item.getItemName());
        statement.setDouble(2, item.getPrice());
        statement.setString(3, item.getAvailabilityStatus());
        statement.setString(4, item.getMealType());
        statement.setString(5, item.getDescription());
        statement.setInt(6, item.getItemId());

        int rowsAffected = statement.executeUpdate();
        statement.close();

        return rowsAffected > 0;
    }

    public boolean checkItemPresent(int itemId) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT 1 FROM Item WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);

        ResultSet resultSet = statement.executeQuery();
        boolean isItemPresent = resultSet.next();

        resultSet.close();
        statement.close();

        return isItemPresent;
    }

    public boolean deleteItem(int itemId) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "DELETE FROM Item WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);

        int rowsAffected = statement.executeUpdate();
        statement.close();

        return rowsAffected > 0;
    }


    public List<Item> getAllItemsWithRatings() throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();
        String query = "SELECT i.item_id, i.item_name, i.price, i.availability_status, i.meal_type, i.description, " +
                "COALESCE(AVG(f.rating), 0) as rating " +
                "FROM Item i LEFT JOIN Feedback f ON i.item_id = f.item_id " +
                "GROUP BY i.item_id";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            Item item = new Item();
            item.setItemId(resultSet.getInt("item_id"));
            item.setItemName(resultSet.getString("item_name"));
            item.setPrice(resultSet.getDouble("price"));
            item.setAvailabilityStatus(resultSet.getString("availability_status"));
            item.setMealType(resultSet.getString("meal_type"));
            item.setDescription(resultSet.getString("description"));
            item.setRating(resultSet.getDouble("rating"));

            items.add(item);
        }

        resultSet.close();
        statement.close();

        return items;
    }

    public void addItemToNextDayList(int itemId) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();
        String query = "INSERT INTO next_day_items (item_id, item_name) SELECT item_id, item_name FROM Item WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);
        statement.executeUpdate();

        statement.close();
    }

    public List<Item> getNextDayItems() throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();
        String query = "SELECT item_id, item_name FROM next_day_items";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        List<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            Item item = new Item();
            item.setItemId(resultSet.getInt("item_id"));
            item.setItemName(resultSet.getString("item_name"));
            items.add(item);
        }

        resultSet.close();
        statement.close();

        return items;
    }

    public void pollForNextDayItem(int itemId) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnector.getInstance().getConnection();
        String query = "UPDATE next_day_items SET poll_count = poll_count + 1 WHERE item_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, itemId);
        statement.executeUpdate();

        statement.close();
    }

}
