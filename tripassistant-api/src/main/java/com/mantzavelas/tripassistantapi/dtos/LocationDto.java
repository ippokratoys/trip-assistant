package com.mantzavelas.tripassistantapi.dtos;

import java.io.Serializable;

public class LocationDto implements Serializable {

	private static final long serialVersionUID = -3802715817637219593L;

	private String latitude;

	private String longitude;

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
