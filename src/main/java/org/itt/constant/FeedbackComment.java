package org.itt.constant;

public enum FeedbackComment {
    VERY_GOOD("Very Good"),
    GOOD("Good"),
    AVERAGE("Average"),
    POOR("Poor"),
    VERY_POOR("Very Poor");

    private final String comment;

    FeedbackComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public static FeedbackComment fromValue(int value) {
        switch (value) {
            case 1: return VERY_GOOD;
            case 2: return GOOD;
            case 3: return AVERAGE;
            case 4: return POOR;
            case 5: return VERY_POOR;
            default: throw new IllegalArgumentException("Invalid feedback comment value");
        }
    }
}
