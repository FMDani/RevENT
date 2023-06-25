package com.example.revent.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.fragments.LogFragment;
import com.example.revent.fragments.LoginFragment;
import com.example.revent.fragments.SignUpFragment;

public class EnterActivity extends AppCompatActivity {

    private static final String TAG = EnterActivity.class.getCanonicalName();


    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        // Render fragment
        renderFragment(true);
    }

    public void renderFragment(boolean isLogin) {

        Fragment fragment = null;

        if(isLogin) {
            fragment = LogFragment.newInstance(LoginFragment.class,"signinCallback", boolean.class);
        }else {
            fragment = LogFragment.newInstance(SignUpFragment.class, "signinCallback", boolean.class);
        }





        if(this.fragmentManager == null) {
            this.fragmentManager = this.getSupportFragmentManager();
        }

        FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();

        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.replace(R.id.loginRegisterFragment, fragment);

        fragmentTransaction.commit();
    }


    public void signinCallback(boolean result) {
        if(!result) {
            // TODO: Better handling of the error message --> Put in a textview of the activity
            Toast
                    .makeText(this, "Username or password are not valid", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // Go To Splash to check the permissions
            Intent intent = new Intent(this, SplashActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }
}