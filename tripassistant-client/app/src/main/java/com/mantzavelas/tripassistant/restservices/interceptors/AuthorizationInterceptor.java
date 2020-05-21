package com.mantzavelas.tripassistant.restservices.interceptors;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mantzavelas.tripassistant.models.CurrentUser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        if (!CurrentUser.getInstance().isLoggedIn()) {
            Log.e("retrofit 2","Authorization header is already present or token is empty....");
            return chain.proceed(chain.request());
        }

        Request authorizedRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", CurrentUser.getInstance().retrieveAccessTokenWithBearer())
                .build();

        return chain.proceed(authorizedRequest);
    }
}
