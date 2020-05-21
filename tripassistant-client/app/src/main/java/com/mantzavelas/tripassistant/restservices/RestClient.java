package com.mantzavelas.tripassistant.restservices;

import com.mantzavelas.tripassistant.restservices.interceptors.AuthorizationInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RestClient {

    private static final String TRIP_ASSISTANT_API_BASE_URL = "http://83.212.106.175:8081/api/";
//    private static final String TRIP_ASSISTANT_API_BASE_URL = "http://192.168.1.5:8080/api/";


    private static RestClient restClient;
    private static Retrofit tripAssistantClient;

    public static RestClient create() {
        if (restClient == null) {
            restClient = new RestClient();
        }

        return restClient;
    }

    public TripAssistantService getTripAssistantService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                                                  .addInterceptor(new AuthorizationInterceptor())
                                                  .addInterceptor(loggingInterceptor)
                                                  .build();

        if(tripAssistantClient == null) {
            tripAssistantClient = new Retrofit.Builder()
                .baseUrl(TRIP_ASSISTANT_API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient)
                .build();
        }

        return tripAssistantClient.create(TripAssistantService.class);
    }

}
