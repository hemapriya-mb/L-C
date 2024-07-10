package org.itt.service;

import org.itt.dao.ItemRepository;
import org.itt.dao.ProfileRepository;
import org.itt.entity.Profile;
import org.itt.entity.Item;
import org.itt.exception.DatabaseException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationService {
    private final ProfileRepository profileRepository;
    private final ItemRepository itemRepository;

    public RecommendationService() {
        this.profileRepository = new ProfileRepository();
        this.itemRepository = new ItemRepository();
    }

    public List<Item> recommendItems(int userId) throws DatabaseException {
        Profile profile = profileRepository.getProfileByUserId(userId);
        if (profile == null) {
            throw new DatabaseException("user profile not found");
        }

        List<Item> items = itemRepository.getAllItems();

        return items.stream()
                .filter(item -> item.getFoodType().equalsIgnoreCase(profile.getFoodType()))
                .filter(item -> item.getSpiceLevel().equalsIgnoreCase(profile.getSpiceLevel()))
                .filter(item -> item.getCuisineType().equalsIgnoreCase(profile.getCuisineType()))
                .filter(item -> item.getSweet() == profile.isSweet())
                .sorted(Comparator.comparingDouble(Item::getAverageRating).reversed())
                .collect(Collectors.toList());
    }
}
