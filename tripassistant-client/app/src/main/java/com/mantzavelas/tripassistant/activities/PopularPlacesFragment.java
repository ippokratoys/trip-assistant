package com.mantzavelas.tripassistant.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mantzavelas.tripassistant.restservices.dtos.PopularPlaceDto;
import com.mantzavelas.tripassistant.restservices.tasks.PopularPlacesTask;

public class PopularPlacesFragment extends CustomMapFragment<PopularPlaceDto> implements OnMapReadyCallback {

    PopularPlacesTask popularPlacesTask;

    public PopularPlacesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(!popularPlacesTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            popularPlacesTask.cancel(true);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        popularPlacesTask = new PopularPlacesTask(googleMap, pointsList);
        popularPlacesTask.execute();
    }

}
