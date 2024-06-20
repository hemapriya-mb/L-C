package org.itt.constant;

public enum ChefAction {
    VIEW_TOP_ITEMS(1),
    SELECT_ITEMS_FOR_NEXT_DAY(2),
    EXIT(3);

    private final int value;

    ChefAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChefAction fromValue(int value) {
        for (ChefAction action : values()) {
            if (action.getValue() == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid action value: " + value);
    }
}
