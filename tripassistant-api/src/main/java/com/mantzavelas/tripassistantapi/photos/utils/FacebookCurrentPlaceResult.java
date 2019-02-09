package com.mantzavelas.tripassistantapi.photos.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FacebookCurrentPlaceResult {

    private String id;

    private String name;

    private String description;

    private int checkins;

    private FacebookLocation location;

    @JsonProperty("category_list")
    private List<FacebookCategory> categories;

    @JsonProperty("confidence_level")
    private String confidenceLevel;

    public FacebookCurrentPlaceResult() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCheckins() { return checkins; }
    public void setCheckins(int checkins) { this.checkins = checkins; }

    public FacebookLocation getLocation() { return location; }
    public void setLocation(FacebookLocation location) { this.location = location; }

    public List<FacebookCategory> getCategories() { return categories; }
    public void setCategories(List<FacebookCategory> categories) { this.categories = categories; }

    public String getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(String confidenceLevel) { this.confidenceLevel = confidenceLevel; }
}
