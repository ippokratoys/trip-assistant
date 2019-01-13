package com.mantzavelas.tripassistant.models;

public class CurrentUser {

    private static User user;

    public static void create(User newUser) {
        if (user == null) {
            user = newUser;
        }
    }

    public static User getUser() {
        return user;
    }

    public static boolean isLoggedIn() {
        if(user != null) {
            return user.getAccessToken() != null && user.getAccessToken().length()>0;
        }

        return false;
    }
}
