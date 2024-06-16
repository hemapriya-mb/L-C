package org.itt.entity;

import java.io.Serializable;

public class Item implements Serializable {
    private int itemId;
    private String itemName;
    private double price;
    private String availabilityStatus;
    private String mealType;
    private String description;

    public Item() {
    }

    public Item(int itemId, String itemName, double price, String availabilityStatus, String mealType, String description) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.availabilityStatus = availabilityStatus;
        this.mealType = mealType;
        this.description = description;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", availabilityStatus='" + availabilityStatus + '\'' +
                ", mealType='" + mealType + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
