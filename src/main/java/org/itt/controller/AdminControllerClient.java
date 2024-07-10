package org.itt.controller;

import org.itt.constant.AdminAction;
import org.itt.entity.Item;
import org.itt.exception.InvalidInputException;
import org.itt.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AdminControllerClient {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final UserService userService;

    public AdminControllerClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.userService = new UserService();
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
                        handleAddUser();
                        break;
                    case ADD_MENU_ITEM:
                        handleAddMenuItem();
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

    private void handleAddUser() throws IOException {
        try {
            String name = userService.getUserName();
            String role = userService.getUserRole();
            String password = userService.getValidPassword();
            objectOutputStream.writeObject(name);
            objectOutputStream.writeObject(role);
            objectOutputStream.writeObject(password);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleAddMenuItem() throws IOException {
        Item item = new Item();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter item name: ");
        item.setItemName(bufferedReader.readLine());

        System.out.print("Enter price: ");
        item.setPrice(Double.parseDouble(bufferedReader.readLine()));

        System.out.print("Enter availability status: ");
        item.setAvailabilityStatus(bufferedReader.readLine());

        System.out.print("Enter meal type(breakfast,lunch,dinner): ");
        item.setMealType(bufferedReader.readLine());

        System.out.print("Enter description: ");
        item.setDescription(bufferedReader.readLine());

        System.out.print("Enter food type( Vegetarian,Non Vegetarian,Eggetarian): ");
        item.setFoodType(bufferedReader.readLine());

        System.out.print("Enter spice level(high,medium,low): ");
        item.setSpiceLevel(bufferedReader.readLine());

        System.out.print("Enter cuisine type(north indian, south indian, other): ");
        item.setCuisineType(bufferedReader.readLine());

        System.out.print("Is it sweet? (true/false): ");
        item.setSweet(Boolean.parseBoolean(bufferedReader.readLine()));

        objectOutputStream.writeObject(item);
    }

    private void handleUpdateMenuItem(BufferedReader bufferedReader) throws IOException {
        System.out.print("Enter item ID to update: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);

        handleAddMenuItem();
    }

    private void handleDeleteMenuItem(BufferedReader bufferedReader) throws IOException {
        System.out.print("Enter item ID to delete: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);
    }
}
