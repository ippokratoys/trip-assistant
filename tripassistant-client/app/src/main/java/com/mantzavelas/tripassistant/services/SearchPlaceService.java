package com.mantzavelas.tripassistant.services;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mantzavelas.tripassistant.models.enums.CategoryEnum;
import com.mantzavelas.tripassistant.restservices.dtos.PlaceDto;

import java.util.ArrayList;
import java.util.List;

public class SearchPlaceService {

    private static final int MAX_DISTANCE_KM = 20000;

    //till 500 meters, increment by 100, after that by 0.5 km
    public List<String> getDistances() {
        List<String> distances = new ArrayList<>();

        int currentDistance = 100; //starting in meters
        while (currentDistance <= MAX_DISTANCE_KM) {
            if (currentDistance < 500) {
                distances.add(Integer.toString(currentDistance) + " m");
                currentDistance += 100;
                continue;
            }

            distances.add(Double.toString(currentDistance/1000.0) + " km");
            currentDistance += 500;
        }
        return distances;
    }

    public String[] getCategories() {
        String[] categories = new String[CategoryEnum.values().length];

        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            categories[categoryEnum.ordinal()] = categoryEnum.name();
        }
        return categories;
    }

    public String stripeOutUnitFromString(String meters) {

        meters = meters.replace("m","");
        meters = meters.replace("k","");
        meters = meters.trim();


        return String.valueOf((int)Double.parseDouble(meters));
    }

    public MarkerOptions createMarker(PlaceDto place) {
        LatLng latLng = new LatLng(Double.parseDouble(place.getLatitude()), Double.parseDouble(place.getLongitude()));
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title(place.getTitle());

        return marker;
    }
}
