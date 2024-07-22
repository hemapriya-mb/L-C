package org.itt.service;

import org.itt.constant.ChefAction;
import org.itt.dao.*;
import org.itt.entity.Feedback;
import org.itt.entity.Item;
import org.itt.entity.User;
import org.itt.exception.DatabaseException;

import java.sql.SQLException;
import java.util.*;

public class ChefService {
    private final ItemRepository itemRepository;
    private final FeedbackRepository feedbackRepository;
    private final SentimentAnalysisService sentimentAnalysisService;
    private final NextDayItemRepository nextDayItemRepository;

    public ChefService() {
        this.itemRepository = new ItemRepository();
        this.feedbackRepository = new FeedbackRepository();
        this.sentimentAnalysisService = new SentimentAnalysisService();
        this.nextDayItemRepository = new NextDayItemRepository();
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
            result.append("============================================================================================================================================================\n");
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
        UserRepository userRepository = new UserRepository();
        NotificationRepository notificationRepository=new NotificationRepository();
        StringBuilder messageBuilder = new StringBuilder("An item has been added for next day menu polling with id: ");
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

    public String viewDiscardMenu() {
        try {
            List<Feedback> feedbacks = feedbackRepository.getAllFeedback();
            Map<Integer, List<Feedback>> feedbackByItem = new HashMap<>();
            for (Feedback feedback : feedbacks) {
                feedbackByItem.computeIfAbsent(feedback.getItemId(), k -> new ArrayList<>()).add(feedback);
            }

            Map<Integer, Double> averageRatings = new HashMap<>();
            for (Map.Entry<Integer, List<Feedback>> entry : feedbackByItem.entrySet()) {
                int itemId = entry.getKey();
                List<Feedback> feedbackList = entry.getValue();
                double avgRating = feedbackList.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
                averageRatings.put(itemId, avgRating);
            }

            Map<Integer, String> sentimentAnalysis = sentimentAnalysisService.performSentimentAnalysis(feedbackByItem);

            StringBuilder discardMenu = new StringBuilder("Discard Menu Item List:\n");
            discardMenu.append(String.format("%-10s %-20s %-20s %-50s%n", "Item ID", "Item Name", "Avg Rating", "Sentiments"));
            discardMenu.append("---------------------------------------------------------------------------------------------------------\n");

            for (Map.Entry<Integer, String> entry : sentimentAnalysis.entrySet()) {
                int itemId = entry.getKey();
                String sentiment = entry.getValue();
                if ("Negative".equals(sentiment)) {
                    Item item = itemRepository.getItemById(itemId);
                    double avgRating = averageRatings.get(itemId);
                    discardMenu.append(String.format("%-10d %-20s %-20.2f %-50s%n",
                            item.getItemId(), item.getItemName(), avgRating, sentiment));
                }
            }

            return discardMenu.toString();
        } catch (DatabaseException e) {
            return "An error occurred while fetching discard menu items: " + e.getMessage();
        }
    }


    public String removeFoodItem(int itemId) {
        try {
            boolean isDeleted = itemRepository.deleteItem(itemId);
            return isDeleted ? "Item deleted successfully." : "Item not found or could not be deleted.";
        } catch (DatabaseException e) {
            return e.getMessage();
        }
    }

    public String addItemForDetailedFeedback(int itemId) {
        try {
            itemRepository.markItemForDetailedFeedback(itemId);
            return "Item marked for detailed feedback successfully.";
        } catch (DatabaseException e) {
            return "An error occurred while marking item for detailed feedback: " + e.getMessage();
        }
    }
}