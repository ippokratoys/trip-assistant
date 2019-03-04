package com.mantzavelas.tripassistantapi.photos.utils;

import java.util.List;

public class FacebookPlaceInfoResponse {

    private String id;

    private String name;

    private String about;

    private String description;

    private int checkins;

    private FacebookLocation location;

    private List<FacebookCategory> category_list;

    public FacebookPlaceInfoResponse() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCheckins() { return checkins; }
    public void setCheckins(int checkins) { this.checkins = checkins; }

    public FacebookLocation getLocation() { return location; }
    public void setLocation(FacebookLocation location) { this.location = location; }

    public List<FacebookCategory> getCategory_list() { return category_list; }
    public void setCategory_list(List<FacebookCategory> category_list) { this.category_list = category_list; }
}
