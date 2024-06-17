package org.itt.utility;

import org.itt.constant.EmployeeAction;
import org.itt.dao.FeedbackRepository;
import org.itt.dao.ItemRepository;
import org.itt.dao.OrderHistoryRepository;
import org.itt.entity.Feedback;
import org.itt.entity.Item;
import org.itt.entity.OrderHistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class EmployeeOperation {

    public void performEmployeeTasks(int userId) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            EmployeeAction choice;

            do {
                displayMenu();

                int choiceValue;
                try {
                    choiceValue = Integer.parseInt(reader.readLine());
                    choice = EmployeeAction.fromValue(choiceValue);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    choice = null;
                }

                if (choice != null) {
                    switch (choice) {
                        case ORDER_FOOD:
                            orderFood(userId);
                            break;
                        case GIVE_FEEDBACK:
                            giveFeedback(userId);
                            break;
                        case VIEW_ORDER_HISTORY:
                            viewOrderHistory(userId);
                            break;
                        case POLL_FOR_NEXT_DAY_ITEMS:
                            pollForNextDayItems();
                            break;
                        case EXIT:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            } while (choice != EmployeeAction.EXIT);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void displayMenu() {
        System.out.println("1. Order Food");
        System.out.println("2. Give Feedback");
        System.out.println("3. View Order History");
        System.out.println("4. Poll for next day item");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private void orderFood(int userId) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ItemRepository itemRepository = new ItemRepository();

            List<Item> items = itemRepository.getAllItems();
            if (items.isEmpty()) {
                System.out.println("No items available.");
                return;
            }

            System.out.println("Available Items:");
            System.out.printf("%-10s %-20s %-10s %-15s %-15s %-20s%n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description");
            System.out.println("-------------------------------------------------------------------------------------------------------");
            for (Item item : items) {
                System.out.printf("%-10d %-20s %-10.2f %-15s %-15s %-20s%n",
                        item.getItemId(), item.getItemName(), item.getPrice(), item.getAvailabilityStatus(), item.getMealType(), item.getDescription());
            }

            System.out.print("Enter the item ID to order: ");
            int itemId = Integer.parseInt(reader.readLine());


            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setUserId(userId);
            orderHistory.setItemId(itemId);
            OrderHistoryRepository orderHistoryRepository = new OrderHistoryRepository();
            orderHistoryRepository.addOrder(orderHistory);

            System.out.println("Order placed successfully!");

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error placing order. Please try again.");
        }
    }

    private void giveFeedback(int userId) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter item ID: ");
            int itemId = Integer.parseInt(reader.readLine());

            System.out.print("Enter rating (1 to 5): ");
            int rating = Integer.parseInt(reader.readLine());

            System.out.print("Enter comments (optional): ");
            String comments = reader.readLine();

            Feedback feedback = new Feedback();
            feedback.setUserId(userId);
            feedback.setItemId(itemId);
            feedback.setRating(rating);
            feedback.setComments(comments);

            FeedbackRepository feedbackRepository = new FeedbackRepository();
            feedbackRepository.addFeedback(feedback);

            System.out.println("Feedback submitted successfully!");

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error submitting feedback. Please try again.");
        }
    }

    private void viewOrderHistory(int userId) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            OrderHistoryRepository orderHistoryRepository = new OrderHistoryRepository();
            List<OrderHistory> orderHistoryList = orderHistoryRepository.getOrderHistoryByUserId(userId);

            if (orderHistoryList.isEmpty()) {
                System.out.println("No order history found.");
            } else {
                System.out.printf("%-10s  %-10s %-20s%n", "Order ID", "Item ID", "Order Date");
                System.out.println("-----------------------------------------------------");
                for (OrderHistory orderHistory : orderHistoryList) {
                    System.out.printf("%-10d %-10d %-10d %-20s%n",
                            orderHistory.getOrderId(),
                            orderHistory.getItemId(),
                            orderHistory.getOrderDate().toString());
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error fetching order history. Please try again.");
        }
    }

    private void pollForNextDayItems() throws IOException, SQLException, ClassNotFoundException {
        ItemRepository itemRepository = new ItemRepository();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Item> items = itemRepository.getNextDayItems();

        System.out.println("Items for the Next Day:");
        System.out.println("---------------------------------------------------------------------------------");
        for (Item item : items) {
            System.out.println("Item ID: " + item.getItemId() + ", Item Name: " + item.getItemName());
        }
        System.out.println("\nEnter the item ID you want to poll for:");
        int itemId = Integer.parseInt(reader.readLine());

        itemRepository.pollForNextDayItem(itemId);

        System.out.println("Your vote has been recorded.");
    }
}
