package org.itt.client;

import org.itt.constant.UserRole;
import org.itt.entity.User;
import org.itt.exception.InvalidInputException;
import org.itt.service.AdminService;
import org.itt.service.ChefService;
import org.itt.service.EmployeeService;
import org.itt.service.UserService;

import java.io.*;
import java.net.Socket;

public class Client {

    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;

    public static void main(String[] args) {
        System.out.println("Welcome");
        boolean continueLogin = true;
        User loggedInUser = null;

        while (continueLogin) {
            System.out.println("Login to continue");

            try {
                UserService userService = new UserService();
                User user = userService.getUserCredentials();
                boolean loginSuccessful = login(user);

                if (loginSuccessful) {
                    loggedInUser = user;
                    successMessage(user);
                    redirectUser(user);
                } else {
                    System.out.println("Login failed!");
                }
                System.out.print("Do you want to continue? (yes/no): ");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String choice = bufferedReader.readLine().trim();
                if (!choice.equalsIgnoreCase("yes")) {
                    continueLogin = false;
                }

            } catch (InvalidInputException exception) {
                System.out.println(exception.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (loggedInUser != null) {
            logout();
        }

        System.out.println("Exiting...");
    }

    private static boolean login(User user) throws IOException, ClassNotFoundException {
        socket = new Socket("localhost", 5000);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        objectOutputStream.writeObject(String.valueOf(user.getUserId()));
        objectOutputStream.writeObject(user.getPassword());

        User authenticatedUser = (User) objectInputStream.readObject();
        if (authenticatedUser != null) {
            user.setName(authenticatedUser.getName());
            user.setRole(authenticatedUser.getRole());
            return true;
        } else {
            socket.close();
            return false;
        }
    }

    private static void logout() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void successMessage(User user) {
        System.out.println("Login successful!\nWelcome " + user.getName().toUpperCase() + "\n");
    }

    private static void redirectUser(User user) {
        AdminService adminService = new AdminService();
        ChefService chefService = new ChefService();
        EmployeeService employeeService = new EmployeeService();
        UserRole role;
        try {
            role = UserRole.fromValue(user.getRole().toLowerCase());
        } catch (InvalidInputException e) {
            e.printStackTrace();
            return;
        }

        switch (role) {
            case ADMIN:
                adminService.performAdminTasks();
                break;
            case CHEF:
                chefService.performChefTasks();
                break;
            case EMPLOYEE:
                employeeService.performEmployeeTasks(user.getUserId());
                break;
        }
    }
}
