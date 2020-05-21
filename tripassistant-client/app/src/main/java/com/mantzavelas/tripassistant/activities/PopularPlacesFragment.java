package com.mantzavelas.tripassistant.activities;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.mantzavelas.tripassistant.restservices.dtos.PopularPlaceDto;
import com.mantzavelas.tripassistant.restservices.tasks.PopularPlacesTask;
import com.mantzavelas.tripassistantapi.dtos.LocationDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

        if(popularPlacesTask!= null && !popularPlacesTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            popularPlacesTask.cancel(true);
        }
    }

    private ClusterManager<MapItem> clusterManager;
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        clusterManager = new ClusterManager<>(getContext(), googleMap);
        try {
            InputStream is = getResources().openRawResource(
                    getResources().getIdentifier("photos",
                            "raw", getContext().getPackageName()));

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            List<LocationDto> photos = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                photos.add(new LocationDto(line.split(",")[0], line.split(",")[1]));
            }

            photos.forEach(photo -> {
                MapItem mapItem = new MapItem(photo.getLatitude(), photo.getLongitude());
                clusterManager.addItem(mapItem);
//                MarkerOptions marker = new MarkerOptions().position(latLng);
//                googleMap.addMarker(marker);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
//        popularPlacesTask = new PopularPlacesTask(googleMap, pointsList);
//        popularPlacesTask.execute();
    }

    public class MapItem implements ClusterItem {
        private LatLng mPosition;
        private String mTitle;
        private String mSnippet;

        public MapItem(String lat, String lng) {
            mPosition = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        }

        public MapItem(double lat, double lng, String title, String snippet) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

        @Override
        public String getSnippet() {
            return mSnippet;
        }
    }

}
