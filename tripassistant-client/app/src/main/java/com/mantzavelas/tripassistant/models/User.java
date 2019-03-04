package com.mantzavelas.tripassistant.models;

public class User {

    private String firstName;

    private String lastName;

    private String username;

    private String accessToken;

    private String latitude;

    private String longitude;

    public User() {
    }

    public User(String firstName, String lastName, String username, String accessToken) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.accessToken = accessToken;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
}
