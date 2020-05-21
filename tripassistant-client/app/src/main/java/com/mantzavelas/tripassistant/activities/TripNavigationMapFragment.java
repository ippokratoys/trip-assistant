package com.mantzavelas.tripassistant.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.activities.listeners.TaskListener;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.models.Place;
import com.mantzavelas.tripassistant.restservices.tasks.StartTripTask;
import com.mantzavelas.tripassistant.restservices.tasks.StopTripTask;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TripNavigationMapFragment extends CustomMapFragment<Place> implements NotifyDataSetChanged, TaskListener {

    private Long tripId;
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        Button button = new Button(view.getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        button.setText("End");
        button.setVisibility(View.VISIBLE);
        button.setLayoutParams(params);
        button.setOnClickListener(v -> new StopTripTask(this).execute(tripId));

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        List<LatLng> path = pointsList.stream()
                                      .map(s -> new LatLng(Double.parseDouble(s.getLatitude()), Double.parseDouble(s.getLongitude())))
                                      .collect(Collectors.toList());
        PolylineOptions options = new PolylineOptions().addAll(path);
        googleMap.addPolyline(options);

        new StartTripTask(this).execute(tripId);
    }


    @Override
    public void onDataSetChanged(Collection<?> dataSet) {
        this.pointsList = (List<Place>) dataSet;
        googleMap.clear();
        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(
                Double.parseDouble(CurrentUser.getInstance().getLatitude())
                , Double.parseDouble(CurrentUser.getInstance().getLongitude())));
        points.addAll(pointsList.stream()
                .map(s -> new LatLng(Double.parseDouble(s.getLatitude()), Double.parseDouble(s.getLongitude())))
                .collect(Collectors.toList()));
        PolylineOptions options = new PolylineOptions().addAll(points);
        googleMap.addPolyline(options);
        try {
            updateMapWithMarkers(true);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        getFragmentManager().popBackStack();
    }
}
