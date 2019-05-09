package com.mantzavelas.tripassistant.activities;

public enum TripPages {

    ALL_TRIPS("All Trips"),
    UPCOMING_TRIPS("Upcoming Trips");

    TripPages(String name) { this.name = name; }

    private String name;
    public String getName() { return name; }
}
