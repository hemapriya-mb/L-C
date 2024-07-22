package org.itt.entity;

import java.sql.Timestamp;

public class DetailedFeedback {
    private int userId;
    private int itemId;
    private String answer1;
    private String answer2;
    private String answer3;

    public DetailedFeedback() {
    }

    public DetailedFeedback( int userId, int itemId, String answer1, String answer2, String answer3) {
        this.userId = userId;
        this.itemId = itemId;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }
}
