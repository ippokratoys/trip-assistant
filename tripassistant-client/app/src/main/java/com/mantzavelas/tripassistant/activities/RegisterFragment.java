package com.mantzavelas.tripassistant.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;
import com.mantzavelas.tripassistant.restservices.resources.UserResource;

import java.io.IOException;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText firstNameET;
    private EditText lastNameET;
    private EditText usernameET;
    private EditText passwordET;
    private Button registerButton;
    private TripAssistantService service;

    public RegisterFragment() {
        service = RestClient.create().getTripAssistantService();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameET = view.findViewById(R.id.register_firstname_editText);
        lastNameET = view.findViewById(R.id.register_lastname_editText);
        usernameET = view.findViewById(R.id.register_username_editText);
        passwordET = view.findViewById(R.id.register_password_editText);
        registerButton = view.findViewById(R.id.register_register_btn);
        registerButton.setOnClickListener(this);
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
        RegisterTask registerTask = new RegisterTask();
        registerTask.execute(firstNameET.getText().toString()
                , lastNameET.getText().toString()
                , usernameET.getText().toString()
                , passwordET.getText().toString());
    }

    private class RegisterTask extends AsyncTask<String, Void ,Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            if(strings.length == 4) {
                UserResource userResource = new UserResource.Builder().withFirstName(strings[0])
                        .withLastName(strings[1])
                        .withUsername(strings[2])
                        .withPassword(strings[3])
                        .build();

                String authToken;
                try {
                    authToken = service.register(userResource).execute().body().getAuthToken();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                CurrentUser.getInstance().setRegisteredUser(userResource.getFirstName(), userResource.getLastName()
                        , userResource.getUsername(), authToken);
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean) {
                Toast.makeText(getActivity(), "Successfully registered!", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                return;
            }

            Toast.makeText(getActivity(), "Your last activity was not completed. Please try again later", Toast.LENGTH_SHORT).show();
        }
    }

}
