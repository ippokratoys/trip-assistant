package com.mantzavelas.tripassistant.restservices.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.models.enums.TripStatus;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class GetTripsTask extends AsyncTask<TripStatus, Void, Response<List<TripDto>>> {

    private WeakReference<Context> context;
    private NotifyDataSetChanged dataSetChanged;

    public GetTripsTask(Context context, NotifyDataSetChanged dataSetChanged) {
        this.context = new WeakReference<>(context);
        this.dataSetChanged = dataSetChanged;
    }

    @Override
    protected Response<List<TripDto>> doInBackground(TripStatus... tripStatuses) {
        Response<List<TripDto>> response;

        String status = tripStatuses.length >= 1 ? tripStatuses[0].name() : "";

        try {
             response = RestClient.create()
                      .getTripAssistantService()
                      .getTrips(status)
                      .execute();

             return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Response<List<TripDto>> listResponse) {
        Context cont = context.get();

        List<TripDto> tripDtos;
        if (listResponse != null) {
            if (listResponse.isSuccessful() && listResponse.body() != null) {
                tripDtos = listResponse.body();
            } else {
                Toast.makeText(cont, listResponse.message(), Toast.LENGTH_SHORT).show();
                tripDtos = Collections.emptyList();
            }
        } else {
            Toast.makeText(cont, "An error occured, please try again later", Toast.LENGTH_SHORT).show();
            tripDtos = Collections.emptyList();
        }

        dataSetChanged.onDataSetChanged(tripDtos);
    }
}
