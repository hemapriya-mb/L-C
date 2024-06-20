package org.itt.constant;

public enum SpecificFeedback {
    TOO_SWEET("Too Sweet"),
    SWEET("Sweet"),
    BLAND("Bland"),
    SPICY("Spicy"),
    TOO_SPICY("Too Spicy");

    private final String comment;

    SpecificFeedback(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public static SpecificFeedback fromValue(int value) {
        for (SpecificFeedback feedback : values()) {
            if (feedback.ordinal() + 1 == value) {
                return feedback;
            }
        }
        throw new IllegalArgumentException("Invalid SpecificFeedback value: " + value);
    }
}
