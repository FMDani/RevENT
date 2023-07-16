package com.example.revent.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.revent.R;
import com.example.revent.activities.EnterActivity;
import com.example.revent.activities.MainActivity;
import com.example.revent.activities.SplashActivity;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public class SignUpFragment extends LogFragment {

    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View externalView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        TextView link = externalView.findViewById(R.id.switchRegisterToLoginLabel);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EnterActivity) SignUpFragment.this.requireActivity()).renderFragment(true);
            }
        });

        Button button = externalView.findViewById(R.id.logButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = externalView.findViewById(R.id.userEmail);
                EditText name = externalView.findViewById(R.id.userName);
                EditText surname = externalView.findViewById(R.id.userSurname);
                EditText password = externalView.findViewById(R.id.userPassword);
                EditText password2 = externalView.findViewById(R.id.userPasswordAgain);


                if (email.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty() ||
                        password2.getText().toString().isEmpty()) {
                    // TODO: Better error handling + remove this hardcoded strings
                    email.setError("Email is required");
                    password.setError("Password is required");
                    password2.setError("Password is required");
                    return;
                }

                if (!password.getText().toString().equals(password2.getText().toString())) {
                    // TODO: Better error handling + remove this hardcoded strings
                    Toast
                            .makeText(SignUpFragment.this.requireActivity(), "Passwords are different", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                //TODO check on name and surname


                // Perform SignIn
                /*
                FireBaseWrapper.Auth auth = new FireBaseWrapper.Auth();
                auth.signUp(
                        email.getText().toString(),
                        password.getText().toString(),
                        FireBaseWrapper.Callback
                                .newInstance(SignUpFragment.this.requireActivity(),
                                        SignUpFragment.this.callbackName,
                                        SignUpFragment.this.callbackPrms)
                );

                 */
                auth = FirebaseAuth.getInstance();
                
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(!task.isSuccessful()) {
                                    // TODO: Better handling of the error message --> Put in a textview of the activity
                                    Toast
                                            .makeText(getActivity(), "Username or password are not valid", Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
                                    DatabaseReference ref_token = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tokens");
                                    if(ref == null) {
                                        return;
                                    }

                                    String uid = new FireBaseWrapper.Auth().getUid();


                                    if(uid == null) {
                                        Toast
                                                .makeText(getActivity(), "User not found", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    // Add new token for device


                                    FirebaseMessaging.getInstance().getToken()
                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                @Override
                                                public void onComplete(@NonNull Task<String> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                                        return;
                                                    }


                                                    // Get new FCM registration token
                                                    String token = task.getResult();

                                                    ref_token.child(uid).setValue(token);

                                                    // toast
                                                    Toast.makeText(getActivity(),"new FCM registration token " + token, Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            });


                                    DatabaseReference ref_user = ref.child(uid);
                                    User user = new User(email.getText().toString(), name.getText().toString(), surname.getText().toString());
                                    ref_user.setValue(user);
                                    // Go To Splash to check the permissions
                                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                                //callback.invoke(task.isSuccessful());
                            /*
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());


                            }
                            */

                            }
                        });


            }
        });

        return externalView;
    }
}