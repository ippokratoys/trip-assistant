package com.mantzavelas.tripassistantapi.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Trip {

	public enum Status {
		FUTURE,
		UPCOMING,
		ARCHIVED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String description;

	@Enumerated(value = EnumType.STRING)
	private Status status;

	@ManyToMany
	@JoinTable(
			name = "trip_places",
			joinColumns = @JoinColumn(name = "trip_id"),
			inverseJoinColumns = @JoinColumn(name = "places_id")
	)
	private List<Place> places;

	private Date scheduledFor;

	@ManyToOne
	@JoinColumn(name = "users_id")
	private User user;

	public Long getId() { return id; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }

	public List<Place> getPlaces() { return places; }
	public void setPlaces(List<Place> places) { this.places = places; }

	public Date getScheduledFor() { return scheduledFor; }
	public void setScheduledFor(Date scheduledFor) { this.scheduledFor = scheduledFor; }

	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
}
