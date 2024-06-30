package org.itt.service;

import org.itt.constant.EmployeeAction;
import org.itt.dao.*;
import org.itt.entity.*;
import org.itt.exception.DatabaseException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeService {
    private final ItemRepository itemRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final NotificationRepository notificationRepository;
    private final FeedbackRepository feedbackRepository;
    private final PollRepository pollRepository;

    public EmployeeService() {
        itemRepository = new ItemRepository();
        orderHistoryRepository = new OrderHistoryRepository();
        notificationRepository = new NotificationRepository();
        feedbackRepository = new FeedbackRepository();
        pollRepository = new PollRepository();
    }

    public String getEmployeeMenu() {
        StringBuilder menu = new StringBuilder("Select operation from menu:\n");
        for (EmployeeAction choice : EmployeeAction.values()) {
            menu.append(choice.ordinal() + 1).append(". ").append(choice.getDescription()).append("\n");
        }
        menu.append("Enter your choice: ");
        return menu.toString();
    }

    public String orderFood(int userId) {
        try {
            List<Item> items = itemRepository.getAllItems();
            if (items.isEmpty()) {
                return "No items available.";
            }

            StringBuilder response = new StringBuilder("Available Items:\n");
            response.append(String.format("%-10s %-20s %-10s %-15s %-15s %-20s%n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description"));
            response.append("-------------------------------------------------------------------------------------------------------\n");
            for (Item item : items) {
                response.append(String.format("%-10d %-20s %-10.2f %-15s %-15s %-20s%n",
                        item.getItemId(), item.getItemName(), item.getPrice(), item.getAvailabilityStatus(), item.getMealType(), item.getDescription()));
            }

            return response.toString();

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String placeOrder(int userId, int itemId) {
        try {
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setUserId(userId);
            orderHistory.setItemId(itemId);
            orderHistoryRepository.addOrder(orderHistory);

            return "Order placed successfully!";

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String viewOrderHistory(int userId) {
        try {
            List<OrderHistory> orderHistoryList = orderHistoryRepository.getOrderHistoryByUserId(userId);

            if (orderHistoryList.isEmpty()) {
                return "No order history found.";
            } else {
                StringBuilder response = new StringBuilder();
                response.append(String.format("%-10s %-10s %-20s %-20s%n", "Order ID", "Item ID", "Item Name", "Order Date"));
                response.append("-------------------------------------------------------------\n");
                for (OrderHistory orderHistory : orderHistoryList) {
                    response.append(String.format("%-10d %-10d %-20s %-20s%n",
                            orderHistory.getOrderId(),
                            orderHistory.getItemId(),
                            orderHistory.getItemName(),
                            orderHistory.getOrderDate().toString()));
                }
                return response.toString();
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String viewNotifications(int userId) {
        try {
            List<Notification> notifications = notificationRepository.getNotificationsByUserId(userId);
            StringBuilder response = new StringBuilder("Notifications:\n");
            for (Notification notification : notifications) {
                response.append(notification.getMessage()).append("\n");
            }
            notificationRepository.markNotificationsAsRead(userId);
            return response.toString();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    public String giveFeedback(int userId, int orderId, int itemId, int rating, String comment) {
        try {
            Feedback feedback = new Feedback(userId, orderId, itemId, rating, comment);
            feedbackRepository.addFeedback(feedback);
            return "Feedback submitted successfully.";
        } catch (DatabaseException e) {
            return "An error occurred while submitting feedback: " + e.getMessage();
        }
    }

    public String pollForNextDayItems(int userId, int itemId) {
        try {
            boolean hasPolled = pollRepository.hasPolledForItem(userId, itemId);
            if (hasPolled) {
                return "You have already polled for this item.";
            }

            Poll poll = new Poll(userId, itemId, Date.valueOf(LocalDate.now()));
            pollRepository.addPoll(poll);
            pollRepository.incrementPollCount(itemId);

            return "Poll count updated successfully for item ID: " + itemId;
        } catch (DatabaseException e) {
            return "An error occurred while updating poll count: " + e.getMessage();
        }
    }

    public String getNextDayItems() {
        try {
            List<Integer> itemIds = pollRepository.getNextDayItemIds();
            if (itemIds.isEmpty()) {
                return "No items available for next day.";
            }

            StringBuilder items = new StringBuilder("Items available for next day polling:\n");
            items.append(String.format("%-10s %-20s %-10s  %-15s %-20s%n", "Item ID", "Item Name", "Price", "Meal Type", "Description"));
            items.append("-------------------------------------------------------------------------------------------------------\n");

            for (int itemId : itemIds) {
                Item item = itemRepository.getItemById(itemId);
                items.append(String.format("%-10d %-20s %-10.2f  %-15s %-20s%n",
                        item.getItemId(), item.getItemName(), item.getPrice(), item.getMealType(), item.getDescription()));
            }

            return items.toString();
        } catch (DatabaseException e) {
            return "An error occurred while retrieving next day items: " + e.getMessage();
        }
    }

    public String getRecommendations() {
        SentimentAnalysisService sentimentAnalysisService = new SentimentAnalysisService();
        try {
            Map<Integer, List<Feedback>> feedbackByItem = getFeedbackByItem();
            Map<Integer, Double> averageRatings = calculateAverageRatings(feedbackByItem);
            List<Integer> topItems = getTopItems(averageRatings);
            Map<Integer, String> sentimentAnalysis = sentimentAnalysisService.performSentimentAnalysis(feedbackByItem);

            return buildRecommendations(topItems, averageRatings, sentimentAnalysis);
        } catch (DatabaseException e) {
            return "An error occurred while fetching recommendations: " + e.getMessage();
        }
    }

    private Map<Integer, List<Feedback>> getFeedbackByItem() throws DatabaseException {
        List<Feedback> feedbacks = feedbackRepository.getAllFeedback();
        Map<Integer, List<Feedback>> feedbackByItem = new HashMap<>();

        for (Feedback feedback : feedbacks) {
            int itemId = feedback.getItemId();
            if (!feedbackByItem.containsKey(itemId)) {
                feedbackByItem.put(itemId, new ArrayList<>());
            }
            feedbackByItem.get(itemId).add(feedback);
        }

        return feedbackByItem;
    }

    private Map<Integer, Double> calculateAverageRatings(Map<Integer, List<Feedback>> feedbackByItem) {
        Map<Integer, Double> averageRatings = new HashMap<>();

        for (Map.Entry<Integer, List<Feedback>> entry : feedbackByItem.entrySet()) {
            int itemId = entry.getKey();
            List<Feedback> feedbackList = entry.getValue();

            int totalRating = 0;
            for (Feedback fb : feedbackList) {
                totalRating += fb.getRating();
            }
            double avgRating = (double) totalRating / feedbackList.size();
            averageRatings.put(itemId, avgRating);
        }

        return averageRatings;
    }

    private List<Integer> getTopItems(Map<Integer, Double> averageRatings) {
        List<Map.Entry<Integer, Double>> sortedEntries = new ArrayList<>(averageRatings.entrySet());
        Collections.sort(sortedEntries, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> e1, Map.Entry<Integer, Double> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        List<Integer> topItems = new ArrayList<>();
        for (int i = 0; i < Math.min(5, sortedEntries.size()); i++) {
            topItems.add(sortedEntries.get(i).getKey());
        }

        return topItems;
    }

    private String buildRecommendations(List<Integer> topItems, Map<Integer, Double> averageRatings, Map<Integer, String> sentimentAnalysis) throws DatabaseException {
        StringBuilder recommendations = new StringBuilder("Top 5 Recommended Items:\n");
        recommendations.append(String.format("%-10s %-20s %-10s %-15s %-15s %-40s %-10s %n", "Item ID", "Item Name", "Price", "Availability", "Meal Type", "Description", "Avg Rating"));
        recommendations.append("-----------------------------------------------------------------------------------------------------------------------------------\n");

        for (int itemId : topItems) {
            if (!"Negative".equals(sentimentAnalysis.get(itemId))) {
                Item item = itemRepository.getItemById(itemId);
                double avgRating = averageRatings.get(itemId);
                String sentiment = sentimentAnalysis.get(itemId);
                recommendations.append(String.format("%-10d %-20s %-10.2f %-15s %-15s %-40s %-10.2f %n",
                        item.getItemId(), item.getItemName(), item.getPrice(), item.getAvailabilityStatus(), item.getMealType(), item.getDescription(), avgRating, sentiment));
            }
        }

        return recommendations.toString();
    }
}
