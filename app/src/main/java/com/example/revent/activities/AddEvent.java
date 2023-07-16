package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.revent.models.MyEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    Uri selectedImage;

    final int PICK_FROM_GALLERY = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    String uid = new FireBaseWrapper.Auth().getUid();

    boolean upload_image_button = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        final ImageView backBtn = findViewById(R.id.backBtn_event);

        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aggiungi qui la logica da eseguire quando viene cliccato l'ImageButton
                // Per esempio, puoi avviare un'azione o mostrare un messaggio
                upload_image_button = true;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        EditText editTextNameEvent = findViewById(R.id.editTextText);
        EditText editTextPlace = findViewById(R.id.editTextText2);
        EditText editTextDescription = findViewById(R.id.editTextText6);
        EditText editTextStartDate = findViewById(R.id.editTextText3);
        EditText editTextEndDate = findViewById(R.id.editTextText4);
        EditText editTextDuration = findViewById(R.id.editTextText5);

        Button buttonCreateEvent = findViewById(R.id.button2);

        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform actions when the button is clicked
                // Add your code logic here
                DatabaseReference rootRef = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");
                if(uid == null) {
                    Toast
                            .makeText(AddEvent.this, "User not found", Toast.LENGTH_SHORT)
                            .show();
                }

                String title = editTextNameEvent.getText().toString();
                String place = editTextPlace.getText().toString();
                String descr = editTextDescription.getText().toString();
                String dtStart = editTextStartDate.getText().toString();
                String dtEnd = editTextEndDate.getText().toString();
                String dur = editTextDuration.getText().toString();
                String key = rootRef.push().getKey();
                MyEvent event = new MyEvent(key, title, place, descr, dtStart, dtEnd, dur, uid, new ArrayList<>());


                DatabaseReference ref_events = rootRef.child(key);

                ref_events.setValue(event);

                if(upload_image_button){
                    uploadImage(key);
                }

                Intent intent = new Intent(AddEvent.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEvent.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void uploadImage(String event_id) {



        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images_events/"+event_id);

        storageReference.child(".image_event.jpg").putFile(selectedImage).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(AddEvent.this, "Successfully Uploaded",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddEvent.this, "Failed to Uploaded",Toast.LENGTH_SHORT).show();
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

            ImageView imageView = (ImageView) findViewById(R.id.imageView5);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
}