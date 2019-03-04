package com.mantzavelas.tripassistantapi.utils;

public class LocationUtil {

    public static final double R = 6372.8; // In kilometers
    public static double haversineDistanceInKm(String lat1, String lon1, String lat2, String lon2) {
        double latStart = Double.parseDouble(lat1);
        double lonStart = Double.parseDouble(lon1);
        double latEnd = Double.parseDouble(lat2);
        double lonEnd = Double.parseDouble(lon2);

        double latDistance = Math.toRadians(latEnd - latStart);
        double lonDistance = Math.toRadians(lonEnd - lonStart);

        latStart = Math.toRadians(latStart);
        latEnd = Math.toRadians(latEnd);

        double a = Math.pow(Math.sin(latDistance / 2),2) + Math.pow(Math.sin(lonDistance / 2),2) * Math.cos(latStart) * Math.cos(latEnd);
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c;
    }

    public static double haversineDistanceInKm(double lat1, double lon1, double lat2, double lon2) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(latDistance / 2),2) + Math.pow(Math.sin(lonDistance / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return R * c;
    }

}
