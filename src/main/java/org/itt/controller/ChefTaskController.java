package org.itt.controller;

import org.itt.constant.ChefAction;
import org.itt.service.ChefService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChefTaskController {
    private final ChefService chefService;

    public ChefTaskController() {
        this.chefService = new ChefService();
    }

    public void handleChefTasks(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        try {
            while (true) {
                String menu = chefService.getChefMenu();
                objectOutputStream.writeObject(menu);

                int choiceValue = (int) objectInputStream.readObject();
                ChefAction choice;
                try {
                    choice = ChefAction.fromValue(choiceValue);
                } catch (IllegalArgumentException e) {
                    objectOutputStream.writeObject("Invalid choice. Please try again.");
                    continue;
                }

                String response;
                switch (choice) {
                    case VIEW_HIGH_RATED_ITEMS:
                        response = chefService.viewHighRatedItems();
                        break;
                    case SELECT_ITEMS_FOR_NEXT_DAY:
                        response = selectItemsForNextDay(objectInputStream);
                        break;
                    case VIEW_DISCARD_MENU:
                        response = chefService.viewDiscardMenu();
                        break;
                    case REMOVE_FOOD_ITEM:
                        response = removeFoodItem(objectInputStream);
                        break;
                    case ADD_ITEM_FOR_DETAILED_FEEDBACK:
                        response = addItemForDetailedFeedback(objectInputStream);
                        break;
                    case EXIT:
                        response = "Exiting...";
                        objectOutputStream.writeObject(response);
                        return;
                    default:
                        response = "Invalid choice.";
                        break;
                }

                objectOutputStream.writeObject(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                objectOutputStream.writeObject("An error occurred while processing your request. Please try again.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private String selectItemsForNextDay(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String input = (String) objectInputStream.readObject();
        return chefService.selectItemsForNextDay(input);
    }

    private String removeFoodItem(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String itemIdString = (String) objectInputStream.readObject();
        int itemId = Integer.parseInt(itemIdString);
        return chefService.removeFoodItem(itemId);
    }

    private String addItemForDetailedFeedback(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String itemIdString = (String) objectInputStream.readObject();
        int itemId = Integer.parseInt(itemIdString);
        return chefService.addItemForDetailedFeedback(itemId);
    }
}
