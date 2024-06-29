package org.itt.utility;

import org.itt.entity.User;
import org.itt.exception.InvalidInputException;
import org.itt.service.UserService;

import java.io.*;
import java.net.Socket;

public class Client {

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
                    User user = userService.getUserCredentials();
                    String responseMessage = login(user);
                    System.out.println(responseMessage);

                    if (responseMessage.startsWith("Login successful")) {
                        String role = responseMessage.split(":")[1].trim(); // Assume responseMessage is like "Login successful:admin"
                        if ("admin".equalsIgnoreCase(role)) {
                            AdminTaskClient adminTaskClient = new AdminTaskClient(objectInputStream, objectOutputStream);
                            adminTaskClient.handleAdminTasks();
                        } else if ("chef".equalsIgnoreCase(role)) {
                            ChefTaskClient chefTaskClient = new ChefTaskClient(objectInputStream, objectOutputStream);
                            chefTaskClient.handleChefTasks();
                        }
                    }

                    System.out.print("Do you want to continue? (yes/no): ");
                    String choice = bufferedReader.readLine().trim();
                    if (!choice.equalsIgnoreCase("yes")) {
                        continueLogin = false;
                    }

                } catch (InvalidInputException exception) {
                    System.out.println(exception.getMessage());
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

    private static String login(User user) throws IOException, ClassNotFoundException {
        try {
            socket = new Socket("localhost", 5000);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(String.valueOf(user.getUserId()));
            objectOutputStream.writeObject(user.getPassword());

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
