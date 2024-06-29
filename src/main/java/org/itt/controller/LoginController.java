package org.itt.controller;

import org.itt.dao.UserRepository;
import org.itt.entity.User;
import org.itt.exception.DatabaseException;
import org.itt.utility.Server;

import java.io.*;
import java.net.Socket;

public class LoginController extends Thread {
    private final Socket socket;
    private final UserRepository userRepository;

    public LoginController(Socket socket) {
        this.socket = socket;
        this.userRepository = new UserRepository();
    }

    public void run()  {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            String userIdString = (String) objectInputStream.readObject();
            int userId = Integer.parseInt(userIdString);
            String password = (String) objectInputStream.readObject();

            User user = userRepository.getUserByUserIdAndPassword(userId, password);

            if (user != null) {
                Server.logActivity("Login", user.getUserId());
                objectOutputStream.writeObject("Login successful:" + user.getRole());

                if ("admin".equalsIgnoreCase(user.getRole())) {
                    AdminTaskController adminTaskController = new AdminTaskController();
                    adminTaskController.handleAdminTasks(objectInputStream, objectOutputStream);
                } else if ("chef".equalsIgnoreCase(user.getRole())) {
                    ChefTaskController chefTaskController = new ChefTaskController();
                    chefTaskController.handleChefTasks(objectInputStream, objectOutputStream);
                }
            } else {
                objectOutputStream.writeObject("Login failed!");
            }

            String clientMessage = (String) objectInputStream.readObject();
            if ("LOGOUT".equals(clientMessage)) {
                Server.logActivity("Logout", userId);
            }

        } catch (IOException | ClassNotFoundException | DatabaseException e) {
            e.printStackTrace();
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
                objectOutputStream.writeObject("An error occurred while processing your request. Please try again.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
