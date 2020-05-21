package com.mantzavelas.tripassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistantapi.dtos.PlaceDto;

import java.util.List;

public class PlaceDtoAdapter extends RecyclerView.Adapter<PlaceDtoAdapter.ViewHolder> {

    private List<PlaceDto> places;

    public PlaceDtoAdapter(List<PlaceDto> places) { this.places = places; }

    public void setDataset(List<PlaceDto> places) {
        this.places.clear();
        this.places.addAll(places);
    }

    @NonNull
    @Override
    public PlaceDtoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_place_with_image, viewGroup, false);

        return new PlaceDtoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceDtoAdapter.ViewHolder viewHolder, int i) {
        PlaceDto placeDto = places.get(i);

        viewHolder.title.setText(placeDto.getTitle());
        Glide.with(viewHolder.itemView)
                .load(placeDto.getPhotoUrls().get(0))
                .apply(new RequestOptions().override(300, 300))
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() { return places.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView image;
        private Button addButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.row_image_place_title);
            image = itemView.findViewById(R.id.row_image_place_image);
            addButton = itemView.findViewById(R.id.row_image_place_button);
        }
    }
}
