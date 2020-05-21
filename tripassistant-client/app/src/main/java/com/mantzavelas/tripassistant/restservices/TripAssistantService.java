package com.mantzavelas.tripassistant.restservices;

import com.mantzavelas.tripassistant.restservices.dtos.CustomPlaceDto;
import com.mantzavelas.tripassistant.restservices.dtos.PopularPlaceDto;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;
import com.mantzavelas.tripassistant.restservices.dtos.UserTokenDto;
import com.mantzavelas.tripassistant.restservices.resources.TripResource;
import com.mantzavelas.tripassistant.restservices.resources.UserCredentialResource;
import com.mantzavelas.tripassistant.restservices.resources.UserResource;
import com.mantzavelas.tripassistantapi.dtos.LocationDto;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface TripAssistantService {

    //AUTHENTICATION
    @POST("auth/signup")
    Call<UserTokenDto> register(@Body UserResource userResource);

    @POST("auth/signin")
    Call<UserTokenDto> login(@Body UserCredentialResource resource);

    //PLACES
    @GET("places")
    Call<List<PopularPlaceDto>> getTop10Places();

    @GET("places/search")
    Call<List<CustomPlaceDto>> searchPlaces(@QueryMap Map<String, String> params);

    //USER
    @POST("user/deviceToken")
    Call<Void> registerDeviceToken(@Body String token);

    //USER TRIPS
    @POST("user/trip")
    Call<TripDto> createTrip(@Body TripResource resource);

    @GET("user/trip")
    Call<List<TripDto>> getTrips(@Query("status") String status);

    @DELETE("user/trip/{tripId}")
    Call<Void> deleteTrip(@Path("tripId") Long tripId);

    @POST("user/trip/{tripId}/start")
    Call<TripDto> beginTrip(@Path("tripId") Long tripId, @Body LocationDto dto);

    @POST("user/trip/{tripId}/stop")
    Call<Void> stopTrip(@Path("tripId") Long tripId);
}
