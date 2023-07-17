package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        final ImageView backBtn_showprofile = findViewById(R.id.backBtn_showprf);
        CircleImageView show_profile_img = findViewById(R.id.show_profile_img);
        final TextView showprofile_name= findViewById(R.id.showprofile_name);
        final TextView showprofile_surname= findViewById(R.id.showprofile_surname);
        final TextView showprofile_email= findViewById(R.id.showprofile_email);


        final String getProfileId = getIntent().getStringExtra("getCreatorId");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images_profile/"+getProfileId+"/image_profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024*5;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                show_profile_img.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(ShowProfileActivity.this, "Failed to download",Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        ref.child(getProfileId).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){

                    User user = dataSnapshot.getValue(User.class);
                    showprofile_email.setText(user.getEmail());
                    showprofile_name.setText(user.getName());
                    showprofile_surname.setText(user.getSurname());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast
                        .makeText(ShowProfileActivity.this, "Unable to find the credentials", Toast.LENGTH_SHORT)
                        .show();
            }
        } );

        backBtn_showprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}