package org.itt.utility;

import org.itt.constant.EmployeeAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EmployeeTaskClient {

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public EmployeeTaskClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public void handleEmployeeTasks(int userId) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String menu = (String) objectInputStream.readObject();
                System.out.println(menu);

                int userChoice = Integer.parseInt(bufferedReader.readLine());
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
                    case EXIT:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("An error occurred while processing your request. Please try again.");
        }
    }

    private void handleOrderFood(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        handleResponse();

        System.out.print("Enter the item ID to order: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);

        handleResponse();
    }

    private void handlePollForNextDayItems(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        handleResponse();

        System.out.print("Enter the item ID to poll for: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);

        handleResponse();
    }

    private void handleGiveFeedback(BufferedReader bufferedReader) throws IOException, ClassNotFoundException {
        System.out.print("Enter the order ID: ");
        int orderId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(orderId);

        System.out.print("Enter the item ID: ");
        int itemId = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(itemId);

        System.out.print("Enter the rating (1-5): ");
        int rating = Integer.parseInt(bufferedReader.readLine());
        objectOutputStream.writeObject(rating);

        System.out.print("Enter your comments: ");
        String comment = bufferedReader.readLine();
        objectOutputStream.writeObject(comment);

        handleResponse();
    }

    private void handleResponse() throws IOException, ClassNotFoundException {
        String response = (String) objectInputStream.readObject();
        System.out.println(response);
    }
}
