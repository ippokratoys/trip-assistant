package com.mantzavelas.tripassistantapi.photos.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlickrSearchResponse {

    private FlickrPhotosSearch photos;

    @JsonProperty("stat")
    private String status;

    public FlickrSearchResponse() { }

    public FlickrSearchResponse(FlickrPhotosSearch photos, String status) {
        this.photos = photos;
        this.status = status;
    }

    public FlickrPhotosSearch getPhotos() {
        return photos;
    }

    public void setPhotos(FlickrPhotosSearch photos) {
        this.photos = photos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
