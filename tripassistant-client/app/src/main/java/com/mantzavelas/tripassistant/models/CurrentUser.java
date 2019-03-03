package com.mantzavelas.tripassistant.models;

public class CurrentUser {

    private static User user = new User();

    public static User getUser() {
        return user;
    }

    public static void setLoggedInUser(String username, String token) {
        user.setUsername(username);
        user.setAccessToken(token);
    }

    public static void setRegisteredUser(String firstName, String lastName, String userName, String token) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(userName);
        user.setAccessToken(token);
    }

    public static boolean isLoggedIn() {
        return user.getAccessToken() != null && user.getAccessToken().length()>0;
    }

    public static void setUserLocation(String latitude, String longitude) {
        user.setLatitude(latitude);
        user.setLongitude(longitude);
    }

    public static String getLatitude() {
        return user.getLatitude();
    }

    public static String getLongitude() {
        return user.getLongitude();
    }

    public static String retrieveAccessTokenWithBearer() {
        if (isLoggedIn()) {
            return "Bearer " + user.getAccessToken();
        }

        return "";
    }
}
