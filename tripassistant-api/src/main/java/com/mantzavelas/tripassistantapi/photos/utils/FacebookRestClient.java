package com.mantzavelas.tripassistantapi.photos.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantzavelas.tripassistantapi.photos.AbstractRestClient;
import com.mantzavelas.tripassistantapi.photos.IntervalType;
import com.mantzavelas.tripassistantapi.utils.PropertyUtil;
import org.springframework.web.util.UriComponentsBuilder;

public class FacebookRestClient extends AbstractRestClient {

    private static final String clientId = PropertyUtil.getProperty("apiClient.facebook.clientId");
    private static final String appSecret = PropertyUtil.getProperty("apiClient.facebook.secret");

    private FacebookRestClient() {}

    private static FacebookRestClient instance;
    public static FacebookRestClient create() {
        if (instance == null) {
            instance = new FacebookRestClient();
        }

        return instance;
    }

    public FacebookCurrentPlaceResponse getCurrentPlace(String latitude, String longitude) {
        String endpoint = baseUrl() + "/current_place/results";

        String fields = "id,name,about,description,checkins,location,category_list";

        ObjectMapper objectMapper = new ObjectMapper();
        String coordinates;
        try {
            coordinates = objectMapper.writeValueAsString(new FacebookLatLonResource(Double.parseDouble(latitude), Double.parseDouble(longitude), 65));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            coordinates = "";
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(endpoint)
            .queryParam("coordinates", coordinates)
            .queryParam("fields", fields)
            .queryParam("access_token", clientId + "|" + appSecret);

        return getForObject(uriBuilder.build().toUri(),FacebookCurrentPlaceResponse.class);
    }

    @Override
    public IntervalType interval() {
        return IntervalType.HOURLY;
    }

    @Override
    public int callsPerInterval() {
        return 180;
    }

    @Override
    public String baseUrl() {
        return "https://graph.facebook.com/v3.2";
    }

}
