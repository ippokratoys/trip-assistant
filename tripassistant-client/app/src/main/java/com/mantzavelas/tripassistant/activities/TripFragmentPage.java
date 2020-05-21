package com.mantzavelas.tripassistant.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.activities.helpers.SimpleDialogBuilder;
import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.adapters.TripsAdapter;
import com.mantzavelas.tripassistant.models.Place;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;
import com.mantzavelas.tripassistant.restservices.tasks.DeleteTripTask;

import java.util.ArrayList;
import java.util.List;

public abstract class TripFragmentPage extends Fragment implements NotifyDataSetChanged {

    private static final int SELECT_PLACES_REQUEST = 1000;

    public List<TripDto> items;

    public RecyclerView.Adapter adapter;

    public RecyclerView recyclerView;

    public FloatingActionButton fab;

    public abstract String getTitle();

    public abstract int getLayoutId();

    public int getRecyclerViewId() { return R.id.all_trips_recyclerview; }

    public int getFabId() { return R.id.all_trips_fab; }

    public boolean isFabEnabled() { return true; }

    public boolean displayMidButton() { return false; }

    public TripFragmentPage() {
        items = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(getRecyclerViewId());
        fab = view.findViewById(getFabId());

        fab.setEnabled(isFabEnabled());
        fab.setOnClickListener(fabClickListener);

        adapter = new TripsAdapter(items, recyclerViewRowListener, displayMidButton());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PLACES_REQUEST && resultCode == Activity.RESULT_OK) {
            TripDto trip = (TripDto) data.getExtras().getSerializable(NewTripActivity.PLACES_EXTRA);

            items.add(trip);
            this.onDataSetChanged(items);
        }
    }

    private View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newTripIntent = new Intent(getContext(), NewTripActivity.class);
            startActivityForResult(newTripIntent, SELECT_PLACES_REQUEST);
        }
    };

    private TripsAdapter.RecyclerViewClickListener recyclerViewRowListener = new TripsAdapter.RecyclerViewClickListener() {
        @Override
        public void onPositionClicked(int position, int componentId) {
            switch (componentId) {
                case R.id.row_place_details_btn:
                    Intent intent = new Intent(getContext(), TripDetailsActivity.class);
                    intent.putExtra("TRIP", items.get(position));
                    startActivity(intent);
                    break;
                case R.id.row_place_middle_btn:
                    TripNavigationMapFragment mapFragment = new TripNavigationMapFragment();
                    mapFragment.setTripId(items.get(position).getId());
                    mapFragment.setPointsList(items.get(position).getPlaces());

                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.all_trips_layout, mapFragment)
                            .commit();
                    break;
                case R.id.row_place_view_on_map_btn:
                    CustomMapFragment<Place> fragment = new CustomMapFragment<>();
                    fragment.setPointsList(items.get(position).getPlaces());

                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.all_trips_layout, fragment)
                            .commit();
                    break;
                case R.id.row_place_delete_btn:
                    String title = "Delete trip " + items.get(position).getTitle();
                    String message = "Are you sure you want to delete this trip?";
                    SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder(getContext(), title, message);
                    dialogBuilder.addDefaultButtonListeners(getOkClickListener(items.get(position)), null);
                    dialogBuilder.createDialog().show();
                    break;
                default:
                    break;
            }
        }
    };

    private DialogInterface.OnClickListener getOkClickListener(final TripDto trip) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DeleteTripTask(getContext(), TripFragmentPage.this, items).execute(trip);
            }
        };
    }
}
