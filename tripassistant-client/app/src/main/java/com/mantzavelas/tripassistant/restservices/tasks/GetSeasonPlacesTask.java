package com.mantzavelas.tripassistant.restservices.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.messaging.PopularSeasonPlaceRequest;
import com.mantzavelas.tripassistant.restservices.dtos.PlaceDto;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class GetSeasonPlacesTask extends AsyncTask<Pair<String, String>, Void, List<PlaceDto>> {

    private NotifyDataSetChanged datasetListener;

    public GetSeasonPlacesTask(NotifyDataSetChanged datasetListener) { this.datasetListener = datasetListener; }

    @Override
    protected List<PlaceDto> doInBackground(Pair<String, String>... pairs) {
        if (pairs.length == 0) {
            return Collections.emptyList();
        }

        List<PlaceDto> dtos;
        try {
            dtos = new PopularSeasonPlaceRequest()
                    .getPlaces(pairs[0].first, pairs[0].second);
        } catch (IOException | TimeoutException | InterruptedException e) {
            Log.e(this.getClass().getSimpleName(), "Error getting popular season place message:" + e + "----------");
            e.printStackTrace();
            dtos = Collections.emptyList();
        }

        return dtos;
    }

    @Override
    protected void onPostExecute(List<PlaceDto> placeDtos) {
        if (placeDtos.isEmpty()) {
            Log.e("[messaging-season-place", "No places fetched");
            return;
        }

        datasetListener.onDataSetChanged(placeDtos);
    }
}
