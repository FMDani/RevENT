package com.example.revent.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;


import com.example.revent.R;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.PermissionManager;

public class SplashActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private void goToActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        this.startActivity(intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // check  Calendar permissions

        PermissionManager pm = new PermissionManager(this);
        if(!pm.askNeededPermission(PERMISSION_REQUEST_CODE, false)) {
            //Go to MainActivity
            //this.goToActivity(MainActivity.class);
            FireBaseWrapper.Auth auth = new FireBaseWrapper.Auth();
            boolean test = auth.isAuthenticated();
            if(!auth.isAuthenticated()) {
                // go to Activity for login or signup
                this.goToActivity(EnterActivity.class);
            }else{
                this.goToActivity(MainActivity.class);
            }

        }

        // Add the listeners
        Button buttonGrantPerm = (Button) this.findViewById(R.id.grantPermission);

        buttonGrantPerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pm.askNeededPermission(PERMISSION_REQUEST_CODE, true)) {
                    //Go to MainActivity
                    SplashActivity.this.goToActivity(SplashActivity.class);

                }

            }
        });

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