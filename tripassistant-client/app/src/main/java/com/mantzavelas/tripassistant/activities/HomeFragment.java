package com.mantzavelas.tripassistant.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.models.CurrentUser;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button loginButton;
    private Button registerButton;

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

        loginButton = view.findViewById(R.id.home_login_btn);
        registerButton = view.findViewById(R.id.home_register_btn);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);


        hideLoginButtons();
    }

    private void hideLoginButtons() {
        if(loginButton != null && registerButton != null && CurrentUser.isLoggedIn()) {
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
}
