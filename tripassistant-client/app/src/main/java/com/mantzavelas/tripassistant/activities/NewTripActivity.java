package com.mantzavelas.tripassistant.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.activities.helpers.SimpleDialogBuilder;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.models.Place;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;
import com.mantzavelas.tripassistant.restservices.resources.TripResource;
import com.mantzavelas.tripassistant.services.SearchPlaceService;
import com.mantzavelas.tripassistant.utils.PermissionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class NewTripActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private FloatingActionButton fab;
    private SupportMapFragment mapFragment;
    private Button filterButton;

    private LocationManager locationManager;
    private GoogleMap googleMap;
    private SearchPlaceService service;
    private String meters;
    private String category;
    private Dialog dialog;
    private Map<String, Place> tripPlaces;
    private String title;
    private String description;
    private Date dateScheduled;

    private static int titleViewId = View.generateViewId();
    private static int descViewId = View.generateViewId();
    private static int dateViewId = View.generateViewId();

    public static final String PLACES_EXTRA = "PLACES_EXTRA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        fab = findViewById(R.id.new_trip_fab);
        filterButton = findViewById(R.id.search_filters_button);

        initMap();

        service = new SearchPlaceService();
        tripPlaces = new HashMap<>();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        filterButton.setOnClickListener(filterButtonListener);
        fab.setOnClickListener(fabClickListener);

        LocationListener locationListener = new LocationListenerImpl();

        initMap();
        if (!PermissionUtil.checkAndRequestLocationPermissions(this)) {
            return;
        }

        if (PermissionUtil.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION)) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

                if (CurrentUser.getInstance().getLatitude() == null || CurrentUser.getInstance().getLongitude() == null) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        CurrentUser.getInstance().setUserLocation(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder(this, "Create New Trip",
                "Fill the trip's details");
        dialogBuilder.setLayout(LinearLayout.VERTICAL);

        dialogBuilder.addEditText("Title", InputType.TYPE_CLASS_TEXT, titleViewId);
        dialogBuilder.addEditText("Description", InputType.TYPE_CLASS_TEXT, descViewId);
        dialogBuilder.addView(new DatePicker(this), dateViewId);
        dialogBuilder.addDefaultButtonListeners(positiveDialogButtonListener, null);

        dialogBuilder.createDialog().show();
    }

    private View.OnClickListener filterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.show();
        }
    };

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();

            if (tripPlaces.isEmpty()) {
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }

            CreateTripTask task = new CreateTripTask();
            task.execute(tripPlaces.values().toArray(new Place[0]));
        }
    };

    private DialogInterface.OnClickListener positiveDialogButtonListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            title = getEditTextValueFromDialog(dialog, titleViewId);
            description = getEditTextValueFromDialog(dialog, descViewId);

            DatePicker picker = ((AlertDialog)dialog).findViewById(dateViewId);
            Calendar cal = Calendar.getInstance();
            cal.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
            dateScheduled = cal.getTime();
        }
    };

    private void endActivity() {
        Intent intent = new Intent();

        TripDto trip = new TripDto();
        trip.setPlaces(new ArrayList<>(tripPlaces.values()));
        trip.setScheduledFor(dateScheduled);
        trip.setTitle(title);
        trip.setDescription(description);

        intent.putExtra(PLACES_EXTRA, trip);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getEditTextValueFromDialog(DialogInterface dialogInterface, int viewId) {
        EditText editText = ((AlertDialog)dialogInterface).findViewById(viewId);

        if (editText == null) {
            return "";
        }

        return editText.getText().toString();
    }

    private DialogInterface.OnCancelListener dialogCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            googleMap.clear();
            new SearchPlaceService.Task(category, meters, googleMap).execute();
        }
    };

    private NumberPicker.OnValueChangeListener metersListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            meters = picker.getDisplayedValues()[newVal];
        }
    };

    private NumberPicker.OnValueChangeListener categoryListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            category = picker.getDisplayedValues()[newVal];
        }
    };

    private void initMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.search_map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.search_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        dialog = new SearchPlaceService.DialogBuilder(this, googleMap, category, meters)
                .withCategoryListener(categoryListener)
                .withMetersListener(metersListener)
                .withCancelClickListener(dialogCancelListener)
                .build();

        googleMap.setOnMarkerClickListener(this);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!tripPlaces.containsKey(marker.getId())) {
            Place place = new com.mantzavelas.tripassistant.models.Place();
            place.setTitle(marker.getTitle());
            place.setDescription(marker.getSnippet());
            place.setLatitude(String.valueOf(marker.getPosition().latitude));
            place.setLongitude(String.valueOf(marker.getPosition().longitude));

            tripPlaces.put(marker.getId(), place);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            tripPlaces.remove(marker.getId());
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        return false;
    }

    class CreateTripTask extends AsyncTask<Place, Void, Response<TripDto>> {

        @Override
        protected Response<TripDto> doInBackground(Place... places) {
            try
            {
                TripResource resource = new TripResource();
                resource.setPlaces(Arrays.asList(places));
                resource.setTitle(title);
                resource.setDescription(description);
                resource.setScheduledFor(dateScheduled);

                return RestClient.create()
                                 .getTripAssistantService()
                                 .createTrip(resource)
                                 .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response<TripDto> response) {

            if (response != null && !response.isSuccessful()) {
                Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_SHORT).show();
                return;
            }

            endActivity();
        }
    }
}
