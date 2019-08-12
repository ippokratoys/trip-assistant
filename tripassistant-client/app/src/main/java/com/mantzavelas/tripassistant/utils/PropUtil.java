package com.mantzavelas.tripassistant.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

public class PropUtil {

    private static Properties properties;

    private static Context context;
    public static Context getContext() {
        return context;
    }

    public static void initProperties(Context cont) {
        if (context == null) {
            context = cont;
        }
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(context.getAssets().open("app.properties"));
            } catch (IOException e) {
                Log.e(PropUtil.class.getCanonicalName(), "Failed to load app.properties", e);
            }
        }
    }

    public static String getProperty(String name) {
        if (context == null || properties == null) {
            return null;
        }
        return properties.getProperty(name);
    }

    public static String getProperty(String name, String defaultValue) {
        if (context == null || properties == null) {
            return null;
        }
        return properties.getProperty(name, defaultValue);
    }
}
