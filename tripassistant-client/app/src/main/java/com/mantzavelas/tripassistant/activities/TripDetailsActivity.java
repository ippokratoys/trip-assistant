package com.mantzavelas.tripassistant.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.adapters.TripDetailsAdapter;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;
import com.mantzavelas.tripassistant.restservices.tasks.DeleteTripTask;

public class TripDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripDetailsAdapter adapter;

    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateScheduledTextView;
    private TextView totalPlacesTextView;
    private AppCompatButton deleteButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        titleTextView = findViewById(R.id.trip_details_title_text);
        descriptionTextView = findViewById(R.id.trip_details_description_text);
        dateScheduledTextView = findViewById(R.id.trip_details_date);
        totalPlacesTextView = findViewById(R.id.trip_details_total_places);
        recyclerView = findViewById(R.id.trip_details_recycler);
        deleteButton = findViewById(R.id.trip_details_delete_btn);

        TripDto trip = (TripDto) getIntent().getSerializableExtra("TRIP");
        adapter = new TripDetailsAdapter(trip.getPlaces());

        deleteButton.setOnClickListener(getDeleteClickListener(trip));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        titleTextView.append(trip.getTitle());
        descriptionTextView.append(trip.getDescription());
        dateScheduledTextView.append(trip.getScheduledFor().toString());
        totalPlacesTextView.append(String.valueOf(trip.getPlaces().size()));
    }

    private View.OnClickListener getDeleteClickListener(final TripDto tripDto) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTripTask task = new DeleteTripTask(TripDetailsActivity.this);
                task.setNeedsTermination(true);
                task.execute(tripDto);
            }
        };
    }
}
