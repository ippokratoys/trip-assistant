package com.mantzavelas.tripassistantapi.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Entity(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @OneToMany(mappedBy = "user",
		orphanRemoval = true)
    private List<Trip> trips;

    @Column(columnDefinition="text")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> deviceTokens;

    public User() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

	public List<Trip> getTrips() { return trips; }
	public void setTrips(List<Trip> trips) { this.trips = trips; }

	public List<String> getDeviceTokens() { return deviceTokens; }
    public void setDeviceTokens(List<String> deviceTokens) { this.deviceTokens = deviceTokens; }
}
