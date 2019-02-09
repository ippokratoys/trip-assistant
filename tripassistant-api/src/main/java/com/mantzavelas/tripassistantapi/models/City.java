package com.mantzavelas.tripassistantapi.models;

public enum City {

    THESSALONIKI("40.635646", "22.944462");

    private String latitude;
    private String longitude;

    City(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
}
