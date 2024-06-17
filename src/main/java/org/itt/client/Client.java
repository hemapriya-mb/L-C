package org.itt.client;

import org.itt.constant.UserRole;
import org.itt.entity.User;
import org.itt.exception.InvalidInputException;
import org.itt.utility.AdminOperation;
import org.itt.utility.ChefOperation;
import org.itt.utility.EmployeeOperation;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        System.out.println("Welcome");
        boolean continueLogin = true;

        while (continueLogin) {
            System.out.println("Login to continue");

            try {
                UserDetail userDetail = new UserDetail();
                User user = userDetail.getUserCredentials();
                boolean loginSuccessful = login(user);

                if (loginSuccessful) {
                    successMessage(user);
                    redirectUser(user);
                } else {
                    System.out.println("Login failed!");
                }
                System.out.print("Do you want to continue? (yes/no): ");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String choice = reader.readLine().trim();
                if (!choice.equalsIgnoreCase("yes")) {
                    continueLogin = false;
                }

            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Exiting...");
    }

    private static boolean login(User user) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket("localhost", 5000);
             OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream();
             ObjectOutputStream oos = new ObjectOutputStream(output);
             ObjectInputStream ois = new ObjectInputStream(input)) {

            oos.writeObject(user.getUserId());
            oos.writeObject(user.getPassword());

            User authenticatedUser = (User) ois.readObject();
            if (authenticatedUser != null) {
                user.setName(authenticatedUser.getName());
                user.setRole(authenticatedUser.getRole());
                return true;
            } else {
                return false;
            }
        }
    }

    private static void successMessage(User user) {
        System.out.println("Login successful!\nWelcome " + user.getName().toUpperCase() + "\n");
    }

    private static void redirectUser(User user) {
        AdminOperation adminOperation = new AdminOperation();
        ChefOperation chefOperation = new ChefOperation();
        EmployeeOperation employeeOperation = new EmployeeOperation();
        UserRole role;
        try {
            role = UserRole.fromValue(user.getRole().toLowerCase());
        } catch (InvalidInputException e) {
            e.printStackTrace();
            return;
        }

        switch (role) {
            case ADMIN:
                adminOperation.performAdminTasks();
                break;
            case CHEF:
                chefOperation.performChefTasks();
                break;
            case EMPLOYEE:
                employeeOperation.performEmployeeTasks(user.getUserId());
                break;
        }
    }
}
