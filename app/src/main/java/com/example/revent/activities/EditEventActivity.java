package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.models.FireBaseWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditEventActivity extends AppCompatActivity {
    final int PICK_FROM_GALLERY = 1;
    private static int RESULT_LOAD_IMAGE = 1;

    Uri selectedImage;

    boolean upload_image_button = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        final ImageView backBtn = findViewById(R.id.backBtn);
        ImageButton modify_eventimage = findViewById(R.id.modify_eventimage);
        Button modifyevent_button = findViewById(R.id.modifyevent_button);

        modify_eventimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                try {
                    if (ActivityCompat.checkSelfPermission(EditEventActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditEventActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        upload_image_button = true;
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        modifyevent_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(upload_image_button) {
                    uploadImage();
                }

                EditText nameEvent= findViewById(R.id.nameEvent);
                EditText namePlace= findViewById(R.id.namePlace);
                EditText nameDescription= findViewById(R.id.nameDescription);
                EditText startDate= findViewById(R.id.startDate);
                EditText endDate= findViewById(R.id.endDate);
                EditText duration= findViewById(R.id.duration);

                DatabaseReference events_ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");
                 String getEventId = getIntent().getStringExtra("EventId");
                if (!nameEvent.getText().toString().isEmpty()){
                    events_ref.child(getEventId).child("title").setValue(nameEvent.getText().toString());
                }
                if (!namePlace.getText().toString().isEmpty()){
                    events_ref.child(getEventId).child("place").setValue(namePlace.getText().toString());
                }
                if (!nameDescription.getText().toString().isEmpty()){
                    events_ref.child(getEventId).child("descr").setValue(nameDescription.getText().toString());
                }
                if (!startDate.getText().toString().isEmpty()){
                    events_ref.child(getEventId).child("dtStart").setValue(startDate.getText().toString());
                }
                if (!endDate.getText().toString().isEmpty()){
                    events_ref.child(getEventId).child("dtEnd").setValue(endDate.getText().toString());
                }
                if (!duration.getText().toString().isEmpty()){
                    events_ref.child(getEventId).child("dur").setValue(duration.getText().toString());
                }

                Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    String uid = new FireBaseWrapper.Auth().getUid();
    private void uploadImage() {

        String getEventId = getIntent().getStringExtra("EventId");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images_events/"+getEventId);

        storageReference.child(".image_event.jpg").putFile(selectedImage).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditEventActivity.this, "Successfully Uploaded",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditEventActivity.this, "Failed to Uploaded",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imagefromgallery_event);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }


}