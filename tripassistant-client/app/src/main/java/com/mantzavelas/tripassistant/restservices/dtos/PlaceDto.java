package com.mantzavelas.tripassistant.restservices.dtos;

import java.util.List;

public class PlaceDto {

    private String title;

    private String description;

    private String latitude;

    private String longitude;

    private int visits;

    private List<String> photoUrls;

    public PlaceDto() {
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public int getVisits() { return visits; }
    public void setVisits(int visits) { this.visits = visits; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }
}
