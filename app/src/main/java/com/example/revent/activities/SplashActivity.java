package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.revent.R;
import com.example.revent.models.PermissionManager;

public class SplashActivity extends AppCompatActivity {

    private void goToActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //TODO : check user
        // if  not --> log

        //TODO: check permissions

        if(!new PermissionManager(this).askNeededPermission(1)) {
            //Go to MainActivity
            this.goToActivity(MainActivity.class);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int res : grantResults) {
            if(res == PackageManager.PERMISSION_DENIED) {
                return;
            }

        }

        //Go to MainActivity
        this.goToActivity(MainActivity.class);
    }
}