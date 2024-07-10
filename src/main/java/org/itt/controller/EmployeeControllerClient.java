package org.itt.controller;

import org.itt.constant.EmployeeAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EmployeeControllerClient {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public EmployeeControllerClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public void handleEmployeeTasks(int userId) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String menu = (String) objectInputStream.readObject();
                System.out.println(menu);

                int userChoice = Integer.parseInt(bufferedReader.readLine().trim());
                objectOutputStream.writeObject(userChoice);
                objectOutputStream.writeObject(userId);

                EmployeeAction choice;
                try {
                    choice = EmployeeAction.fromValue(userChoice);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }

                switch (choice) {
                    case ORDER_FOOD:
                        handleOrderFood(bufferedReader);
                        break;
                    case VIEW_ORDER_HISTORY:
                    case VIEW_NOTIFICATIONS:
                    case GET_RECOMMENDATIONS:
                        handleResponse();
                        break;
                    case POLL_FOR_NEXT_DAY_ITEMS:
                        handlePollForNextDayItems(bufferedReader);
                        break;
                    case GIVE_FEEDBACK:
                        handleGiveFeedback(bufferedReader);
                        break;
                    case GIVE_DETAILED_FEEDBACK:
                        handleGiveDetailedFeedback(bufferedReader);
                        break;
                    case UPDATE_PROFILE:
                        handleUpdateProfile(bufferedReader);
                        break;
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred while processing your request. Please try again.");
        }
    }

    private void handleOrderFood(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        handleResponse();

        System.out.print("Enter the item ID to order: ");
        int itemId = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(itemId);

        handleResponse();
    }

    private void handlePollForNextDayItems(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        handleResponse();

        System.out.print("Enter the item ID to poll for: ");
        int itemId = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(itemId);

        handleResponse();
    }

    private void handleGiveFeedback(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        System.out.print("Enter the order ID: ");
        int orderId = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(orderId);

        System.out.print("Enter the item ID: ");
        int itemId = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(itemId);

        System.out.print("Enter the rating (1-5): ");
        int rating = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(rating);

        System.out.print("Enter your comments: ");
        String comment = bufferedReader.readLine();
        objectOutputStream.writeObject(comment);

        handleResponse();
    }

    private void handleGiveDetailedFeedback(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        System.out.println("Fetching items eligible for detailed feedback...");
        objectOutputStream.writeObject("FETCH_DETAILED_FEEDBACK_ITEMS");

        String response = (String) objectInputStream.readObject();
        System.out.println(response);

        System.out.print("Enter the item ID for detailed feedback: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);

        String question1 = (String) objectInputStream.readObject();
        System.out.println(question1);
        String answer1 = bufferedReader.readLine();
        objectOutputStream.writeObject(answer1);

        String question2 = (String) objectInputStream.readObject();
        System.out.println(question2);
        String answer2 = bufferedReader.readLine();
        objectOutputStream.writeObject(answer2);

        String question3 = (String) objectInputStream.readObject();
        System.out.println(question3);
        String answer3 = bufferedReader.readLine();
        objectOutputStream.writeObject(answer3);

        handleResponse();
    }

    private void handleUpdateProfile(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        System.out.println("Please answer these questions to know your preferences");

        System.out.println("1) Please select one - \n1. Vegetarian\n2. Non Vegetarian\n3. Eggetarian");
        int foodTypeChoice = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(foodTypeChoice);

        System.out.println("2) Please select your spice level - \n1. High\n2. Medium\n3. Low");
        int spiceLevelChoice = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(spiceLevelChoice);

        System.out.println("3) What do you prefer most? - \n1. North Indian\n2. South Indian\n3. Other");
        int cuisineChoice = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(cuisineChoice);

        System.out.println("4) Do you have a sweet tooth? - \n1. Yes\n2. No");
        int sweetToothChoice = Integer.parseInt(bufferedReader.readLine().trim());
        objectOutputStream.writeObject(sweetToothChoice);

        handleResponse();
    }

    private void handleResponse() throws IOException, ClassNotFoundException {
        String response = (String) objectInputStream.readObject();
        System.out.println(response);
    }
}
