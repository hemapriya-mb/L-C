package org.itt.service;

import org.itt.constant.ChefAction;
import org.itt.entity.Item;
import org.itt.dao.NextDayItemsRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class ChefService {
    private final NextDayItemsRepository nextDayItemsRepository = new NextDayItemsRepository();

    public void performChefTasks() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
                        case VIEW_TOP_ITEMS:
                            viewTopItemsByPoll();
                            break;
                        case SELECT_ITEMS_FOR_NEXT_DAY:
                            selectItemsForNextDay();
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

    private void selectItemsForNextDay() {
        try {
            List<Item> topRatedItems = nextDayItemsRepository.getTopRatedItems();
            System.out.println("Top 10 Items Based on Average Rating:");
            System.out.println("ID    Name              Meal Type       Average Rating");
            System.out.println("------------------------------------------------------");
            for (Item item : topRatedItems) {
                System.out.printf("%-5d %-17s %-15s %.2f%n", item.getItemId(), item.getItemName(), item.getMealType(), item.getAverageRating());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter the item IDs to select for the next day (comma-separated):");
            String[] itemIds = reader.readLine().split(",");
            for (String itemIdString : itemIds) {
                try {
                    int itemId = Integer.parseInt(itemIdString.trim());
                    nextDayItemsRepository.addItemForNextDay(itemId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid item ID format: " + itemIdString);
                }
            }

            System.out.println("Items selected for the next day successfully.");

        } catch (SQLException | ClassNotFoundException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTopItemsByPoll() {
        try {
            List<Item> topRatedItems = nextDayItemsRepository.getTopItemsByPollCount(5);
            System.out.println("Top 5 Items Based on Poll Count:");
            System.out.println("ID    Name              Poll Count");
            System.out.println("------------------------------");
            for (Item item : topRatedItems) {
                System.out.printf("%-5d %-17s %-10d%n", item.getItemId(), item.getItemName(), item.getPollCount());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayMenu() {
        System.out.println("1. View Top Items by Poll Count");
        System.out.println("2. Select Items for Next Day");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }
}
