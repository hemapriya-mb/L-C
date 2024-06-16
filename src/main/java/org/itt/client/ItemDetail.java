package org.itt.client;

import org.itt.entity.Item;
import org.itt.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ItemDetail {
    public Item getItemDetails() throws IOException, InvalidInputException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Item item = new Item();

        System.out.print("Enter item name: ");
        item.setItemName(reader.readLine().trim());

        item.setPrice(getValidPrice(reader));

        item.setAvailabilityStatus(getValidAvailabilityStatus(reader));

        item.setMealType(getValidMealType(reader));

        System.out.print("Enter item description: ");
        item.setDescription(reader.readLine().trim());

        return item;
    }

    public double getValidPrice(BufferedReader reader) throws IOException, InvalidInputException {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter item price: ");
            try {
                String priceStr = reader.readLine().trim();
                double price = Double.parseDouble(priceStr);
                if (price >= 0) {
                    return price;
                } else {
                    System.out.println("Price must be a positive number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        throw new InvalidInputException("Invalid price input. You have reached the maximum number of attempts.");
    }

    public String getValidAvailabilityStatus(BufferedReader reader) throws IOException, InvalidInputException {
        String[] validStatuses = {"Available", "Unavailable"};
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter availability status (Available/Unavailable): ");
            String status = reader.readLine().trim();
            for (String validStatus : validStatuses) {
                if (validStatus.equalsIgnoreCase(status)) {
                    return validStatus;
                }
            }
            System.out.println("Invalid input. Please enter one of the valid statuses.");
        }
        throw new InvalidInputException("Invalid availability status input. You have reached the maximum number of attempts.");
    }

    public String getValidMealType(BufferedReader reader) throws IOException, InvalidInputException {
        String[] validMealTypes = {"Breakfast", "Lunch", "Dinner"};
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter meal type (Breakfast/Lunch/Dinner): ");
            String mealType = reader.readLine().trim();
            for (String validType : validMealTypes) {
                if (validType.equalsIgnoreCase(mealType)) {
                    return mealType;
                }
            }
            System.out.println("Invalid input. Please enter one of the valid meal types.");
        }
        throw new InvalidInputException("Invalid meal type input. You have reached the maximum number of attempts.");
    }

}
