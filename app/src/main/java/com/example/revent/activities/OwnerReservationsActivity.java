package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.revent.R;
import com.example.revent.adapters.ReservationAdapter;
import com.example.revent.models.ReservationList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class OwnerReservationsActivity extends AppCompatActivity {
    private final List<ReservationList> reservationLists = new ArrayList<>();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    private RecyclerView reservationRecyclerView;
    private ReservationAdapter reservationAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_reservations);
        final String getEventId = getIntent().getStringExtra("EventId");

        reservationRecyclerView = findViewById(R.id.reservationRecyclerView);

        DatabaseReference reservations_Ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reservations");

        reservations_Ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                // Leggi i dati della prenotazione dal nodo corrente

                if(snapshot.child("idEvent").getValue(String.class).equals(getEventId)){

                    String reservationId = snapshot.getKey();
                    String idClient = snapshot.child("idClient").getValue(String.class);
                    String idEvent = snapshot.child("idEvent").getValue(String.class);
                    String reservationDate = snapshot.child("reservationDate").getValue(String.class);
                    boolean confirmed = snapshot.child("confirmed").getValue(boolean.class);

                    // Crea un oggetto Reservation con i dati ottenuti


                    ReservationList reservationList = new ReservationList(idClient, idEvent,reservationDate, confirmed, reservationId);

                    // Aggiungi l'oggetto Reservation alla lista

                    reservationLists.add(reservationList);

                    reservationAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reservationRecyclerView.setHasFixedSize(true);
        reservationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set adapter to recyclerview

        reservationAdapter = new ReservationAdapter(reservationLists, this);

        reservationRecyclerView.setAdapter(reservationAdapter);
        //reservationAdapter.updateData(reservationLists);
    }
}