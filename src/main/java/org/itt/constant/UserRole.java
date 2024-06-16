package org.itt.constant;

import org.itt.exception.InvalidInputException;

public enum UserRole {
    ADMIN("admin"), CHEF("chef"), EMPLOYEE("employee");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole fromValue(String value) throws InvalidInputException {
        for (UserRole role : UserRole.values()) {
            if (role.getValue().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new InvalidInputException("Invalid role selection");
    }
}
