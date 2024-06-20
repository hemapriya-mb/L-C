package org.itt.service;

import org.itt.constant.EmployeeAction;
import org.itt.constant.FeedbackComment;
import org.itt.dao.*;
import org.itt.entity.Feedback;
import org.itt.entity.Item;
import org.itt.entity.OrderHistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeeService {
    private final ItemRepository itemRepository = new ItemRepository();
    private final PollRepository pollRepository = new PollRepository();
    private final NextDayItemsRepository nextDayItemsRepository = new NextDayItemsRepository();
//    private final NotificationRepository notificationRepository = new NotificationRepository();


    public void performEmployeeTasks(int userId) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            EmployeeAction choice;

            do {
//                viewNotifications();
                displayMenu();

                int choiceValue;
                try {
                    choiceValue = Integer.parseInt(bufferedReader.readLine());
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
                            pollForNextDayItems(userId);
                            break;
                        case EXIT:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            } while (choice != EmployeeAction.EXIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    private void viewNotifications() {
//        try {
//            List<Notification> notifications = notificationRepository.getNotificationsForEmployee();
//            System.out.println("Notifications:");
//            for (Notification notification : notifications) {
//                System.out.println(notification.getMessage() + " (Received: " + notification.getTimestamp() + ")\n");
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            System.out.println("Error retrieving notifications: " + e.getMessage());
//        }
//    }
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
            FeedbackRepository feedbackRepository = new FeedbackRepository();

            System.out.print("Enter the item ID: ");
            int itemId = Integer.parseInt(reader.readLine());

            System.out.print("Enter the rating (1-5): ");
            int rating = Integer.parseInt(reader.readLine());

            System.out.println("Select a comment:");
            for (FeedbackComment comment : FeedbackComment.values()) {
                System.out.println(comment.ordinal() + 1 + ". " + comment.getComment());
            }

            int commentChoice = Integer.parseInt(reader.readLine());
            FeedbackComment feedbackComment = FeedbackComment.fromValue(commentChoice);

            Feedback feedback = new Feedback(userId, itemId, rating, feedbackComment.getComment());
            feedbackRepository.addFeedback(feedback);

            System.out.println("Feedback submitted successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input. Please try again.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewOrderHistory(int userId) {
        try {
            OrderHistoryRepository orderHistoryRepository = new OrderHistoryRepository();
            List<OrderHistory> orderHistoryList = orderHistoryRepository.getOrderHistoryByUserId(userId);

            if (orderHistoryList.isEmpty()) {
                System.out.println("No order history found.");
            } else {
                System.out.printf("%-10s  %-10s %-20s%n", "Order ID", "Item ID", "Order Date");
                System.out.println("-----------------------------------------------------");
                for (OrderHistory orderHistory : orderHistoryList) {
                    System.out.printf("%-10d %-10d %-20s%n",
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

    public void pollForNextDayItems(int userId) {
        try {
            List<Item> nextDayItems = nextDayItemsRepository.getNextDayItems();
            System.out.println("Items available for next day:");
            System.out.println("ID    Name              Poll Count");
            System.out.println("----------------------------------");
            for (Item item : nextDayItems) {
                System.out.printf("%-5d %-17s %-15d%n", item.getItemId(), item.getItemName(), item.getPollCount());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the item ID to poll for: ");
            int itemId = Integer.parseInt(reader.readLine().trim());

            Date pollDate = Date.valueOf(LocalDate.now());

            if (pollRepository.hasPolled(userId, itemId, pollDate)) {
                System.out.println("You have already polled for this item today.");
            } else {
                pollRepository.addPoll(userId, itemId, pollDate);
                nextDayItemsRepository.incrementPollCount(itemId);
                System.out.println("Your poll has been recorded.");
            }

        } catch (SQLException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
