package org.itt.utility;

import org.itt.constant.AdminAction;
import org.itt.entity.Item;
import org.itt.entity.User;
import org.itt.exception.InvalidInputException;
import org.itt.service.ItemService;
import org.itt.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AdminTaskClient {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final UserService userService;
    private final ItemService itemService;

    public AdminTaskClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.userService = new UserService();
        this.itemService = new ItemService();
    }

    public void handleAdminTasks() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String menu = (String) objectInputStream.readObject();
                System.out.println(menu);

                System.out.print("Enter your choice: ");
                int choiceStr = Integer.parseInt(bufferedReader.readLine());
                objectOutputStream.writeObject(choiceStr);

                AdminAction choice;
                try {
                    choice = AdminAction.fromValue(choiceStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                switch (choice) {
                    case ADD_NEW_USER:
                        handleAddUser(bufferedReader);
                        break;
                    case ADD_MENU_ITEM:
                        handleAddMenuItem(bufferedReader);
                        break;
                    case UPDATE_MENU_ITEM:
                        handleUpdateMenuItem(bufferedReader);
                        break;
                    case DELETE_MENU_ITEM:
                        handleDeleteMenuItem(bufferedReader);
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

                String response = (String) objectInputStream.readObject();
                System.out.println(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("An error occurred while processing your request. Please try again.");
        }
    }

    private void handleAddUser(BufferedReader bufferedReader) throws IOException {
        try {
            User user = userService.getUserDetail();
            objectOutputStream.writeObject(user.getName());
            objectOutputStream.writeObject(user.getRole());
            objectOutputStream.writeObject(user.getPassword());
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleAddMenuItem(BufferedReader bufferedReader) throws IOException {
        try {
            Item item = itemService.getItemDetails();
            objectOutputStream.writeObject(item.getItemName());
            objectOutputStream.writeObject(item.getPrice());
            objectOutputStream.writeObject(item.getAvailabilityStatus());
            objectOutputStream.writeObject(item.getMealType());
            objectOutputStream.writeObject(item.getDescription());
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleUpdateMenuItem(BufferedReader bufferedReader) throws IOException {
        try {
            System.out.print("Enter item ID to update: ");
            int itemId = Integer.parseInt(bufferedReader.readLine());
            objectOutputStream.writeObject(itemId);

            Item item = itemService.getItemDetails();
            objectOutputStream.writeObject(item.getItemName());
            objectOutputStream.writeObject(item.getPrice());
            objectOutputStream.writeObject(item.getAvailabilityStatus());
            objectOutputStream.writeObject(item.getMealType());
            objectOutputStream.writeObject(item.getDescription());
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteMenuItem(BufferedReader bufferedReader) throws IOException {
        System.out.print("Enter item ID to delete: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);
    }
}
