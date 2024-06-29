package org.itt.dao;

import org.itt.entity.OrderHistory;
import org.itt.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryRepository {

    public List<OrderHistory> getOrderHistoryByUserId(int userId) throws DatabaseException {
        List<OrderHistory> orderHistoryList = new ArrayList<>();
        String query = "SELECT * FROM order_history WHERE user_id = ?";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderHistory orderHistory = new OrderHistory();
                    orderHistory.setOrderId(resultSet.getInt("order_id"));
                    orderHistory.setUserId(resultSet.getInt("user_id"));
                    orderHistory.setItemId(resultSet.getInt("item_id"));
                    orderHistory.setOrderDate(resultSet.getDate("order_date"));
                    orderHistoryList.add(orderHistory);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve order history by user ID", e);
        }

        return orderHistoryList;
    }

    public void addOrder(OrderHistory orderHistory) throws DatabaseException {
        String query = "INSERT INTO order_history (user_id, item_id, order_date) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderHistory.getUserId());
            statement.setInt(2, orderHistory.getItemId());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add order history", e);
        }
    }
}
