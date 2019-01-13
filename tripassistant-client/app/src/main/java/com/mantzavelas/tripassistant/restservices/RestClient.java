package com.mantzavelas.tripassistant.restservices;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    private static final String TRIP_ASSISTANT_API_BASE_URL = "http://83.212.106.175:8080/api/";

    private static RestClient restClient;
    private static Retrofit tripAssistantClient;

    public static RestClient create() {
        if (restClient == null) {
            restClient = new RestClient();
        }

        return restClient;
    }

    public TripAssistantService getTripAssistantService() {
        if(tripAssistantClient == null) {
            tripAssistantClient = new Retrofit.Builder()
                .baseUrl(TRIP_ASSISTANT_API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        }

        return tripAssistantClient.create(TripAssistantService.class);
    }

}
