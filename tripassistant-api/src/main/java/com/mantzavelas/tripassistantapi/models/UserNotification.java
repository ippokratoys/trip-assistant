package com.mantzavelas.tripassistantapi.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class UserNotification implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String details;

	@Lob
	private byte[] notificationImage;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "trip_id")
	private Trip trip;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private User user;

	@Basic
	@Temporal(TemporalType.DATE)
	private Date dateSent;

	private boolean acked;

	public Long getId() { return id; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDetails() { return details; }
	public void setDetails(String details) { this.details = details; }

	public byte[] getNotificationImage() { return notificationImage; }
	public void setNotificationImage(byte[] notificationImage) { this.notificationImage = notificationImage; }

	public Trip getTrip() { return trip; }
	public void setTrip(Trip trip) { this.trip = trip; }

	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }

	public Date getDateSent() { return dateSent; }
	public void setDateSent(Date dateSent) { this.dateSent = dateSent; }

	public boolean isAcked() { return acked; }
	public void setAcked(boolean acked) { this.acked = acked; }
}
