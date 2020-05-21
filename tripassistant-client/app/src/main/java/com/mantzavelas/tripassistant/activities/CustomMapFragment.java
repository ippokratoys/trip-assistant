package com.mantzavelas.tripassistant.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

    public CustomMapFragment() { pointsList = new ArrayList<>(); }

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

    protected void initMap() {
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

    protected void updateMapWithMarkers() throws InvocationTargetException, IllegalAccessException {
        updateMapWithMarkers(false);
    }

    private static int pointCounter = 1;
    protected void updateMapWithMarkers(boolean numberPoints) throws InvocationTargetException, IllegalAccessException {
        if (!pointsList.isEmpty()) {
            LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
            for (T point : pointsList) {
                String latitude = MapUtil.getLatitudeFromReflection(point);
                String longitude = MapUtil.getLongitudeFromReflection(point);
                String title = MapUtil.getMapPointTitleFromReflection(point);

                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                MarkerOptions marker = new MarkerOptions().position(latLng)
                        .title(title);

                if (numberPoints) {
                    View markerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_point_w_number, null);
                    TextView numTxt = markerView.findViewById(R.id.custom_map_point_textview);
                    numTxt.setText(String.valueOf(pointCounter));
                    marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getContext(), markerView)));
                }
                googleMap.addMarker(marker);
                boundBuilder.include(latLng);
                pointCounter++;
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 50));
            pointCounter = 1;
        }

    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
