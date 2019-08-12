package com.mantzavelas.tripassistantapi.models;

import com.mantzavelas.tripassistantapi.exceptions.NoSuchCategoryException;

import java.util.Arrays;

public enum PhotoCategoryEnum {
    FOOD,
    ENTERTAINMENT,
    COFFEE,
    SHOPPING,
    SIGHTSEEING,
    NIGHTLIFE,
    ACTIVITIES,
    OTHER;

    public static PhotoCategoryEnum resolveCategory(String category) {
        return Arrays.stream(PhotoCategoryEnum.values())
                	 .filter(categoryEnum -> categoryEnum.name().equalsIgnoreCase(category))
                	 .findFirst()
                	 .orElseThrow(NoSuchCategoryException::new);
    }
}
