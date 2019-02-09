package com.mantzavelas.tripassistantapi.photos.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookCurrentPlaceResponse {

    private List<FacebookCurrentPlaceResult> data;

    public FacebookCurrentPlaceResponse() {
    }

    public List<FacebookCurrentPlaceResult> getData() { return data; }
    public void setData(List<FacebookCurrentPlaceResult> data) { this.data = data; }
}
