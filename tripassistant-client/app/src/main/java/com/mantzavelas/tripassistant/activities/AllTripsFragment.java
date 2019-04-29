package com.mantzavelas.tripassistant.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.activities.helpers.SimpleDialogBuilder;
import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.adapters.TripsAdapter;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;
import com.mantzavelas.tripassistant.restservices.tasks.DeleteTripTask;
import com.mantzavelas.tripassistant.restservices.tasks.GetTripsTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllTripsFragment extends Fragment implements NotifyDataSetChanged {

    private static final int SELECT_PLACES_REQUEST = 1000;

    private RecyclerView recyclerView;
    private FloatingActionButton newTripFab;

    private TripsAdapter adapter;
    private List<TripDto> trips;

    public AllTripsFragment() {
        trips = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.all_trips_recyclerview);
        newTripFab = view.findViewById(R.id.all_trips_fab);

        newTripFab.setOnClickListener(fabClickListener);

        adapter = new TripsAdapter(trips, recyclerViewRowListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        new GetTripsTask(getContext(), this).execute();
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent newTripIntent = new Intent(getContext(), NewTripActivity.class);
            startActivityForResult(newTripIntent, SELECT_PLACES_REQUEST);
        }
    };

    TripsAdapter.RecyclerViewClickListener recyclerViewRowListener = new TripsAdapter.RecyclerViewClickListener() {
        @Override
        public void onPositionClicked(int position, int componentId) {
            switch (componentId) {
                case R.id.row_place_details_btn :
                    Intent intent = new Intent(getContext(), TripDetailsActivity.class);
                    intent.putExtra("TRIP", trips.get(position));
                    startActivity(intent);
                    break;
                case R.id.row_place_view_on_map_btn :
                    //do that
                    break;
                case R.id.row_place_delete_btn :
                    String title = "Delete trip " + trips.get(position).getTitle();
                    String message = "Are you sure you want to delete this trip?";
                    SimpleDialogBuilder dialogBuilder = new SimpleDialogBuilder(getContext(), title, message);
                    dialogBuilder.addDefaultButtonListeners(getOkClickListener(trips.get(position)), null);
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
                new DeleteTripTask(getContext(), AllTripsFragment.this, trips).execute(trip);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PLACES_REQUEST && resultCode == Activity.RESULT_OK) {
            TripDto trip = (TripDto) data.getExtras().getSerializable(NewTripActivity.PLACES_EXTRA);

            trips.add(trip);
            adapter.setDataSet(trips);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDataSetChanged(Collection<?> dataSet) {
        this.trips = (List<TripDto>) dataSet;
        this.adapter.setDataSet(trips);
        this.adapter.notifyDataSetChanged();
    }

}