package com.example.revent.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.revent.R;
import com.example.revent.activities.EnterActivity;
import com.example.revent.models.FireBaseWrapper;

public class LoginFragment extends LogFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View externalView = inflater.inflate(R.layout.fragment_login, container, false);

        TextView link = externalView.findViewById(R.id.switchLoginToRegisterLabel);

        link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((EnterActivity)LoginFragment.this.requireActivity()).renderFragment(false);


            }
        });

        Button button = externalView.findViewById(R.id.logButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = externalView.findViewById(R.id.userEmail);
                EditText password = externalView.findViewById(R.id.userPassword);

                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {

                    email.setError("Email is required");
                    password.setError("Password is required");
                    return;
                }

                // Perform SignIn
                FireBaseWrapper.Auth auth = new FireBaseWrapper.Auth();
                auth.signIn(
                        email.getText().toString(),
                        password.getText().toString(),
                        FireBaseWrapper.Callback
                                .newInstance(LoginFragment.this.requireActivity(),
                                        LoginFragment.this.callbackName,
                                        LoginFragment.this.callbackPrms)
                );
            }
        });

        return externalView;
    }
}