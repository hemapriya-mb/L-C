package org.itt.controller;

import org.itt.constant.EmployeeAction;
import org.itt.entity.Item;
import org.itt.exception.DatabaseException;
import org.itt.service.EmployeeService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class EmployeeTaskController {
    private final EmployeeService employeeService;

    public EmployeeTaskController() {
        this.employeeService = new EmployeeService();
    }

    public void handleEmployeeTasks(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws IOException {
        try {
            while (true) {
                String menu = employeeService.getEmployeeMenu();
                objectOutputStream.writeObject(menu);

                int choiceValue = (int) objectInputStream.readObject();
                int userId = (int) objectInputStream.readObject();
                EmployeeAction choice;
                try {
                    choice = EmployeeAction.fromValue(choiceValue);
                } catch (IllegalArgumentException e) {
                    objectOutputStream.writeObject("Invalid choice. Please try again.");
                    continue;
                }

                String response;
                switch (choice) {
                    case ORDER_FOOD:
                        response = employeeService.orderFood(userId);
                        objectOutputStream.writeObject(response);
                        int itemId = (int) objectInputStream.readObject();
                        response = employeeService.placeOrder(userId, itemId);
                        break;
                    case VIEW_ORDER_HISTORY:
                        response = employeeService.viewOrderHistory(userId);
                        break;
                    case VIEW_NOTIFICATIONS:
                        response = employeeService.viewNotifications(userId);
                        break;
                    case POLL_FOR_NEXT_DAY_ITEMS:
                        response = employeeService.getNextDayItems();
                        objectOutputStream.writeObject(response);
                        itemId = (int) objectInputStream.readObject();
                        response = employeeService.pollForNextDayItems(userId, itemId);
                        break;
                    case GIVE_FEEDBACK:
                        response = handleGiveFeedback(objectInputStream, userId);
                        break;
                    case GET_RECOMMENDATIONS:
                        response = employeeService.getRecommendations();
                        break;
                    case GIVE_DETAILED_FEEDBACK:
                        response = handleGiveDetailedFeedback(objectInputStream, objectOutputStream, userId);
                        break;
                    case UPDATE_PROFILE:
                        response = handleUpdateProfile(objectInputStream, userId);
                        break;
                    case GET_RECOMMENDATION_BY_PROFILE:
                        handleGetRecommendations(objectInputStream, objectOutputStream, userId);
                        continue;  // avoid writing response as string
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
        } catch (IOException | ClassNotFoundException | DatabaseException e) {
            objectOutputStream.writeObject("An error occurred while processing your request. Please try again.");
        }
    }

    private String handleGiveFeedback(ObjectInputStream objectInputStream, int userId) throws IOException, ClassNotFoundException {
        int orderId = (int) objectInputStream.readObject();
        int itemId = (int) objectInputStream.readObject();
        int rating = (int) objectInputStream.readObject();
        String comment = (String) objectInputStream.readObject();
        return employeeService.giveFeedback(userId, orderId, itemId, rating, comment);
    }

    private String handleGiveDetailedFeedback(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, int userId) throws IOException, ClassNotFoundException, DatabaseException {
        if ("FETCH_DETAILED_FEEDBACK_ITEMS".equals(objectInputStream.readObject())) {
            String response = employeeService.getItemsForDetailedFeedback();
            objectOutputStream.writeObject(response);
        }

        int itemId = (int) objectInputStream.readObject();

        String question1 = "Q1. What didn't you like about this item?";
        String question2 = "Q2. How would you like this item to taste?";
        String question3 = "Q3. Share your mom's recipe.";

        objectOutputStream.writeObject(question1);
        String answer1 = (String) objectInputStream.readObject();

        objectOutputStream.writeObject(question2);
        String answer2 = (String) objectInputStream.readObject();

        objectOutputStream.writeObject(question3);
        String answer3 = (String) objectInputStream.readObject();

        return employeeService.giveDetailedFeedback(userId, itemId, answer1, answer2, answer3);
    }

    private String handleUpdateProfile(ObjectInputStream objectInputStream, int userId) throws IOException, ClassNotFoundException, DatabaseException {
        int foodTypeChoice = (int) objectInputStream.readObject();
        int spiceLevelChoice = (int) objectInputStream.readObject();
        int cuisineChoice = (int) objectInputStream.readObject();
        int sweetToothChoice = (int) objectInputStream.readObject();

        return employeeService.updateProfile(userId, foodTypeChoice, spiceLevelChoice, cuisineChoice, sweetToothChoice);
    }

    public void handleGetRecommendations(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, int userId) throws IOException {
        try {
            List<Item> recommendedItems = employeeService.getRecommendedItems(userId);
            objectOutputStream.writeObject(recommendedItems);
        } catch (DatabaseException | IOException e) {
            objectOutputStream.writeObject("Failed to get recommendations: " + e.getMessage());
        }
    }
}
