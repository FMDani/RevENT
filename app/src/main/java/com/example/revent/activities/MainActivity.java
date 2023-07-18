package com.example.revent.activities;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.revent.R;
import com.example.revent.fragments.HomeFragment;
import com.example.revent.fragments.NotificationFragment;
import com.example.revent.fragments.MyProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    MyProfileFragment myProfileFragment = new MyProfileFragment();
    NotificationFragment notificationFragment = new NotificationFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView  = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.notification) {
                    // Create an Intent object
                    Intent intent = new Intent(MainActivity.this, AddEvent.class);
                    startActivity(intent);
                    finish();
                } else if (itemId == R.id.settings) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, myProfileFragment).commit();
                    return true;
                }


                return false;
            }
        });

    }

}