package com.mantzavelas.tripassistant.restservices.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;
import com.mantzavelas.tripassistantapi.dtos.LocationDto;

import java.io.IOException;

import retrofit2.Response;

public class StartTripTask extends AsyncTask<Long, Void, Response<TripDto>> {

    private static final String TAG = "[" + StartTripTask.class.getCanonicalName() + "]";

    private NotifyDataSetChanged datasetListener;

    public StartTripTask(NotifyDataSetChanged datasetListener) {
        this.datasetListener = datasetListener;
    }


    @Override
    protected Response<TripDto> doInBackground(Long... longs) {
        if (longs.length <= 0) {
            return null;
        }

        LocationDto source = new LocationDto(CurrentUser.getInstance().getLatitude(), CurrentUser.getInstance().getLongitude());
        Response<TripDto> response;
        try {
            response = RestClient.create().getTripAssistantService().beginTrip(longs[0], source).execute();
        } catch (IOException e) {
            Log.e(TAG, "Failed to start trip.");
            return null;
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response<TripDto> tripDtoResponse) {
        if (tripDtoResponse == null || tripDtoResponse.body() == null) {
            return;
        }

        TripDto trip = tripDtoResponse.body();
        if (trip.getPlaces().isEmpty()) {
            Log.d(TAG, "Returned trip's places are empty.");
        }

        datasetListener.onDataSetChanged(trip.getPlaces());
    }
}
