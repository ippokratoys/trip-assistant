package com.mantzavelas.tripassistantapi.resources;

import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.models.Trip;

import java.util.Date;
import java.util.List;

public class TripResource {

	private String title;

	private String description;

	private Trip.Status status;

	private List<PlaceDto> places;

	private Date scheduledFor;

	public TripResource() {
	}

	public TripResource(String title, String description, Trip.Status status, List<PlaceDto> places, Date scheduledFor) {
		this.title = title;
		this.description = description;
		this.status = status;
		this.places = places;
		this.scheduledFor = scheduledFor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Trip.Status getStatus() {
		return status;
	}

	public void setStatus(Trip.Status status) {
		this.status = status;
	}

	public List<PlaceDto> getPlaces() {
		return places;
	}

	public void setPlaces(List<PlaceDto> places) {
		this.places = places;
	}

	public Date getScheduledFor() {
		return scheduledFor;
	}

	public void setScheduledFor(Date scheduledFor) {
		this.scheduledFor = scheduledFor;
	}
}
