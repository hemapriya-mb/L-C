package org.itt.service;

import org.itt.constant.ChefAction;
import org.itt.dao.ItemRepository;
import org.itt.dao.NextDayItemRepository;
import org.itt.dao.NotificationRepository;
import org.itt.dao.UserRepository;
import org.itt.entity.Item;
import org.itt.exception.DatabaseException;

import java.sql.SQLException;
import java.util.List;

public class ChefService {
    private final ItemRepository itemRepository;
    private final NextDayItemRepository nextDayItemRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public ChefService() {
        this.itemRepository = new ItemRepository();
        this.nextDayItemRepository = new NextDayItemRepository();
        this.notificationRepository = new NotificationRepository();
        this.userRepository = new UserRepository();
    }

    public String getChefMenu() {
        StringBuilder menu = new StringBuilder("Select operation from menu:\n");
        for (ChefAction choice : ChefAction.values()) {
            menu.append(choice.ordinal() + 1).append(". ").append(choice.getDescription()).append("\n");
        }
        menu.append("Enter your choice: ");
        return menu.toString();
    }

    public String viewHighRatedItems() {
        try {
            List<Item> items = itemRepository.getTopRatedItems();
            StringBuilder result = new StringBuilder();
            result.append(String.format("%-10s %-20s %-10s %-20s %-15s %-50s %-15s%n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description", "Average Rating"));
            result.append("=========================================================================================================================\n");
            for (Item item : items) {
                result.append(String.format("%-10d %-20s %-10.2f %-20s %-15s %-50s %-15.2f%n",
                        item.getItemId(),
                        item.getItemName(),
                        item.getPrice(),
                        item.getAvailabilityStatus(),
                        item.getMealType(),
                        item.getDescription(),
                        item.getAverageRating()));
            }
            return result.toString();
        } catch (DatabaseException e) {
            return "An error occurred while retrieving high-rated items: " + e.getMessage();
        }
    }

    public String selectItemsForNextDay(String input) {
        try {
            String[] itemIdsStr = input.split(",");
            int[] itemIds = new int[itemIdsStr.length];

            for (int i = 0; i < itemIdsStr.length; i++) {
                itemIds[i] = Integer.parseInt(itemIdsStr[i].trim());
            }

            nextDayItemRepository.addNextDayItems(itemIds);
            notifyEmployees(itemIds);
            return "Selected items have been added to the next day item list.";
        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
            return "Invalid input. Please enter a valid list of item IDs.";
        } catch (DatabaseException e) {
            return "An error occurred while adding items to the next day item list: " + e.getMessage();
        }
    }

    private void notifyEmployees(int[] itemIds) throws SQLException, ClassNotFoundException, DatabaseException {
        StringBuilder messageBuilder = new StringBuilder("An item as been added for next day menu polling with id: ");
        for (int i = 0; i < itemIds.length; i++) {
            messageBuilder.append(itemIds[i]);
            if (i < itemIds.length - 1) {
                messageBuilder.append(", ");
            }
        }
        String message = messageBuilder.toString();
        List<Integer> employeeUserIds = userRepository.getAllEmployeeUserIds();
        for (int userId : employeeUserIds) {
            notificationRepository.addNotification(userId, message);
        }
    }
}
