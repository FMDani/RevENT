package com.example.revent.models;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

    private final static String[] NEEDED_PERMISSIONS = {Manifest.permission.READ_CALENDAR};

    private final static String TAG = PermissionManager.class.getCanonicalName();

    private final Activity activity;


    public PermissionManager(Activity activity) {
        this.activity = activity;
    }

    public boolean askNeededPermission(int requestCode) {
        List<String> missingPermissions = new ArrayList<>();

        for (String permission : NEEDED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this.activity, permission) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission "+ permission + " is granted by the user.");
                continue;

            }/* else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            showInContextUI(...);
        }*/ else {
                missingPermissions.add(permission);
            }
        }
        if(missingPermissions.isEmpty()) {
            return false;
        }

        ActivityCompat.requestPermissions(this.activity, missingPermissions.toArray(new String[missingPermissions.size()]), requestCode);
        return true;
    }

}
