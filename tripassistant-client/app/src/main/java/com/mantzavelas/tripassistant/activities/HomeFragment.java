package com.mantzavelas.tripassistant.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.activities.listeners.NotifyDataSetChanged;
import com.mantzavelas.tripassistant.adapters.PlaceDtoAdapter;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.restservices.tasks.GetSeasonPlacesTask;
import com.mantzavelas.tripassistantapi.dtos.PlaceDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, NotifyDataSetChanged {

    private Button loginButton;
    private Button registerButton;
    private RecyclerView seasonPlacesRecyclers;

    private PlaceDtoAdapter placesAdapter;
    private List<PlaceDto> places;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideLoginButtons();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seasonPlacesRecyclers = view.findViewById(R.id.home_season_recycler);
        loginButton = view.findViewById(R.id.home_login_btn);
        registerButton = view.findViewById(R.id.home_register_btn);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);


        places = new ArrayList<>();
        placesAdapter = new PlaceDtoAdapter(places);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        seasonPlacesRecyclers.setLayoutManager(layoutManager);
        seasonPlacesRecyclers.setItemAnimator(new DefaultItemAnimator());
        seasonPlacesRecyclers.setAdapter(placesAdapter);

        new GetSeasonPlacesTask(this)
                .execute(new Pair<>(CurrentUser.getInstance().getLatitude(), CurrentUser.getInstance().getLongitude()));
        hideLoginButtons();
    }

    private void hideLoginButtons() {
        if(loginButton != null && registerButton != null && CurrentUser.getInstance().isLoggedIn()) {
            loginButton.setVisibility(View.INVISIBLE);
            registerButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        switch (v.getId()) {
            case R.id.home_login_btn:
                fragment = new LoginFragment();
                replaceFragment(fragment);
                break;
            case R.id.home_register_btn:
                fragment = new RegisterFragment();
                replaceFragment(fragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.home_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDataSetChanged(Collection<?> dataSet) {
        this.places = (List<PlaceDto>) dataSet;
        placesAdapter.setDataset(places);
        placesAdapter.notifyDataSetChanged();
    }
}
