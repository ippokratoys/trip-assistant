package com.mantzavelas.tripassistant.adapters;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.restservices.dtos.TripDto;

import java.lang.ref.WeakReference;
import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {

    private List<TripDto> trips;
    private RecyclerViewClickListener listener;

    private static final String TITLE_TOKEN = "Title: ";
    private static final String DESCRIPTION_TOKEN = "Description: ";
    private static final String DATE_SCHEDULED_TOKEN = "Date Scheduled: ";
    private static final String TOTAL_PLACES_TOKEN = "Places: ";

    public TripsAdapter(List<TripDto> trips, RecyclerViewClickListener listener) {
        this.trips = trips;
        this.listener = listener;
    }

    public void setDataSet(List<TripDto> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.row_place_list, viewGroup, false);

        return new TripViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder tripViewHolder, int i) {
        TripDto trip = trips.get(i);
        tripViewHolder.setTitle(constructBoldText(TITLE_TOKEN, trip.getTitle()));
        tripViewHolder.setDescription(constructBoldText(DESCRIPTION_TOKEN, trip.getDescription()));
        tripViewHolder.setDateScheduled(constructBoldText(DATE_SCHEDULED_TOKEN, trip.getScheduledFor().toString()));
        tripViewHolder.setTotalPlaces(constructBoldText(TOTAL_PLACES_TOKEN, String.valueOf(trip.getPlaces().size())));
    }

    private SpannableStringBuilder constructBoldText(String bold, String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(bold);

        if (text == null) {
            return builder;
        }

        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, bold.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.append(text);

        return builder;
    }

    @Override
    public int getItemCount() { return trips.size(); }

    static class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private TextView description;
        private TextView dateScheduled;
        private TextView totalPlaces;
        private Button detailsButton;
        private Button viewOnMapButton;
        private AppCompatImageButton deleteButton;
        private WeakReference<RecyclerViewClickListener> clickListenerRef;

        public TextView getTitle() { return title; }
        public void setTitle(SpannableStringBuilder title) { this.title.setText(title); }

        public TextView getDescription() { return description; }
        public void setDescription(SpannableStringBuilder description) { this.description.setText(description); }

        public TextView getDateScheduled() { return dateScheduled; }
        public void setDateScheduled(SpannableStringBuilder dateScheduled) { this.dateScheduled.setText(dateScheduled); }

        public TextView getTotalPlaces() { return totalPlaces; }
        public void setTotalPlaces(SpannableStringBuilder totalPlaces) { this.totalPlaces.setText(totalPlaces); }

        public TripViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.row_place_title);
            description = itemView.findViewById(R.id.row_place_description);
            dateScheduled = itemView.findViewById(R.id.row_place_date_scheduled);
            totalPlaces = itemView.findViewById(R.id.row_place_total_places);
            detailsButton = itemView.findViewById(R.id.row_place_details_btn);
            viewOnMapButton = itemView.findViewById(R.id.row_place_view_on_map_btn);
            deleteButton = itemView.findViewById(R.id.row_place_delete_btn);
            clickListenerRef = new WeakReference<>(listener);

            detailsButton.setOnClickListener(this);
            viewOnMapButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListenerRef.get().onPositionClicked(getAdapterPosition(), v.getId());
        }
    }

    public interface RecyclerViewClickListener {
        void onPositionClicked(int position, int componentId);
    }
}
