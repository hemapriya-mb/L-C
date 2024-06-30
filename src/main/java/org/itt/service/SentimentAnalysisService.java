package org.itt.service;

import org.itt.entity.Feedback;

import java.util.*;

public class SentimentAnalysisService {
    private final Set<String> positiveWords;
    private final Set<String> negativeWords;

    public SentimentAnalysisService() {
        positiveWords = new HashSet<>(Arrays.asList("good", "very good", "excellent", "amazing", "fantastic", "great"));
        negativeWords = new HashSet<>(Arrays.asList("poor", "very poor", "bad", "terrible", "awful", "disappointing"));
    }

    public Map<Integer, String> performSentimentAnalysis(Map<Integer, List<Feedback>> feedbackByItem) {
        Map<Integer, String> sentimentAnalysis = new HashMap<>();

        for (Map.Entry<Integer, List<Feedback>> entry : feedbackByItem.entrySet()) {
            int itemId = entry.getKey();
            List<Feedback> feedbacks = entry.getValue();

            long positiveCount = feedbacks.stream()
                    .filter(fb -> containsPositiveWords(fb.getComment().toLowerCase()))
                    .count();
            long negativeCount = feedbacks.stream()
                    .filter(fb -> containsNegativeWords(fb.getComment().toLowerCase()))
                    .count();
            long totalCount = feedbacks.size();

            String sentiment = "Neutral";
            if (positiveCount > totalCount / 2) {
                sentiment = "Positive";
            } else if (negativeCount > totalCount / 2) {
                sentiment = "Negative";
            }

            sentimentAnalysis.put(itemId, sentiment);
        }

        return sentimentAnalysis;
    }

    private boolean containsPositiveWords(String comment) {
        for (String word : positiveWords) {
            if (comment.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNegativeWords(String comment) {
        for (String word : negativeWords) {
            if (comment.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
