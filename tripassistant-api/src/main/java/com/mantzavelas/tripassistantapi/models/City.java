package com.mantzavelas.tripassistantapi.models;

import com.mantzavelas.tripassistantapi.utils.LocationUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

public enum City {

    THESSALONIKI("40.635646", "22.944462");

    private String latitude;
    private String longitude;

    City(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() { return latitude; }

    public String getLongitude() { return longitude; }

	public static Optional<City> getCityFromLatLon(String lat, String lon) {
		return Arrays.stream(City.values())
				.filter(getCityDistancePredicate(lat, lon))
				.findFirst();
	}

	private static Predicate<City> getCityDistancePredicate(String lat, String lon) {
		return city -> LocationUtil.haversineDistanceInKm(lat, lon, city.getLatitude(), city.getLongitude()) < 100.0;
	}
}
