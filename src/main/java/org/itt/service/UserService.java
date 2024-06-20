package org.itt.service;

import org.itt.constant.UserRole;
import org.itt.entity.User;
import org.itt.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserService {
    private final static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public User getUserDetail() throws IOException, InvalidInputException {
        User user = new User();
        user.setName(getValidName());
        user.setRole(getValidRole().toString());
        user.setPassword(getValidPassword());
        return user;
    }

    public User getUserCredentials() throws InvalidInputException, IOException {
        int userId = getUserId();
        String password = getValidPassword();
        return new User(userId, "", "", password);
    }

    private int getUserId() throws InvalidInputException, IOException {
        int attempts = 0;
        int userId = -1;

        while (attempts < 3) {
            System.out.print("Enter user ID: ");
            try {
                String userIdString = bufferedReader.readLine();
                userId = Integer.parseInt(userIdString);
                return userId;
            } catch (NumberFormatException | IOException e) {
                attempts++;
                System.out.println("Invalid user ID. Please only enter numbers. Attempt " + attempts + " of 3.");
                if (attempts >= 3) {
                    if (!askToContinue(bufferedReader)) {
                        throw new InvalidInputException("Max attempts reached. Exiting...");
                    }
                    attempts = 0; // Reset attempts if the user wants to continue
                }
            }
        }
        throw new InvalidInputException("Max attempts reached. Exiting...");
    }
    private String getValidName() throws IOException, InvalidInputException {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter user name: ");
            String name = bufferedReader.readLine().trim();
            if (isValidName(name)) {
                return name;
            } else {
                System.out.println("Invalid name. Name must contain only letters. Please try again.");
            }
        }
        throw new InvalidInputException("Invalid name. You have reached maximum attempts.");
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private UserRole getValidRole() throws IOException, InvalidInputException {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter user role (Admin/Chef/Employee): ");
            String roleStr = bufferedReader.readLine().trim().toLowerCase();
            try {
                UserRole role = UserRole.fromValue(roleStr);
                return role;
            } catch (InvalidInputException e) {
                System.out.println("Invalid role. Please enter a valid role (Admin, Chef, or Employee).");
            }
        }
        throw new InvalidInputException("Invalid role. You have reached maximum attempts.");
    }

    private String getValidPassword() throws IOException, InvalidInputException {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter password (minimum 8 characters): ");
            String password = bufferedReader.readLine();
            if (isValidPassword(password)) {
                return password;
            } else {
                System.out.println("Invalid password. Password must be at least 8 characters long. Please try again.");
            }
        }
        throw new InvalidInputException("Invalid password. You have reached maximum attempts.");
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private boolean askToContinue(BufferedReader reader) throws IOException {
        System.out.print("You have reached the maximum attempts. Do you want to continue? (yes/no): ");
        String choice = reader.readLine().toLowerCase().trim();
        return choice.equals("yes");
    }
}
