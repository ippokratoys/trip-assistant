package com.mantzavelas.tripassistant.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mantzavelas.tripassistant.R;
import com.mantzavelas.tripassistant.models.CurrentUser;
import com.mantzavelas.tripassistant.models.User;
import com.mantzavelas.tripassistant.restservices.RestClient;
import com.mantzavelas.tripassistant.restservices.TripAssistantService;
import com.mantzavelas.tripassistant.restservices.dtos.UserTokenDto;
import com.mantzavelas.tripassistant.restservices.resources.UserCredentialResource;
import com.mantzavelas.tripassistant.services.UserLoginListeners;

import java.io.IOException;

import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText usernameET;
    private EditText passwordET;
    private Button loginButton;
    private TripAssistantService service;

    public LoginFragment() {
        service = RestClient.create().getTripAssistantService();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameET = view.findViewById(R.id.login_username_editText);
        passwordET = view.findViewById(R.id.login_password_editText);
        loginButton = view.findViewById(R.id.login_login_btn);
        loginButton.setOnClickListener(this);
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
        LoginTask loginTask = new LoginTask();
        loginTask.execute(usernameET.getText().toString(), passwordET.getText().toString());
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            if(strings.length == 2) {
                UserCredentialResource resource = new UserCredentialResource(strings[0], strings[1]);

                Response<UserTokenDto> response;
                try {
                    response = service.login(resource).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                if (!response.isSuccessful()) {
                    return false;
                }

                User user = new User();
                user.setUsername(strings[0]);
                user.setAccessToken(response.body().getAuthToken());
                CurrentUser.getInstance().setLoggedInUser(user.getUsername(), user.getAccessToken());
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                Toast.makeText(getActivity(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_body, new HomeFragment(), "HomeFragment")
                        .commit();
                getFragmentManager().popBackStack();
                UserLoginListeners.INSTANCE.notifyListeners();
                return;
            }

            Toast.makeText(getActivity(), "Invalid username and/or password", Toast.LENGTH_SHORT).show();
            usernameET.setText("");
            passwordET.setText("");
        }
    }
}
