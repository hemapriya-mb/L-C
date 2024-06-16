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
         try (InputStream input = socket.getInputStream();
              OutputStream output = socket.getOutputStream();
              ObjectOutputStream oos = new ObjectOutputStream(output);
              ObjectInputStream ois = new ObjectInputStream(input)) {

             int userId = (int) ois.readObject();
             String password = (String) ois.readObject();

             UserRepository userRepository = new UserRepository();
             User user = userRepository.authenticate(userId, password);
             oos.writeObject(user);

         } catch (IOException | ClassNotFoundException | SQLException e) {
             e.printStackTrace();
         }
    }
}
