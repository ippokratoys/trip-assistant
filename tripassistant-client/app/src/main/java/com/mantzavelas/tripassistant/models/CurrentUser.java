package com.mantzavelas.tripassistant.models;

public class CurrentUser {

    private static CurrentUser currentUser;
    private static User user;

    public static CurrentUser getInstance() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
            user = new User();
        }

        return currentUser;
    }

    public User getUser() {
        return user;
    }

    public void setLoggedInUser(String username, String token) {
        user.setUsername(username);
        user.setAccessToken(token);
    }

    public void setRegisteredUser(String firstName, String lastName, String userName, String token) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(userName);
        user.setAccessToken(token);
    }

    public boolean isLoggedIn() {
        return user.getAccessToken() != null && user.getAccessToken().length()>0;
    }

    public void setUserLocation(String latitude, String longitude) {
        user.setLatitude(latitude);
        user.setLongitude(longitude);
    }

    public String getLatitude() {
        return user.getLatitude();
    }

    public String getLongitude() {
        return user.getLongitude();
    }

    public String retrieveAccessTokenWithBearer() {
        if (isLoggedIn()) {
            return "Bearer " + user.getAccessToken();
        }

        return "";
    }
}
