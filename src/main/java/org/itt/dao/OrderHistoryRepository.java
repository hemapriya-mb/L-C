package org.itt.dao;

import org.itt.entity.OrderHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryRepository {

    public List<OrderHistory> getOrderHistoryByUserId(int userId) throws SQLException, ClassNotFoundException {
        List<OrderHistory> orderHistoryList = new ArrayList<>();
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT * FROM order_history WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrderId(resultSet.getInt("order_id"));
            orderHistory.setUserId(resultSet.getInt("user_id"));
            orderHistory.setItemId(resultSet.getInt("item_id"));
            orderHistory.setOrderDate(resultSet.getDate("order_date"));
            orderHistoryList.add(orderHistory);
        }
        return orderHistoryList;
    }

    public void addOrder(OrderHistory orderHistory) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        PreparedStatement statement = null;

        connection = DataBaseConnector.getInstance().getConnection();
        String query = "INSERT INTO order_history (user_id, item_id, order_date) VALUES (?, ?, ?)";
        statement = connection.prepareStatement(query);
        statement.setInt(1, orderHistory.getUserId());
        statement.setInt(2, orderHistory.getItemId());
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

        statement.executeUpdate();

    }
}
