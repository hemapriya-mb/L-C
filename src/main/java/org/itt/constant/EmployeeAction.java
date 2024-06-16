package org.itt.constant;

public enum EmployeeAction {
    ORDER_FOOD(1),
    GIVE_FEEDBACK(2),
    VIEW_ORDER_HISTORY(3),
    EXIT(4);

    private final int value;

    EmployeeAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EmployeeAction fromValue(int value) {
        for (EmployeeAction action : values()) {
            if (action.getValue() == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid action value: " + value);
    }
}
