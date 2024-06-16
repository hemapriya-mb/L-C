package org.itt.utility;

import org.itt.client.ItemDetail;
import org.itt.client.UserDetail;
import org.itt.constant.AdminAction;
import org.itt.dao.ItemRepository;
import org.itt.dao.UserRepository;
import org.itt.entity.Item;
import org.itt.entity.User;
import org.itt.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class AdminOperation {
    private final UserDetail userDetail;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public AdminOperation() {
        this.userDetail = new UserDetail();
        this.userRepository = new UserRepository();
        this.itemRepository = new ItemRepository();
    }

    public void performAdminTasks() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            AdminAction choice;
            do {
                displayMenu();

                int choiceValue;
                try {
                    choiceValue = Integer.parseInt(reader.readLine());
                    choice = AdminAction.fromValue(choiceValue);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    choice = null;
                }

                if (choice != null) {
                    switch (choice) {
                        case ADD_NEW_USER:
                            addNewUser();
                            break;
                        case GET_ITEM_LIST:
                            getAllItem();
                            break;
                        case ADD_MENU_ITEM:
                            addMenuItem();
                            break;
                        case UPDATE_MENU_ITEM:
                            updateMenuItem();
                            break;
                        case DELETE_MENU_ITEM:
                            deleteMenuItem();
                            break;
                        case EXIT:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
            } while (choice != AdminAction.EXIT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMenu() {
        System.out.println("Select operation from menu :");
        for (AdminAction choice : AdminAction.values()) {
            System.out.println(choice.getValue() + ". " + choice.getDescription());
        }
        System.out.print("Enter your choice: ");
    }

    private void addNewUser() {
        try {
            User newUser = userDetail.getUserDetail();
            userRepository.addUser(newUser);
        } catch (IOException | InvalidInputException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void getAllItem() {
        System.out.println("\nDisplaying all items in the table...");

        ItemRepository itemRepository = new ItemRepository();
        try {
            List<Item> items = itemRepository.getAllItems();
            if (items.isEmpty()) {
                System.out.println("No items found.");
            } else {
                // Print table header
                System.out.printf("%-10s %-20s %-10s %-20s %-15s %-30s%n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description");
                System.out.println("-------------------------------------------------------------------------------------------------------------");

                // Print each item in a formatted table row
                for (Item item : items) {
                    System.out.printf("%-10d %-20s %-10.2f %-20s %-15s %-30s%n",
                            item.getItemId(),
                            item.getItemName(),
                            item.getPrice(),
                            item.getAvailabilityStatus(),
                            item.getMealType(),
                            item.getDescription());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error fetching items from the database.");
        }
    }

    private void addMenuItem() {
        ItemDetail itemDetail = new ItemDetail();
        try {
            Item newItem = itemDetail.getItemDetails();
            itemRepository.addItem(newItem);

        } catch (InvalidInputException | IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMenuItem() {
        try {
            int itemId = getItemIdToUpdate();
            ItemRepository itemRepository = new ItemRepository();

            if (!itemRepository.checkItemPresent(itemId)) {
                throw new InvalidInputException("Item with ID " + itemId + " does not exist.");
            }

            Item updatedItem = getUpdatedItemDetails(itemId);

            boolean isUpdated = itemRepository.updateItem(updatedItem);

            if (isUpdated) {
                System.out.println("Item updated successfully.");
            } else {
                System.out.println("Item could not be updated.");
            }
        } catch (InvalidInputException | IOException | SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getItemIdToUpdate() throws IOException, InvalidInputException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int maxAttempts = 3;

        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter item ID to update: ");
            try {
                String itemIdStr = reader.readLine();
                int itemId = Integer.parseInt(itemIdStr);
                if (itemId > 0) {
                    return itemId;
                } else {
                    System.out.println("Item ID must be a positive number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        throw new InvalidInputException("Invalid item ID input. You have reached the maximum number of attempts.");
    }

    private Item getUpdatedItemDetails(int itemId) throws IOException, InvalidInputException {
        ItemDetail itemDetail = new ItemDetail();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Item item = new Item();
        item.setItemId(itemId);

        System.out.print("Enter new item name: ");
        item.setItemName(reader.readLine());

        item.setPrice(itemDetail.getValidPrice(reader));
        item.setAvailabilityStatus(itemDetail.getValidAvailabilityStatus(reader));
        item.setMealType(itemDetail.getValidMealType(reader));

        System.out.print("Enter new item description: ");
        item.setDescription(reader.readLine());

        return item;
    }

    private void deleteMenuItem() {
        try {
            int itemId = getItemIdToDelete();

            boolean isDeleted = itemRepository.deleteItem(itemId);

            if (isDeleted) {
                System.out.println("Item deleted successfully.");
            } else {
                System.out.println("Item not found or could not be deleted.");
            }
        } catch (InvalidInputException | IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getItemIdToDelete() throws IOException, InvalidInputException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int maxAttempts = 3;

        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter item ID to delete: ");
            try {
                String itemIdStr = reader.readLine();
                int itemId = Integer.parseInt(itemIdStr);
                if (itemId > 0) {
                    return itemId;
                } else {
                    System.out.println("Item ID must be a positive number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        throw new InvalidInputException("Invalid item ID input. You have reached the maximum number of attempts.");
    }
}
