package org.itt.service;

import org.itt.constant.ChefAction;
import org.itt.dao.ItemRepository;
import org.itt.dao.NextDayItemRepository;
import org.itt.dao.NotificationRepository;
import org.itt.entity.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ChefService {

    private ItemRepository itemRepository;
    private NextDayItemRepository nextDayItemRepository;
    private NotificationRepository notificationRepository;

    public ChefService() {
        itemRepository = new ItemRepository();
        nextDayItemRepository = new NextDayItemRepository();
        notificationRepository = new NotificationRepository();
    }

    public void performChefTasks() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            ChefAction choice;
            do {
                displayMenu();

                int choiceValue;
                try {
                    choiceValue = Integer.parseInt(reader.readLine());
                    choice = ChefAction.fromValue(choiceValue);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    choice = null;
                }

                if (choice != null) {
                    switch (choice) {
                        case VIEW_HIGH_RATED_ITEMS:
                            viewHighRatedItems();
                            break;
                        case SELECT_ITEMS_FOR_NEXT_DAY:
                            selectItemsForNextDay(reader);
                            break;
                        case EXIT:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            } while (choice != ChefAction.EXIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewHighRatedItems() {
        try {
            List<Item> items = itemRepository.getTopRatedItems();
            System.out.printf("%-10s %-20s %-10s %-20s %-15s %-50s %-15s%n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description", "Average Rating");
            System.out.println("=========================================================================================================================");
            for (Item item : items) {
                System.out.printf("%-10d %-20s %-10.2f %-20s %-15s %-50s %-15.2f%n",
                        item.getItemId(),
                        item.getItemName(),
                        item.getPrice(),
                        item.getAvailabilityStatus(),
                        item.getMealType(),
                        item.getDescription(),
                        item.getAverageRating());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void selectItemsForNextDay(BufferedReader reader) {
        try {
            System.out.println("Enter the IDs of the items to select for the next day (comma-separated): ");
            String input = reader.readLine();
            String[] itemIdsStr = input.split(",");
            int[] itemIds = new int[itemIdsStr.length];

            for (int i = 0; i < itemIdsStr.length; i++) {
                itemIds[i] = Integer.parseInt(itemIdsStr[i].trim());
            }

            nextDayItemRepository.addNextDayItems(itemIds);
            System.out.println("Selected items have been added to the next day item list.");
            notifyEmployees(itemIds);

        } catch (IOException | NumberFormatException | SQLException | ClassNotFoundException e) {
            System.out.println("Invalid input. Please enter a valid list of item IDs.");
        }
    }

    private void notifyEmployees(int[] itemIds) throws SQLException, ClassNotFoundException {
        StringBuilder messageBuilder = new StringBuilder("Items selected for next day: ");
        for (int i = 0; i < itemIds.length; i++) {
            messageBuilder.append(itemIds[i]);
            if (i < itemIds.length - 1) {
                messageBuilder.append(", ");
            }
        }
        String message = messageBuilder.toString();
        notificationRepository.addNotification("Chef", message);
    }


    private void displayMenu() {
        System.out.println("1. View High rated Items ");
        System.out.println("2. Select Items for Next Day");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }
}
