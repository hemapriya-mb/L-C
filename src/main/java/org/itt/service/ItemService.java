package org.itt.service;

import org.itt.entity.Item;
import org.itt.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ItemService {
    public Item getItemDetails() throws IOException, InvalidInputException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Item item = new Item();

        System.out.print("Enter item name: ");
        item.setItemName(bufferedReader.readLine().trim());

        item.setPrice(getValidPrice(bufferedReader));

        item.setAvailabilityStatus(getValidAvailabilityStatus(bufferedReader));

        item.setMealType(getValidMealType(bufferedReader));

        System.out.print("Enter item description: ");
        item.setDescription(bufferedReader.readLine().trim());

        return item;
    }

    public double getValidPrice(BufferedReader bufferedReader) throws IOException, InvalidInputException {
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter item price: ");
            try {
                String priceString = bufferedReader.readLine().trim();
                double price = Double.parseDouble(priceString);
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

    public String getValidAvailabilityStatus(BufferedReader bufferedReader) throws IOException, InvalidInputException {
        String[] validStatuses = {"Available", "Unavailable"};
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter availability status (Available/Unavailable): ");
            String status = bufferedReader.readLine().trim();
            for (String validStatus : validStatuses) {
                if (validStatus.equalsIgnoreCase(status)) {
                    return validStatus;
                }
            }
            System.out.println("Invalid input. Please enter one of the valid statuses.");
        }
        throw new InvalidInputException("Invalid availability status input. You have reached the maximum number of attempts.");
    }

    public String getValidMealType(BufferedReader bufferedReader) throws IOException, InvalidInputException {
        String[] validMealTypes = {"Breakfast", "Lunch", "Dinner"};
        int maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            System.out.print("Enter meal type (Breakfast/Lunch/Dinner): ");
            String mealType = bufferedReader.readLine().trim();
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
