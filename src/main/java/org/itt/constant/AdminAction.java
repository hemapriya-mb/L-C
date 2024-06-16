package org.itt.constant;

public enum AdminAction
{
    ADD_NEW_USER(1, "Add New User"),
    GET_ITEM_LIST(2,"get all items"),
    ADD_MENU_ITEM(3, "Add Menu Item"),
    UPDATE_MENU_ITEM(4, "Update Menu Item"),
    DELETE_MENU_ITEM(5, "Delete Menu Item"),
    EXIT(6, "Exit");

    private final int value;
    private final String description;

    AdminAction(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AdminAction fromValue(int value) {
        for (AdminAction choice : AdminAction.values()) {
            if (choice.getValue() == value) {
                return choice;
            }
        }
        throw new IllegalArgumentException("Invalid choice: " + value);
    }
}
