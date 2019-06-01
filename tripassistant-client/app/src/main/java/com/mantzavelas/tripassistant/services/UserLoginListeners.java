package com.mantzavelas.tripassistant.services;

import com.mantzavelas.tripassistant.activities.listeners.UserLoggedInListener;

import java.util.ArrayList;
import java.util.List;

public enum UserLoginListeners {

    INSTANCE;

    private List<UserLoggedInListener> listeners = new ArrayList<>();

    public void addListener(UserLoggedInListener listener) {
        listeners.add(listener);
    }

    public void removeListener(UserLoggedInListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for(UserLoggedInListener listener : listeners) {
            listener.userLoggedIn();
        }
    }
}
