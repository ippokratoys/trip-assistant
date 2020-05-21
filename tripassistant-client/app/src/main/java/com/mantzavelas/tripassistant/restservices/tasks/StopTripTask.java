package com.mantzavelas.tripassistant.restservices.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mantzavelas.tripassistant.activities.listeners.TaskListener;
import com.mantzavelas.tripassistant.restservices.RestClient;

import java.io.IOException;

import retrofit2.Response;

public class StopTripTask extends AsyncTask<Long, Void, Response<Void>> {

    private static final String TAG = "[" + StopTripTask.class.getCanonicalName() + "]";
    private TaskListener listener;

    public StopTripTask(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Response<Void> doInBackground(Long... longs) {
        if (longs.length <= 0) {
            return null;
        }

        Response<Void> response;
        try {
            response = RestClient.create().getTripAssistantService().stopTrip(longs[0]).execute();
        } catch (IOException e) {
            Log.e(TAG, "Failed to stop trip.");
            return null;
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response<Void> voidResponse) {
        listener.onComplete();
    }
}
