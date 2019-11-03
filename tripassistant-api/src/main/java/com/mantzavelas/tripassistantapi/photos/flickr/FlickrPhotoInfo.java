package com.mantzavelas.tripassistantapi.photos.flickr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlickrPhotoInfo {

    private String id;

    private String secret;

    private String server;

    private int farm;

    //already in unix timestamp format
    @JsonProperty("dateuploaded")
    private String dateUploaded;

    private int rotation;

    @JsonProperty("originalsecret")
    private String originalSecret;

    @JsonProperty("originalformat")
    private String originalFormat;

    private String title;
    @JsonProperty("title")
    private void unWrapNestedTitle(Map<String, Object> title) {
        this.title = (String)title.get("_content");
    }

    private String description;
    @JsonProperty("description")
    private void unWrapNestedDescription(Map<String, Object> description) {
        this.description = (String) description.get("_content");
    }

    private String latitude;

    private String longitude;

    @JsonProperty("location")
    private void unWrapNestedLatLon(Map<String, Object> location) {
        this.latitude = (String) location.get("latitude");
        this.longitude = (String) location.get("longitude");
    }

    public FlickrPhotoInfo() {
    }

    public FlickrPhotoInfo(String id, String secret, String server, int farm, String dateUploaded, int rotation, String originalSecret, String originalFormat, String title, String description, String latitude, String longitude) {
        this.id = id;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.dateUploaded = dateUploaded;
        this.rotation = rotation;
        this.originalSecret = originalSecret;
        this.originalFormat = originalFormat;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getOriginalSecret() {
        return originalSecret;
    }

    public void setOriginalSecret(String originalSecret) {
        this.originalSecret = originalSecret;
    }

    public String getOriginalFormat() {
        return originalFormat;
    }

    public void setOriginalFormat(String originalFormat) {
        this.originalFormat = originalFormat;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
