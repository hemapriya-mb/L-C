package org.itt.entity;

public class Feedback {
    private int userId;
    private int itemId;
    private int rating;
    private String comment;

    public Feedback(int userId, int itemId, int rating, String comment) {
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
