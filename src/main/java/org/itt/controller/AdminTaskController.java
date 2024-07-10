package org.itt.controller;

import org.itt.constant.AdminAction;
import org.itt.entity.Item;
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
        Item newItem = new Item();
        newItem.setItemName((String) objectInputStream.readObject());
        newItem.setPrice((Double) objectInputStream.readObject());
        newItem.setAvailabilityStatus((String) objectInputStream.readObject());
        newItem.setMealType((String) objectInputStream.readObject());
        newItem.setDescription((String) objectInputStream.readObject());
        newItem.setFoodType((String) objectInputStream.readObject());
        newItem.setSpiceLevel((String) objectInputStream.readObject());
        newItem.setCuisineType((String) objectInputStream.readObject());
        newItem.setSweet((Boolean) objectInputStream.readObject());

        return adminService.addMenuItem(newItem);
    }

    private String updateMenuItem(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Item updatedItem = new Item();
        updatedItem.setItemId((Integer) objectInputStream.readObject());
        updatedItem.setItemName((String) objectInputStream.readObject());
        updatedItem.setPrice((Double) objectInputStream.readObject());
        updatedItem.setAvailabilityStatus((String) objectInputStream.readObject());
        updatedItem.setMealType((String) objectInputStream.readObject());
        updatedItem.setDescription((String) objectInputStream.readObject());
        updatedItem.setFoodType((String) objectInputStream.readObject());
        updatedItem.setSpiceLevel((String) objectInputStream.readObject());
        updatedItem.setCuisineType((String) objectInputStream.readObject());
        updatedItem.setSweet((Boolean) objectInputStream.readObject());

        return adminService.updateMenuItem(updatedItem);
    }


    private String deleteMenuItem(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        int itemId = (Integer) objectInputStream.readObject();
        return adminService.deleteMenuItem(itemId);
    }
}
