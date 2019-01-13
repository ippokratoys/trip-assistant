package com.mantzavelas.tripassistant.restservices.dtos;

public class PopularPlaceDto {

    private Long id;

    private String latitude;

    private String longitude;

    private String name;

    private Integer timesMentioned;

    public PopularPlaceDto() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getTimesMentioned() { return timesMentioned; }
    public void setTimesMentioned(Integer timesMentioned) { this.timesMentioned = timesMentioned; }
}
