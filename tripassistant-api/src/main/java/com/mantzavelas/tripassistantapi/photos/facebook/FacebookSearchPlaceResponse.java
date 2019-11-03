package com.mantzavelas.tripassistantapi.photos.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookSearchPlaceResponse {

    private List<FacebookSearchPlaceResult> data;

    public FacebookSearchPlaceResponse() { /* Needed by Jackson deserializer */ }

    public List<FacebookSearchPlaceResult> getData() { return data; }
    public void setData(List<FacebookSearchPlaceResult> data) { this.data = data; }
}
