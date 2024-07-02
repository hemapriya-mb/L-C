package org.itt.controller;

import org.itt.service.UserService;

import java.io.*;
import java.net.Socket;

public class LoginControllerClient {

    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    public static void main(String[] args) {
        System.out.println("Welcome");
        boolean continueLogin = true;

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (continueLogin) {
                System.out.println("Login to continue");

                try {
                    UserService userService = new UserService();
                    int userId = userService.getUserId();
                    String password = userService.getPassword();
                    String responseMessage = login(userId, password);
                    System.out.println(responseMessage);

                    if (responseMessage.startsWith("Login successful:")) {
                        String role = responseMessage.split(":")[1];
                        if ("EMPLOYEE".equals(role)) {
                            EmployeeControllerClient employeeControllerClient = new EmployeeControllerClient(objectInputStream, objectOutputStream);
                            employeeControllerClient.handleEmployeeTasks(userId);
                        } else if ("CHEF".equals(role)) {
                            ChefControllerClient chefControllerClient = new ChefControllerClient(objectInputStream, objectOutputStream);
                            chefControllerClient.handleChefTasks();
                        } else if ("ADMIN".equals(role)) {
                            AdminControllerClient adminControllerClient = new AdminControllerClient(objectInputStream, objectOutputStream);
                            adminControllerClient.handleAdminTasks();
                        }
                    }

                    System.out.print("Do you want to continue? (yes/no): ");
                    String choice = bufferedReader.readLine().trim();
                    if (!choice.equalsIgnoreCase("yes")) {
                        continueLogin = false;
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("An error occurred while processing your request. Please try again.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logout();
            System.out.println("Exiting...");
        }
    }

    private static String login(int userId, String password) throws IOException, ClassNotFoundException {
        try {
            socket = new Socket("localhost", 5000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(userId);
            objectOutputStream.writeObject(password);

            return (String) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            if (socket != null) {
                socket.close();
            }
            throw e;
        }
    }

    private static void logout() {
        try {
            if (socket != null && !socket.isClosed()) {
                objectOutputStream.writeObject("LOGOUT");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
