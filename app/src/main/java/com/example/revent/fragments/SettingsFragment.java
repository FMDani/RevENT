package com.example.revent.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.activities.EnterActivity;
import com.example.revent.activities.MainActivity;
import com.example.revent.activities.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settings, container, false);
        View externalView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button sgnbutton = externalView.findViewById(R.id.sgnoutbut);

        //sgnbutton = (Button)
        sgnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO refactor as a callback
                /*
                FireBaseWrapper.Auth auth = new FireBaseWrapper.Auth();
                auth.signOut(
                        FireBaseWrapper.Callback
                                .newInstance(MainActivity.class,
                                        "signinCallback",
                                        boolean.class)
                );
                 */


                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                signinCallback(true);

            }
        });
        return externalView;
    }
    public void signinCallback(boolean result) {
        if(!result) {
            // TODO: Better handling of the error message --> Put in a textview of the activity
            Toast
                    .makeText(getActivity(), "unable to signOut", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast
                    .makeText(getActivity(), "SignOut", Toast.LENGTH_SHORT)
                    .show();
            // Go To Splash to check the permissions
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            startActivity(intent);
        }
    }

}