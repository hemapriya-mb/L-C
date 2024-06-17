package org.itt.utility;

import org.itt.dao.ItemRepository;
import org.itt.entity.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class ChefOperation {
    private ItemRepository itemRepository = new ItemRepository();

    public void performChefTasks() {
        try {
            generateNextDayItemList();
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateNextDayItemList() throws IOException, SQLException, ClassNotFoundException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<Item> items = itemRepository.getAllItemsWithRatings();

        System.out.println("Items with Ratings:");
        System.out.println("Item ID    Item Name            Rating");
        System.out.println("--------------------------------------------------");
        for (Item item : items) {
            System.out.printf("%-10d%-20s%-10.2f%n", item.getItemId(), item.getItemName(), item.getRating());
        }

        // Ask chef to select items for the next day
        System.out.println("Enter the item IDs to add for the next day, separated by commas:");
        String input = reader.readLine();
        String[] itemIdsStr = input.split(",");

        for (String itemIdStr : itemIdsStr) {
            int itemId = Integer.parseInt(itemIdStr.trim());
            itemRepository.addItemToNextDayList(itemId);
        }

        System.out.println("Items successfully added for the next day.");
    }
}
