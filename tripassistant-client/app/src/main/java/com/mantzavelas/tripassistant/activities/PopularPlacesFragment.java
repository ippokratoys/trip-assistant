package com.mantzavelas.tripassistant.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.restservices.dtos.PopularPlaceDto;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;

import java.io.IOException;
import java.util.List;

public class PopularPlacesFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<PopularPlaceDto> popularPlaces;
    PopularPlacesTask popularPlacesTask;
    SupportMapFragment mapFragment;

    public PopularPlacesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popularPlacesTask = new PopularPlacesTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMap();
    }

    private void initMap() {
        FragmentManager fragmentManager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
       mMap = googleMap;

       popularPlacesTask.execute();
    }

    private class PopularPlacesTask extends AsyncTask<Void,Void,Void> {
        //should pass the mMap and popPlaces through params

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
                    mMap.addMarker(marker);
                    boundBuilder.include(latLng);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 0));
            }
        }
    }
}
