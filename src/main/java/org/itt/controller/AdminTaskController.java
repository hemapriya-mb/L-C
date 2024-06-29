package org.itt.controller;

import org.itt.constant.AdminAction;
import org.itt.service.AdminService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AdminTaskController {
    private final AdminService adminService;

    public AdminTaskController() {
        this.adminService = new AdminService();
    }

    public void handleAdminTasks(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        try {
            while (true) {
                String menu = adminService.getAdminMenu();
                objectOutputStream.writeObject(menu);

                int choiceStr = (Integer) objectInputStream.readObject();
                AdminAction choice;
                try {
                    choice = AdminAction.fromValue(choiceStr);
                } catch (IllegalArgumentException e) {
                    objectOutputStream.writeObject("Invalid choice. Please try again.");
                    continue;
                }

                String response;
                switch (choice) {
                    case ADD_NEW_USER:
                        response = addUser(objectInputStream);
                        break;
                    case ADD_MENU_ITEM:
                        response = addMenuItem(objectInputStream);
                        break;
                    case UPDATE_MENU_ITEM:
                        response = updateMenuItem(objectInputStream);
                        break;
                    case DELETE_MENU_ITEM:
                        response = deleteMenuItem(objectInputStream);
                        break;
                    case EXIT:
                        response = "Exiting...";
                        objectOutputStream.writeObject(response);
                        return;
                    default:
                        response = adminService.executeAdminTask(choiceStr);
                        break;
                }

                objectOutputStream.writeObject(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                objectOutputStream.writeObject("An error occurred while processing your request. Please try again.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private String addUser(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String name = (String) objectInputStream.readObject();
        String role = (String) objectInputStream.readObject();
        String password = (String) objectInputStream.readObject();

        return adminService.addNewUser(name, role, password);
    }

    private String addMenuItem(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String itemName = (String) objectInputStream.readObject();
        double price = (Double) objectInputStream.readObject();
        String availabilityStatus = (String) objectInputStream.readObject();
        String mealType = (String) objectInputStream.readObject();
        String description = (String) objectInputStream.readObject();

        return adminService.addMenuItem(itemName, price, availabilityStatus, mealType, description);
    }

    private String updateMenuItem(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int itemId = (Integer) objectInputStream.readObject();
        String itemName = (String) objectInputStream.readObject();
        double price = (Double) objectInputStream.readObject();
        String availabilityStatus = (String) objectInputStream.readObject();
        String mealType = (String) objectInputStream.readObject();
        String description = (String) objectInputStream.readObject();

        return adminService.updateMenuItem(itemId, itemName, price, availabilityStatus, mealType, description);
    }

    private String deleteMenuItem(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int itemId = (Integer) objectInputStream.readObject();
        return adminService.deleteMenuItem(itemId);
    }
}
