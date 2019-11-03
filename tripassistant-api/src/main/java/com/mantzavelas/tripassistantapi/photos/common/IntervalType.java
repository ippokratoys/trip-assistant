package com.mantzavelas.tripassistantapi.photos.common;

public enum IntervalType {

    MINUTELY(60),
    HOURLY(MINUTELY.value*60),
    DAILY(HOURLY.value*24),
    WEEKLY(DAILY.value*7),
    MONTHLY(WEEKLY.value*30),
    UNLIMITED(0);

    IntervalType(int value) { this.value = value; }

    int value;
    public int getValue() { return value; }
}
