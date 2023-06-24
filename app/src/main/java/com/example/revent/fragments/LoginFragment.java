package com.example.revent.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.revent.R;
import com.example.revent.activities.EnterActivity;
import com.example.revent.models.FireBaseWrapper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends LogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "method_name";
    private static final String ARG_PARAM2 = "method_prms";

    // TODO: Rename and change types of parameters
    /*
    private String mParam1;
    private String mParam2;
    */
    public LoginFragment() {
        // Required empty public constructor
    }


    private String callbackName;
    private Class[] callbackPrms;
    public static LoginFragment newInstance(String param1, Class<?>... prms) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, prms);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.callbackName = getArguments().getString(ARG_PARAM1);
            this.callbackPrms = (Class[]) getArguments().getSerializable(ARG_PARAM2);
        }
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
                    // TODO: Remove this hardcoded strings
                    email.setError("Email is required");
                    password.setError("Password is required");
                    return;
                }

                // Perform SignIn
                FireBaseWrapper.Auth auth = new FireBaseWrapper.Auth();
                // TODO: accept as input also the list of parameters
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