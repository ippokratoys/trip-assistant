package com.mantzavelas.tripassistant.activities;

public enum TripPages {

    FUTURE_TRIPS("Future Trips"),
    UPCOMING_TRIPS("Upcoming Trips"),
    COMPLETED_TRIPS("Completed Trips");

    TripPages(String name) { this.name = name; }

    private String name;
    public String getName() { return name; }
}
