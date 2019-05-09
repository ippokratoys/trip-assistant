package com.mantzavelas.tripassistant.restservices.tasks;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;
import com.mantzavelas.tripassistant.restservices.dtos.PopularPlaceDto;

import java.io.IOException;
import java.util.List;

public class PopularPlacesTask extends AsyncTask<Void,Void,Void> {

    private GoogleMap googleMap;
    private List<PopularPlaceDto> popularPlaces;

    public PopularPlacesTask(GoogleMap googleMap, List<PopularPlaceDto> popularPlaces) {
        this.googleMap = googleMap;
        this.popularPlaces = popularPlaces;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        TripAssistantService service = RestClient.create().getTripAssistantService();
        try {
            popularPlaces = service.getTop10Places().execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
        if (popularPlaces != null && !popularPlaces.isEmpty()) {
            for (PopularPlaceDto popularPlace : popularPlaces) {
                LatLng latLng = new LatLng(Double.parseDouble(popularPlace.getLatitude()), Double.parseDouble(popularPlace.getLongitude()));
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);
                marker.title(popularPlace.getName());
                //marker.snippet(popularPlace.getDescription());
                googleMap.addMarker(marker);
                boundBuilder.include(latLng);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 0));
        }
    }
}
