package com.example.revent.fragments;

import static java.nio.file.Paths.get;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.activities.EditProfileActivity;
import com.example.revent.activities.SplashActivity;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsFragment extends Fragment {
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settings, container, false);
        auth = FirebaseAuth.getInstance();
        View externalView = inflater.inflate(R.layout.fragment_settings, container, false);
        CircleImageView circleImageView = externalView.findViewById(R.id.profile_image);
        TextView name_textView = externalView.findViewById(R.id.profile_name);
        TextView surname_textView = externalView.findViewById(R.id.profile_surname);
        TextView email_textView = externalView.findViewById(R.id.profile_email);


        String uid = new FireBaseWrapper.Auth().getUid();


        // Set the image resource for the CircleImageView
        //circleImageView.setImageResource(R.drawable.usa);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images_profile/"+uid+"/image_profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024*5;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                circleImageView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getActivity(), "Failed to download",Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////////////////////////

        DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        if(ref == null) {
            Toast
                    .makeText(getActivity(), "User not found", Toast.LENGTH_SHORT)
                    .show();

        }

        if(uid == null) {
            Toast
                    .makeText(getActivity(), "User not found", Toast.LENGTH_SHORT)
                    .show();
        }


        ref.child(uid).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    //Map<String, User> usermap = (HashMap<String, User>) dataSnapshot.getValue();
                    //String value = String.valueOf(usermap.get( "email" ));
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("Water_level", "Value is: " + user.getEmail());
                    email_textView.setText(user.getEmail());
                    name_textView.setText(user.getName());
                    surname_textView.setText(user.getSurname());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast
                        .makeText(getActivity(), "Unable to find the credentials", Toast.LENGTH_SHORT)
                        .show();
            }
        } );
        /*------------------------

        ref.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    Map<Long, User> dbUsers = new HashMap<>();

                    // Iterate over the db objects

                    for(DataSnapshot child : task.getResult().getChildren()) {
                        User user = child.getValue(User.class);
                        dbUsers.put(ref.get(uid), user);
                    }
                }
            }
        });

        */
        /////////////////////////////////////


        // Set the text for the TextView
       // name_textView.setText("Settings Fragment");


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
        Button edtprofbutton = externalView.findViewById(R.id.edit_profile_button);

        edtprofbutton.setOnClickListener(new View.OnClickListener() {
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


                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                getActivity().finish();

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
            getActivity().finish();
        }
    }

}