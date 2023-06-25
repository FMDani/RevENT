package com.example.revent.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.fragments.LoginFragment;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.MyEvent;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireBaseWrapper.RTDatabase db = new FireBaseWrapper.RTDatabase();
        db.writeDbData(new MyEvent(10, "title", "descrizione", 11L, 12L, "£"));

        Button sgnbutton;

        sgnbutton = (Button) findViewById(R.id.signoutbutton);
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


    }

    public void signinCallback(boolean result) {
        if(!result) {
            // TODO: Better handling of the error message --> Put in a textview of the activity
            Toast
                    .makeText(this, "unable to signOut", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // Go To Splash to check the permissions
            Intent intent = new Intent(this, EnterActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }
}