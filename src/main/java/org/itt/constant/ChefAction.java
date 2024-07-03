package org.itt.constant;

public enum ChefAction {
    VIEW_HIGH_RATED_ITEMS("View High Rated Items"),
    SELECT_ITEMS_FOR_NEXT_DAY("Select Items for Next Day"),
    VIEW_DISCARD_MENU("View Discard Menu"),
    REMOVE_FOOD_ITEM("Remove Food Item"),
    GET_DETAILED_FEEDBACK("Get Detailed Feedback"),
    EXIT("Exit");

    private final String description;

    ChefAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ChefAction fromValue(int value) {
        for (ChefAction action : values()) {
            if (action.ordinal() + 1 == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid action value: " + value);
    }
}
