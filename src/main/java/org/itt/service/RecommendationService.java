package org.itt.service;

import org.itt.dao.FeedbackRepository;
import org.itt.entity.Feedback;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationService {

    private FeedbackRepository feedbackRepository;

    public RecommendationService() {
        feedbackRepository = new FeedbackRepository();
    }

    public void generateRecommendations() {
        try {
            List<Feedback> allFeedback = feedbackRepository.getAllFeedback();
            Map<Integer, ItemFeedbackSummary> itemFeedbackMap = new HashMap<Integer, ItemFeedbackSummary>();

            for (Feedback feedback : allFeedback) {
                int itemId = feedback.getItemId();
                ItemFeedbackSummary summary = itemFeedbackMap.getOrDefault(itemId, new ItemFeedbackSummary(itemId));

                summary.addRating(feedback.getRating());
                summary.addComment(feedback.getComment());

                itemFeedbackMap.put(itemId, summary);
            }

            for (ItemFeedbackSummary summary : itemFeedbackMap.values()) {
                System.out.println("Item ID: " + summary.getItemId());
                System.out.println("Average Rating: " + summary.getAverageRating());
                System.out.println("Feedback Summary: " + summary.getCommentSummary());
                System.out.println("-----------------------------------------");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
            this.commentCountMap = new HashMap<String, Integer>();
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
    }}