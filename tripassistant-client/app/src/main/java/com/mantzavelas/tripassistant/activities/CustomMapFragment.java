package com.mantzavelas.tripassistant.activities;

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
import com.mantzavelas.tripassistant.utils.MapUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CustomMapFragment<T> extends Fragment implements OnMapReadyCallback {

    protected GoogleMap googleMap;
    protected List<T> pointsList;
    private SupportMapFragment mapFragment;

    public CustomMapFragment() {
        pointsList = new ArrayList<>();
    }

    public List<T> getPointsList() { return pointsList; }
    public void setPointsList(List<T> pointsList) { this.pointsList = pointsList; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        try {
            updateMapWithMarkers();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void updateMapWithMarkers() throws InvocationTargetException, IllegalAccessException {
        if (!pointsList.isEmpty()) {
            LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
            for (T point : pointsList) {
                String latitude = MapUtil.getLatitudeFromReflection(point);
                String longitude = MapUtil.getLongitudeFromReflection(point);
                String title = MapUtil.getMapPointTitleFromReflection(point);

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                MarkerOptions marker = new MarkerOptions();
                marker.position(latLng);
                marker.title(title);
                googleMap.addMarker(marker);
                boundBuilder.include(latLng);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 50));
        }
    }
}
