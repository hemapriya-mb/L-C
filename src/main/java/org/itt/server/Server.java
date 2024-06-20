package org.itt.server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("user_activity.log", true);
            fileHandler.setFormatter(new org.itt.utility.CustomFormatter());
            logger.addHandler(fileHandler);

            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is listening on port 5000");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logActivity(String action, int userId) {
        logger.log(Level.INFO, action, new Object[] { userId });
    }
}
