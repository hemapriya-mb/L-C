package org.itt.service;

import org.itt.entity.User;
import org.itt.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserService {
    private final BufferedReader bufferedReader;

    public UserService() {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public int getUserId() throws IOException {
        int userId = -1;
        while (userId < 0) {
            System.out.print("Enter user ID: ");
            try {
                userId = Integer.parseInt(bufferedReader.readLine().trim());
                if (userId < 0) {
                    System.out.println("User ID must be a non-negative number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return userId;
    }

    public String getPassword() throws IOException {
        String password = "";
        while (password.length() < 8) {
            System.out.print("Enter password (minimum 8 characters): ");
            password = bufferedReader.readLine().trim();
            if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long. Please try again.");
            }
        }
        return password;
    }

    public String getUserName() throws IOException {
        String name = "";
        while (!name.matches("[a-zA-Z]+")) {
            System.out.print("Enter user name: ");
            name = bufferedReader.readLine().trim();
            if (!name.matches("[a-zA-Z]+")) {
                System.out.println("Invalid name. Name must contain only letters. Please try again.");
            }
        }
        return name;
    }

    public String getUserRole() throws IOException {
        String[] validRoles = {"Admin", "Chef", "Employee"};
        String role = "";
        boolean validRole = false;
        while (!validRole) {
            System.out.print("Enter user role (Admin/Chef/Employee): ");
            role = bufferedReader.readLine().trim();
            for (String validRoleString : validRoles) {
                if (validRoleString.equalsIgnoreCase(role)) {
                    validRole = true;
                    role = validRoleString;
                    break;
                }
            }
            if (!validRole) {
                System.out.println("Invalid role. Please enter one of the valid roles (Admin, Chef, or Employee).");
            }
        }
        return role;
    }

    public String getValidPassword() throws IOException {
        return getPassword();
    }

    public User getUserDetail() throws IOException, InvalidInputException {
        User user = new User();
        user.setName(getUserName());
        user.setRole(getUserRole());
        user.setPassword(getValidPassword());
        return user;
    }

}
