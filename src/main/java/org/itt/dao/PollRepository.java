package org.itt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class PollRepository {

    public boolean hasPolled(int employeeId, int itemId, Date pollDate) throws SQLException, ClassNotFoundException {
        String query = "SELECT 1 FROM poll WHERE employee_id = ? AND item_id = ? AND poll_date = ?";
        try (Connection connection =  DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setInt(2, itemId);
            statement.setDate(3, pollDate);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void addPoll(int employeeId, int itemId, Date pollDate) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO poll (employee_id, item_id, poll_date) VALUES (?, ?, ?)";
        try (Connection connection =  DataBaseConnector.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setInt(2, itemId);
            statement.setDate(3, pollDate);
            statement.executeUpdate();
        }
    }
}
