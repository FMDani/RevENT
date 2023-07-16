package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.adapters.ReservationAdapter;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.Reservation;
import com.example.revent.models.ReservationList;
import com.example.revent.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShowEventActivity extends AppCompatActivity {


    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);



        final ImageView backBtn = findViewById(R.id.backBtn);
        final ImageView card_image = findViewById(R.id.card_image);
        final TextView nameEvent= findViewById(R.id.nameEvent);
        final TextView placeEvent= findViewById(R.id.placeEvent);
        final TextView dateEventStart= findViewById(R.id.dateEventStart);
        final TextView dateEventEnd= findViewById(R.id.dateEventEnd);
        final TextView hourEvent= findViewById(R.id.hourEvent);
        final TextView descrEvent= findViewById(R.id.descrEvent);
        final TextView creatorEvent= findViewById(R.id.creatorEvent);
        final Button btn_show_edt = findViewById(R.id.btn_show_edt);
        final Button reservation_button = findViewById(R.id.reservation_button);


        // get data from myeventadapter class

        final String getEventId = getIntent().getStringExtra("eventId");

        StorageReference imageRef = storageRef.child("images_events/"+getEventId+"/.image_event.jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 3;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                card_image.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        nameEvent.setText(getIntent().getStringExtra("title"));
        placeEvent.setText(getIntent().getStringExtra("place"));
        dateEventStart.setText(getIntent().getStringExtra("dtStart"));
        dateEventEnd.setText(getIntent().getStringExtra("dtEnd"));
        hourEvent.setText(getIntent().getStringExtra("dur"));
        descrEvent.setText(getIntent().getStringExtra("descr"));
        String getCreatorId = getIntent().getStringExtra("userCreatorId");


        DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        String name_creator;

        ref.child(getCreatorId).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    //Map<String, User> usermap = (HashMap<String, User>) dataSnapshot.getValue();
                    //String value = String.valueOf(usermap.get( "email" ));
                    User user = dataSnapshot.getValue(User.class);


                    creatorEvent.setText(user.getName()+" "+user.getSurname());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast
                        .makeText(ShowEventActivity.this, "Unable to find the credentials", Toast.LENGTH_SHORT)
                        .show();
            }
        } );


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowEventActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String uid = new FireBaseWrapper.Auth().getUid();

        if(uid.equals(getCreatorId)) {
            btn_show_edt.setText("Edit Event");
            reservation_button.setText("booking for this Event");
            btn_show_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ShowEventActivity.this, EditEventActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            reservation_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ShowEventActivity.this, OwnerReservationsActivity.class);
                    intent.putExtra("EventId",getEventId);
                    startActivity(intent);
                    finish();



                }
            });

        }else {
            btn_show_edt.setText("Show Profile");
            btn_show_edt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowEventActivity.this, ShowProfileActivity.class);
                    intent.putExtra("getCreatorId",getCreatorId);
                    startActivity(intent);
                    finish();
                }
            });
            reservation_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar cal = Calendar.getInstance();

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String reservationDate = dateFormat.format(cal.getTime());



                    DatabaseReference reservations_ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reservations");


                    reservations_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()) {
                                // Il nodo ha figli
                                // Puoi eseguire le operazioni desiderate

                                boolean hasBookedOtherEvents = false;
                                boolean hasBookedCurrentEvent = false;

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (dataSnapshot.child("idClient").getValue(String.class).equals(uid)) {
                                        if (!dataSnapshot.child("idEvent").getValue(String.class).equals(getEventId)) {
                                            hasBookedOtherEvents = true;
                                        } else {
                                            hasBookedCurrentEvent = true;
                                            break;
                                        }
                                    }
                                }

                                if (hasBookedCurrentEvent) {
                                    Toast.makeText(ShowEventActivity.this, "You have already booked for this event", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (hasBookedOtherEvents) {
                                        Toast.makeText(ShowEventActivity.this, "You have booked for other events", Toast.LENGTH_SHORT).show();
                                    }

                                    boolean confirm = false;
                                    createNewReservation(uid, getCreatorId, getEventId, reservationDate, confirm);
                                    Toast.makeText(ShowEventActivity.this, "Successfully booked", Toast.LENGTH_SHORT).show();
                                }


                                /*
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {


                                    if(dataSnapshot.child("idClient").getValue(String.class).equals(uid)){

                                        if(!dataSnapshot.child("idEvent").getValue(String.class).equals(getEventId)){

                                            continue;
                                        }
                                        Toast.makeText(ShowEventActivity.this , "you have already booked",Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {

                                        boolean confirm = false;
                                        createNewReservation(uid, getCreatorId, getEventId, reservationDate, confirm);
                                        Intent intent = new Intent(ShowEventActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }


                                }

                                */

                            } else {
                                // Il nodo non ha figli
                                // Puoi gestire il caso in cui non ci siano figli

                                boolean confirm = false;
                                createNewReservation(uid, getCreatorId, getEventId, reservationDate, confirm);
                                Intent intent = new Intent(ShowEventActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Gestione degli errori durante la lettura dei dati
                        }
                    });
                }
            });
        }



    }

    // Metodo per creare una nuova prenotazione nel database

    public void createNewReservation(String clientID, String ownerID,String eventId, String reservationDate, boolean confirmed) {

        DatabaseReference reservations_ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reservations");

        DatabaseReference newReservation_ref = reservations_ref.push();

        // id univoco generato

        String id_reservation = newReservation_ref.getKey();

        Reservation reservation = new Reservation(clientID, ownerID, eventId, reservationDate, confirmed);

        Toast.makeText(ShowEventActivity.this, "Successfully booked",Toast.LENGTH_SHORT).show();

        newReservation_ref.setValue(reservation);
    }
}

