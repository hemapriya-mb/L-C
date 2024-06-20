package org.itt.constant;

public enum EmployeeAction {
    ORDER_FOOD(1),
    GIVE_FEEDBACK(2),
    VIEW_ORDER_HISTORY(3),
    POLL_FOR_NEXT_DAY_ITEMS(4),
    VIEW_NOTIFICATIONS(5),
    VIEW_RECOMMENDATIONS(6),
    EXIT(7);

    private final int value;

    EmployeeAction(int value) {
        this.value = value;
    }

    public static EmployeeAction fromValue(int value) {
        for (EmployeeAction action : values()) {
            if (action.value == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
