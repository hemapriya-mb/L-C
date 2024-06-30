package org.itt.dao;

import org.itt.entity.OrderHistory;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryRepository {

    public void addOrder(OrderHistory orderHistory) throws DatabaseException {
        String query = "INSERT INTO order_history (user_id, item_id) VALUES (?, ?)";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderHistory.getUserId());
            statement.setInt(2, orderHistory.getItemId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add order", e);
        }
    }

    public List<OrderHistory> getOrderHistoryByUserId(int userId) throws DatabaseException {
        List<OrderHistory> orderHistoryList = new ArrayList<>();
        String query = "SELECT oh.order_id, oh.item_id, i.item_name, oh.order_date " +
                "FROM order_history oh " +
                "JOIN item i ON oh.item_id = i.item_id " +
                "WHERE oh.user_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderHistory orderHistory = new OrderHistory();
                    orderHistory.setOrderId(resultSet.getInt("order_id"));
                    orderHistory.setItemId(resultSet.getInt("item_id"));
                    orderHistory.setItemName(resultSet.getString("item_name"));
                    orderHistory.setOrderDate(resultSet.getTimestamp("order_date").toLocalDateTime().toLocalDate());
                    orderHistoryList.add(orderHistory);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve order history by user ID", e);
        }

        return orderHistoryList;
    }
}
