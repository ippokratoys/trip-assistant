package com.mantzavelas.tripassistantapi.dtos;

import com.mantzavelas.tripassistantapi.models.Trip;

import java.util.Date;
import java.util.List;

public class TripDto {

	private Long id;

	private String title;

	private String description;

	private Trip.Status status;

	private List<PlaceDto> places;

	private Date scheduledFor;

	public TripDto() {
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public Trip.Status getStatus() { return status; }
	public void setStatus(Trip.Status status) { this.status = status; }

	public List<PlaceDto> getPlaces() { return places; }
	public void setPlaces(List<PlaceDto> places) { this.places = places; }

	public Date getScheduledFor() { return scheduledFor; }
	public void setScheduledFor(Date scheduledFor) { this.scheduledFor = scheduledFor; }
}
