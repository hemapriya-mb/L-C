package org.itt.service;

import org.itt.constant.AdminAction;
import org.itt.dao.ItemRepository;
import org.itt.dao.UserRepository;
import org.itt.entity.Item;
import org.itt.entity.User;
import org.itt.exception.DatabaseException;
import org.itt.exception.InvalidInputException;

import java.util.List;

public class AdminService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public AdminService() {
        this.userRepository = new UserRepository();
        this.itemRepository = new ItemRepository();
    }

    public String getAdminMenu() {
        StringBuilder menu = new StringBuilder("Select operation from menu:\n");
        for (AdminAction choice : AdminAction.values()) {
            menu.append(choice.getValue()).append(". ").append(choice.getDescription()).append("\n");
        }
        return menu.toString();
    }

    public String executeAdminTask(int choiceValue) {
        try {
            AdminAction choice = AdminAction.fromValue(choiceValue);

            switch (choice) {
                case GET_ITEM_LIST:
                    return getAllItem();
                case EXIT:
                    return "Exiting...";
                default:
                    return "Invalid choice.";
            }
        } catch (IllegalArgumentException | InvalidInputException e) {
            return e.getMessage();
        }
    }

    public String addNewUser(String name, String role, String password) {
        try {
            if (password.length() < 8) {
                throw new InvalidInputException("Password must be at least 8 characters long.");
            }

            User newUser = new User(0, name, role, password);
            userRepository.addUser(newUser);
            return "User added successfully.";
        } catch (InvalidInputException | DatabaseException e) {
            return e.getMessage();
        }
    }

    public String addMenuItem(Item newItem) {
        try {
            itemRepository.addItem(newItem);
            return "Item added successfully.";
        } catch (DatabaseException e) {
            return e.getMessage();
        }
    }

    public String updateMenuItem(Item updatedItem) {
        try {
            if (!itemRepository.checkItemPresent(updatedItem.getItemId())) {
                return "Item with ID " + updatedItem.getItemId() + " does not exist.";
            }

            boolean isUpdated = itemRepository.updateItem(updatedItem);

            return isUpdated ? "Item updated successfully." : "Item could not be updated.";
        } catch (DatabaseException e) {
            return e.getMessage();
        }
    }


    public String deleteMenuItem(int itemId) {
        try {
            boolean isDeleted = itemRepository.deleteItem(itemId);
            return isDeleted ? "Item deleted successfully." : "Item not found or could not be deleted.";
        } catch (DatabaseException e) {
            return e.getMessage();
        }
    }

    private String getAllItem() {
        StringBuilder result = new StringBuilder("\nDisplaying all items in the table...\n");

        try {
            List<Item> items = itemRepository.getAllItems();
            if (items.isEmpty()) {
                return "No items found.";
            } else {
                result.append(String.format("%-10s %-20s %-10s %-20s %-15s %-30s%n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description"));
                result.append("-------------------------------------------------------------------------------------------------------------\n");

                for (Item item : items) {
                    result.append(String.format("%-10d %-20s %-10.2f %-20s %-15s %-30s%n",
                            item.getItemId(),
                            item.getItemName(),
                            item.getPrice(),
                            item.getAvailabilityStatus(),
                            item.getMealType(),
                            item.getDescription()));
                }
                return result.toString();
            }
        } catch (DatabaseException e) {
            return e.getMessage();
        }
    }
}
