package com.mantzavelas.tripassistant.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.mantzavelas.tripassistant.models.CurrentUser;

public class LocationListenerImpl implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        String lat = Double.toString(location.getLatitude());
        String lon = Double.toString(location.getLongitude());

        CurrentUser.getInstance().setUserLocation(lat, lon);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
