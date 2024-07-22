package org.itt.constant;

public enum EmployeeAction {
    ORDER_FOOD("Order Food"),
    VIEW_ORDER_HISTORY("View Order History"),
    VIEW_NOTIFICATIONS("View Notifications"),
    GIVE_FEEDBACK("Give Feedback"),
    POLL_FOR_NEXT_DAY_ITEMS("Poll for Next Day Items"),
    GET_RECOMMENDATIONS("Get Recommendations"),
    GIVE_DETAILED_FEEDBACK("Give detailed feedback"),
    UPDATE_PROFILE("update profile"),
    GET_RECOMMENDATION_BY_PROFILE("get recommendation by profile"),
    EXIT("Exit");

    private final String description;

    EmployeeAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static EmployeeAction fromValue(int value) {
        for (EmployeeAction action : values()) {
            if (action.ordinal() + 1 == value) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
