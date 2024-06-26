package org.itt.service;

import org.itt.dao.FeedbackRepository;
import org.itt.entity.Feedback;
import org.itt.exception.DatabaseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationService {
    private final FeedbackRepository feedbackRepository;

    public RecommendationService() {
        feedbackRepository = new FeedbackRepository();
    }

    public String generateRecommendations() {
        StringBuilder recommendations = new StringBuilder();
        try {
            List<Feedback> allFeedback = feedbackRepository.getAllFeedback();
            Map<Integer, ItemFeedbackSummary> itemFeedbackMap = new HashMap<>();

            for (Feedback feedback : allFeedback) {
                int itemId = feedback.getItemId();
                ItemFeedbackSummary summary = itemFeedbackMap.getOrDefault(itemId, new ItemFeedbackSummary(itemId));

                summary.addRating(feedback.getRating());
                summary.addComment(feedback.getComment());

                itemFeedbackMap.put(itemId, summary);
            }

            for (ItemFeedbackSummary summary : itemFeedbackMap.values()) {
                recommendations.append("Item ID: ").append(summary.getItemId()).append("\n")
                        .append("Average Rating: ").append(summary.getAverageRating()).append("\n")
                        .append("Feedback Summary: ").append(summary.getCommentSummary()).append("\n")
                        .append("-----------------------------------------\n");
            }

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        return recommendations.toString();
    }

    private class ItemFeedbackSummary {
        private int itemId;
        private int totalRating;
        private int ratingCount;
        private Map<String, Integer> commentCountMap;

        public ItemFeedbackSummary(int itemId) {
            this.itemId = itemId;
            this.totalRating = 0;
            this.ratingCount = 0;
            this.commentCountMap = new HashMap<>();
        }

        public void addRating(int rating) {
            totalRating += rating;
            ratingCount++;
        }

        public void addComment(String comment) {
            String[] comments = comment.split(",");
            for (String comm : comments) {
                comm = comm.trim();
                commentCountMap.put(comm, commentCountMap.getOrDefault(comm, 0) + 1);
            }
        }

        public double getAverageRating() {
            return (double) totalRating / ratingCount;
        }

        public int getItemId() {
            return itemId;
        }

        public String getCommentSummary() {
            StringBuilder summary = new StringBuilder();
            for (Map.Entry<String, Integer> entry : commentCountMap.entrySet()) {
                summary.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
            return summary.toString();
        }
    }
}
