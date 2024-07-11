package org.itt.service;

import org.itt.entity.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ItemService {
    private final BufferedReader bufferedReader;

    public ItemService() {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public Item getItemDetails() throws IOException {
        Item item = new Item();

        System.out.print("Enter item name: ");
        item.setItemName(bufferedReader.readLine().trim());

        item.setPrice(getValidPrice());

        item.setAvailabilityStatus(getValidAvailabilityStatus());

        item.setMealType(getValidMealType());

        System.out.print("Enter item description: ");
        item.setDescription(bufferedReader.readLine().trim());

        return item;
    }

    public double getValidPrice() throws IOException {
        double price;
        while (true) {
            System.out.print("Enter item price: ");
            try {
                price = Double.parseDouble(bufferedReader.readLine().trim());
                if (price >= 0) {
                    return price;
                } else {
                    System.out.println("Price must be a positive number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public String getValidAvailabilityStatus() throws IOException {
        String[] validStatuses = {"Available", "Unavailable"};
        String status;
        while (true) {
            System.out.print("Enter availability status (Available/Unavailable): ");
            status = bufferedReader.readLine().trim();
            for (String validStatus : validStatuses) {
                if (validStatus.equalsIgnoreCase(status)) {
                    return validStatus;
                }
            }
            System.out.println("Invalid input. Please enter one of the valid statuses.");
        }
    }

    public String getValidMealType() throws IOException {
        String[] validMealTypes = {"Breakfast", "Lunch", "Dinner"};
        String mealType;
        while (true) {
            System.out.print("Enter meal type (Breakfast/Lunch/Dinner): ");
            mealType = bufferedReader.readLine().trim();
            for (String validType : validMealTypes) {
                if (validType.equalsIgnoreCase(mealType)) {
                    return mealType;
                }
            }
            System.out.println("Invalid input. Please enter one of the valid meal types.");
        }
    }
}
