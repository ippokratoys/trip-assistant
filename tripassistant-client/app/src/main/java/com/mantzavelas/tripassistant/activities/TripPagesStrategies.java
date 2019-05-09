package com.mantzavelas.tripassistant.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripPagesStrategies {

    private static List<TripPageStrategy> pageStrategies = new ArrayList<>();

    static {
        pageStrategies.addAll(Arrays.asList(
            new TripPageStrategy(new AllTripsFragment()),
            new TripPageStrategy(new UpComingTripsFragment())
        ));
    }

    public TripPageStrategy getTripPageStrategy(String pageName) {
        for (TripPageStrategy strategy : pageStrategies) {
            if (strategy.matchesStrategy(pageName)) {
                return strategy;
            }
        }

        return null;
    }
}
