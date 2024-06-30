package org.itt.entity;

import java.sql.Date;

public class Poll {
    private int pollId;
    private int userId;
    private int itemId;
    private Date pollDate;

    public Poll() {}

    public Poll(int userId, int itemId, Date pollDate) {
        this.userId = userId;
        this.itemId = itemId;
        this.pollDate = pollDate;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
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

    public Date getPollDate() {
        return pollDate;
    }

    public void setPollDate(Date pollDate) {
        this.pollDate = pollDate;
    }
}
