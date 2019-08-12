package com.mantzavelas.tripassistant.models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class CurrentUser {

    private static CurrentUser currentUser;
    private static User user;
    private LocationManager locationManager;

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

    public void initLocationServices(Context context) {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setUserLocation(Double.toString(lastLocation.getLatitude()), Double.toString(lastLocation.getLongitude()));
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
