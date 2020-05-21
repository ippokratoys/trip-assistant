package com.mantzavelas.tripassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.models.Place;

import java.util.List;

public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsAdapter.ViewHolder> {

    private List<Place> places;

    public TripDetailsAdapter(List<Place> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public TripDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_place_thumbnail, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripDetailsAdapter.ViewHolder viewHolder, int i) {
        Place place = places.get(i);

        viewHolder.title.setText(place.getTitle());
        Glide.with(viewHolder.itemView)
            .load(place.getPhotoUrls().get(0))
            .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.row_place_thumbnail_title);
            thumbnail = itemView.findViewById(R.id.row_place_thumbnail_img);
        }
    }
}
