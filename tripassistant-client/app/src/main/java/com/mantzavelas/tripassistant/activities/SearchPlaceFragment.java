package com.mantzavelas.tripassistant.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.activities.helpers.NotLoggedInAlertDialog;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;
import com.mantzavelas.tripassistant.restservices.dtos.PlaceDto;
import com.mantzavelas.tripassistant.services.SearchPlaceService;
import com.mantzavelas.tripassistant.utils.PermissionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.HttpException;
import retrofit2.Response;

public class SearchPlaceFragment extends Fragment implements OnMapReadyCallback {

    private static String category;
    private static String meters;
    private SearchPlaceTask searchPlaceTask;
    private SearchPlaceService searchService;

    private GoogleMap gMap;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;

    private Button filterButton;

    private static final int MY_PERMISSION = 1234;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchService = new SearchPlaceService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMap();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null || getContext() == null) {
            return;
        }

        if (!CurrentUser.getInstance().isLoggedIn()) {
            NotLoggedInAlertDialog dialog = new NotLoggedInAlertDialog(getContext(), getFragmentManager());
            dialog.create("You have to be logged-in in order to search for places around you!" +
                    "\nClick on Login to log-in or click on Cancel to go to the previous screen");
            dialog.show();
        }

        filterButton = view.findViewById(R.id.search_filters_button);
        filterButton.setOnClickListener(filterButtonClickListener);

        LocationListener locationListener = new LocationListenerImpl();

        initMap();
        if (!PermissionUtil.checkAndRequestLocationPermissions(getActivity())) {
            return;
        }

        if (PermissionUtil.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION)) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                if (CurrentUser.getInstance().getLatitude() == null || CurrentUser.getInstance().getLongitude() == null) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    CurrentUser.getInstance().setUserLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        searchPlaceTask = new SearchPlaceTask();
        searchPlaceTask.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationListener locationListener = new LocationListenerImpl();
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    View.OnClickListener filterButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            invokeDialog();
        }
    };

    private void invokeDialog() {
        if (getActivity() == null) {
            return;
        }

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.search_wizard);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                gMap.clear();
                new SearchPlaceTask().execute();
            }
        });

        NumberPicker categoryPicker = createNumberPicker(dialog, R.id.search_category_picker, searchService.getCategories());
        NumberPicker distancePicker = createNumberPicker(dialog,R.id.search_distance_picker,
            searchService.getDistances().toArray(new String[0]));

        categoryPicker.setOnValueChangedListener(categoryListener);
        distancePicker.setOnValueChangedListener(metersListener);

        dialog.show();
    }

    private NumberPicker createNumberPicker(Dialog dialog, int viewId, String[] displayedValues) {
        NumberPicker numberPicker = dialog.findViewById(viewId);

        numberPicker.setDisplayedValues(displayedValues);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(displayedValues.length-1);
        numberPicker.setWrapSelectorWheel(false);

        return numberPicker;
    }

    NumberPicker.OnValueChangeListener metersListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            meters = picker.getDisplayedValues()[newVal];
        }
    };

    NumberPicker.OnValueChangeListener categoryListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            category = picker.getDisplayedValues()[newVal];
        }
    };

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onDetach() { super.onDetach(); }

    @Override
    public void onDestroy() { super.onDestroy(); }

    private void initMap() {
        if (getActivity() == null) {
            return;
        }

        FragmentManager fragmentManager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.search_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.search_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { gMap = googleMap; }

    class SearchPlaceTask extends AsyncTask<Void, Void, Void> {

        private TripAssistantService service = RestClient.create().getTripAssistantService();
        private Map<String, String> params = new HashMap<>();
        private List<PlaceDto> places;

        @Override
        protected Void doInBackground(Void... voids) {
            String lat = CurrentUser.getInstance().getLatitude();
            String lon = CurrentUser.getInstance().getLongitude();

            params.put("lat", lat != null ? lat : "1");
            params.put("lon", lon != null ? lon : "1");
            if (meters != null && !meters.isEmpty()) {
                params.put("radius", searchService.stripeOutUnitFromString(meters));
            }
            if (category != null && !category.isEmpty()) {
                params.put("category", category);
            }

            try {
                Response<List<PlaceDto>> response = service.searchPlaces(params).execute();
                if (response.code() != 200) {
                    throw new HttpException(response);
                }

                places = response.body();
            } catch (IOException | HttpException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            LatLngBounds.Builder boundBuilder = new LatLngBounds.Builder();
            if (places != null && !places.isEmpty()) {
                for (PlaceDto place : places) {
                    MarkerOptions marker = searchService.createMarker(place);
                    gMap.addMarker(marker);
                    boundBuilder.include(marker.getPosition());
                }

                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 0));
            }
        }
    }
}
