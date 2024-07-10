package org.itt.dao;

import org.itt.exception.DatabaseException;
import org.itt.utility.ProfileHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileRepository {

    public void saveProfile(int userId, int foodTypeChoice, int spiceLevelChoice, int cuisineChoice, int sweetToothChoice) throws DatabaseException {
        ProfileHelper profileHelper = new ProfileHelper();
        String foodType = profileHelper.getFoodType(foodTypeChoice);
        String spiceLevel = profileHelper.getSpiceLevel(spiceLevelChoice);
        String cuisinePreference = profileHelper.getCuisinePreference(cuisineChoice);
        boolean sweetTooth = profileHelper.getSweetToothPreference(sweetToothChoice);

        String sql = "INSERT INTO employee_profile (userId, foodType, spiceLevel, cuisinePreference, sweetTooth) VALUES (?, ?, ?, ?, ?)"
                + " ON DUPLICATE KEY UPDATE foodType = VALUES(foodType), spiceLevel = VALUES(spiceLevel), cuisinePreference = VALUES(cuisinePreference), sweetTooth = VALUES(sweetTooth)";

        try (Connection connection = DataBaseConnector.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, foodType);
            preparedStatement.setString(3, spiceLevel);
            preparedStatement.setString(4, cuisinePreference);
            preparedStatement.setBoolean(5, sweetTooth);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error saving profile data", e);
        }
    }

}
