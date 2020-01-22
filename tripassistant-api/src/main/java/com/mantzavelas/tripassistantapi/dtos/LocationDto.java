package com.mantzavelas.tripassistantapi.dtos;

import java.io.Serializable;

public class LocationDto implements Serializable {

	String latitude;

	String longitude;

	public LocationDto() { /* Needed by Jackson deserializer */ }

	public LocationDto(String latitude, String longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getLatitude() { return latitude; }
	public void setLatitude(String latitude) { this.latitude = latitude; }

	public String getLongitude() { return longitude; }
	public void setLongitude(String longitude) { this.longitude = longitude; }
}
