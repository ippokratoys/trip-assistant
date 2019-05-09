package com.mantzavelas.tripassistant.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.NumberPicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.models.enums.CategoryEnum;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;
import com.mantzavelas.tripassistant.restservices.dtos.PlaceDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.HttpException;
import retrofit2.Response;

public class SearchPlaceService {

    private static final int MAX_DISTANCE_KM = 20000;

    //till 500 meters, increment by 100, after that by 0.5 km
    public static List<String> getDistances() {
        List<String> distances = new ArrayList<>();

        int currentDistance = 100; //starting in meters
        while (currentDistance <= MAX_DISTANCE_KM) {
            if (currentDistance < 500) {
                distances.add(Integer.toString(currentDistance) + " m");
                currentDistance += 100;
                continue;
            }

            distances.add(Double.toString(currentDistance/1000.0) + " km");
            currentDistance += 500;
        }
        return distances;
    }

    public static String[] getCategories() {
        String[] categories = new String[CategoryEnum.values().length];

        for (CategoryEnum categoryEnum : CategoryEnum.values()) {
            categories[categoryEnum.ordinal()] = categoryEnum.name();
        }
        return categories;
    }

    public static String stripeOutUnitFromString(String meters) {
        meters = meters.replace("m","");
        meters = meters.replace("k","");
        meters = meters.trim();

        return String.valueOf((int)Double.parseDouble(meters));
    }

    public static MarkerOptions createMarker(PlaceDto place) {
        LatLng latLng = new LatLng(Double.parseDouble(place.getLatitude()), Double.parseDouble(place.getLongitude()));
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title(place.getTitle());

        return marker;
    }

    public static NumberPicker createNumberPicker(Dialog dialog, int viewId, String[] displayedValues) {
        NumberPicker numberPicker = dialog.findViewById(viewId);

        numberPicker.setDisplayedValues(displayedValues);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(displayedValues.length-1);
        numberPicker.setWrapSelectorWheel(false);

        return numberPicker;
    }

    public static class Task extends AsyncTask<Void, Void, Void> {

        private String category;
        private String meters;
        private GoogleMap gMap;

        private TripAssistantService service;
        private Map<String, String> params;
        private List<PlaceDto> places;

        public Task(String category, String meters, GoogleMap gMap) {
            this.category = category;
            this.meters = meters;
            this.gMap = gMap;

            service = RestClient.create().getTripAssistantService();
            params = new HashMap<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String lat = CurrentUser.getInstance().getLatitude();
            String lon = CurrentUser.getInstance().getLongitude();

            params.put("lat", lat != null ? lat : "1");
            params.put("lon", lon != null ? lon : "1");
            if (meters != null && !meters.isEmpty()) {
                params.put("radius", stripeOutUnitFromString(meters));
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
                    MarkerOptions marker = createMarker(place);
                    gMap.addMarker(marker);
                    boundBuilder.include(marker.getPosition());
                }
                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(), 0));
            }
        }
    }

    public static class DialogBuilder {
        private Dialog dialog;

        public DialogBuilder(Activity activity, final GoogleMap googleMap, final String category, final String meters) {
            dialog = new Dialog(activity);
            dialog.setContentView(R.layout.search_wizard);
        }

        public DialogBuilder withCategoryListener(NumberPicker.OnValueChangeListener listener) {
            NumberPicker categoryPicker = createNumberPicker(dialog, R.id.search_category_picker
                    , getCategories());
            categoryPicker.setOnValueChangedListener(listener);
            return this;
        }

        public DialogBuilder withMetersListener(NumberPicker.OnValueChangeListener listener) {
            NumberPicker distancePicker = createNumberPicker(dialog,R.id.search_distance_picker,
                    getDistances().toArray(new String[0]));
            distancePicker.setOnValueChangedListener(listener);
            return this;
        }

        public DialogBuilder withCancelClickListener(DialogInterface.OnCancelListener listener) {
            dialog.setOnCancelListener(listener);
            return this;
        }

        public Dialog build() {
            return dialog;
        }

    }
}
