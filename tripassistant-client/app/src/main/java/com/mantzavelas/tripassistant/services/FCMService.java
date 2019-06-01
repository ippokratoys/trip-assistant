package com.mantzavelas.tripassistant.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.mantzavelas.tripassistant.activities.listeners.UserLoggedInListener;
import com.mantzavelas.tripassistant.restservices.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FCMService extends FirebaseMessagingService implements UserLoggedInListener {

    private static final String TAG = FCMService.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        Log.d(TAG,"Refreshed device token: " + s);

        super.onNewToken(s);
        UserLoginListeners.INSTANCE.addListener(this);
    }

    @Override
    public void userLoggedIn() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        RestClient.create()
                                  .getTripAssistantService()
                                  .registerDeviceToken(token)
                                  .enqueue(getRegisterTokenCallback());

                        Log.d(TAG, "Registration token: " + token);
                    }
                }
        );
    }

    private Callback<Void> getRegisterTokenCallback() {
        return new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d(TAG, "Successfully registered device token");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d(TAG, "Failed to register device token");
            }
        };
    }
}
