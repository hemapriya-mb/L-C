package org.itt.entity;

public class Feedback {
    private int feedbackId;
    private int userId;
    private int orderId;
    private int itemId;
    private int rating;
    private String comment;

    public Feedback() {}

    public Feedback(int userId, int orderId, int itemId, int rating, String comment) {
        this.userId = userId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
