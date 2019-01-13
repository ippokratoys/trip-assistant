package com.mantzavelas.tripassistant.restservices;

import com.mantzavelas.tripassistant.restservices.dtos.PopularPlaceDto;
import com.mantzavelas.tripassistant.restservices.resources.UserCredentialResource;
import com.mantzavelas.tripassistant.restservices.resources.UserResource;
import com.mantzavelas.tripassistant.restservices.dtos.UserTokenDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TripAssistantService {

    @POST("auth/signup")
    Call<UserTokenDto> register(@Body UserResource userResource);

    @POST("auth/signin")
    Call<UserTokenDto> login(@Body UserCredentialResource resource);

    @GET("places")
    Call<List<PopularPlaceDto>> getTop10Places();

}
