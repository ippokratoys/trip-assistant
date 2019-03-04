package com.mantzavelas.tripassistant.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {

    public static final int REQUEST_PERMISSION_LOCATION = 0;

    public static boolean checkAndRequestLocationPermissions(Activity activity) {

        int fineLocationPerm = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPerm = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // Permission List
        List<String> listPermissionsNeeded = new ArrayList<>();

        // Coarse Location Permission
        if (fineLocationPerm != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(activity, "Location Permission is required for map related tabs", Toast.LENGTH_SHORT).show();
            }
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // Location Permission
        if (coarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(activity, "Location Permission is required for map related tabs", Toast.LENGTH_SHORT).show();
            }
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[0]),
                    REQUEST_PERMISSION_LOCATION);
            return false;
        }

        return true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
