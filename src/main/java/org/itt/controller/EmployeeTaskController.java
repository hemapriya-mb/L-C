package org.itt.controller;

import org.itt.constant.EmployeeAction;
import org.itt.service.EmployeeService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EmployeeTaskController {
    private final EmployeeService employeeService;

    public EmployeeTaskController() {
        this.employeeService = new EmployeeService();
    }

    public void handleEmployeeTasks(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
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
                if ("FETCH_DETAILED_FEEDBACK_ITEMS".equals(choiceValue)) {
                    String response = employeeService.getItemsForDetailedFeedback();
                    objectOutputStream.writeObject(response);
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
            e.printStackTrace();
            try {
                objectOutputStream.writeObject("An error occurred while processing your request. Please try again.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private String handleGiveFeedback(ObjectInputStream objectInputStream, int userId) throws IOException, ClassNotFoundException {
        int orderId = (int) objectInputStream.readObject();
        int itemId = (int) objectInputStream.readObject();
        int rating = (int) objectInputStream.readObject();
        String comment = (String) objectInputStream.readObject();
        return employeeService.giveFeedback(userId, orderId, itemId, rating, comment);
    }

    private String handleGiveDetailedFeedback(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, int userId) throws IOException, ClassNotFoundException {
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
}
