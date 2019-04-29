package com.mantzavelas.tripassistant.restservices.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class DeleteTripTask extends AsyncTask<TripDto, Void, Map<Response, TripDto>> {

    private WeakReference<Context> context;
    private NotifyDataSetChanged dataSetChangedListener;
    private List<TripDto> tripDtos;

    public DeleteTripTask(Context context, NotifyDataSetChanged dataSetChangedListener, List<TripDto> tripDtos) {
        this.context = new WeakReference<>(context);
        this.dataSetChangedListener = dataSetChangedListener;
        this.tripDtos = tripDtos;
    }

    @Override
    protected Map<Response, TripDto> doInBackground(TripDto... tripDtos) {
        TripAssistantService service = RestClient.create().getTripAssistantService();
        Map<Response, TripDto> responses = new HashMap<>();
        for (TripDto tripDto : tripDtos) {
            try {
                responses.put(service.deleteTrip(tripDto.getId()).execute(), tripDto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responses;
    }

    @Override
    protected void onPostExecute(Map<Response, TripDto> responses) {
        Context cont = context.get();
        for (Response response : responses.keySet()) {
            if (!response.isSuccessful()) {
                Toast.makeText(cont, "Failed to delete trip " + responses.get(response).getTitle(), Toast.LENGTH_SHORT).show();
                return;
            }
            tripDtos.remove(responses.get(response));
        }

        dataSetChangedListener.onDataSetChanged(tripDtos);
    }
}
