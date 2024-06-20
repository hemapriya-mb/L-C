package org.itt.server;

import org.itt.dao.UserRepository;
import org.itt.entity.User;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            String userIdString = (String) objectInputStream.readObject();
            int userId = Integer.parseInt(userIdString);
            String password = (String) objectInputStream.readObject();

            UserRepository userRepository = new UserRepository();
            User user = userRepository.getUserByUserIdAndPassword(userId, password);

            if (user != null) {
                Server.logActivity("Login", user.getUserId());
            }

            objectOutputStream.writeObject(user);

            inputStream.read();
            Server.logActivity("Logout", userId);
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
