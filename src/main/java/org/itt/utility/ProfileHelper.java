package org.itt.utility;

public class ProfileHelper {
    public String getFoodType(int choice) {
        switch (choice) {
            case 1: return "Vegetarian";
            case 2: return "Non Vegetarian";
            case 3: return "Eggetarian";
            default: throw new IllegalArgumentException("Invalid food type choice");
        }
    }

    public String getSpiceLevel(int choice) {
        switch (choice) {
            case 1: return "High";
            case 2: return "Medium";
            case 3: return "Low";
            default: throw new IllegalArgumentException("Invalid spice level choice");
        }
    }

    public String getCuisinePreference(int choice) {
        switch (choice) {
            case 1: return "North Indian";
            case 2: return "South Indian";
            case 3: return "Other";
            default: throw new IllegalArgumentException("Invalid cuisine preference choice");
        }
    }

    public boolean getSweetToothPreference(int choice) {
        switch (choice) {
            case 1: return true;
            case 2: return false;
            default: throw new IllegalArgumentException("Invalid sweet tooth preference choice");
        }
    }
}
