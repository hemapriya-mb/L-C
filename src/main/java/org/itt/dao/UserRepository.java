package org.itt.dao;

import org.itt.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserRepository {
    public User getUserByUserIdAndPassword(int userId, String password) throws SQLException, ClassNotFoundException {
        User user = null;
        Connection connection = DataBaseConnector.getInstance().getConnection();

        String query = "SELECT * FROM user WHERE user_id = ? AND password = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setString(2, password);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String role = resultSet.getString("role");
            user = new User(userId, name, role, "");
        }

        return user;
    }

    public void addUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DataBaseConnector.getInstance().getConnection();
            String query = "INSERT INTO user (name, role, password) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getRole());
            statement.setString(3, user.getPassword());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllEmployees() {
        List<User> employees = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DataBaseConnector.getInstance().getConnection();
            String query = "SELECT * FROM user WHERE role = 'employee'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setName(resultSet.getString("name"));

                employees.add(user);
            }
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }}
