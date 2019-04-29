package com.mantzavelas.tripassistant.restservices.resources;

import com.mantzavelas.tripassistant.models.Place;
import com.mantzavelas.tripassistant.models.enums.TripStatus;

import java.util.Date;
import java.util.List;

public class TripResource {

    private String title;

    private String description;

    private TripStatus status;

    private List<Place> places;

    private Date scheduledFor;

    public TripResource() {
    }

    public TripResource(String title, String description, TripStatus status, List<Place> places, Date scheduledFor) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.places = places;
        this.scheduledFor = scheduledFor;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public List<Place> getPlaces() { return places; }
    public void setPlaces(List<Place> places) { this.places = places; }

    public Date getScheduledFor() { return scheduledFor; }
    public void setScheduledFor(Date scheduledFor) { this.scheduledFor = scheduledFor; }

}
