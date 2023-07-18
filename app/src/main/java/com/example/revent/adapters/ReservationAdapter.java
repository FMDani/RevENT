package com.example.revent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revent.R;
import com.example.revent.models.FCMSend;
import com.example.revent.models.ReservationList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.MyViewHolder> {

    private List<ReservationList> reservationLists;

    private final Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    public ReservationAdapter(List<ReservationList> reservationLists, Context context) {
        this.reservationLists = reservationLists;
        this.context = context;
    }


    @NonNull
    @Override
    public ReservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.MyViewHolder holder, int position) {
        ReservationList list1 = reservationLists.get(position);

        StorageReference imageRef = storageRef.child("images_profile/"+list1.getIdClient()+"/image_profile.jpg");
        DatabaseReference databaseReference_users = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");


        if (list1.isConfirmed()) {

            holder.btn_confirmstatus.setVisibility(View.GONE);
            holder.confirmedText.setVisibility(View.VISIBLE);
        } else {

            holder.btn_confirmstatus.setVisibility(View.VISIBLE);
            holder.confirmedText.setVisibility(View.GONE);
        }

        final long ONE_MEGABYTE = 1024 * 1024 * 3;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.res_profilePic.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        databaseReference_users.child(list1.getIdClient()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                holder.reservation_name.setText(dataSnapshot.child("name").getValue(String.class));
                holder.reservation_surname.setText(dataSnapshot.child("surname").getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Errore nel recupero dei dati dal database
            }
        });
        holder.reservation_date.setText(list1.getReservationDate());


        holder.btn_confirmstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference_res = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reservations");
                DatabaseReference databaseReference_events = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");
                DatabaseReference databaseReference_users = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
                DatabaseReference databaseReference_tokens = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("tokens");


                databaseReference_events.child(list1.getIdEvent()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String confirmedReservationId = dataSnapshot.child("confirmedReservationId").getValue(String.class);
                        String confirmedClientId = dataSnapshot.child("confirmedClientId").getValue(String.class);
                        String titleEvent = dataSnapshot.child("title").getValue(String.class);

                        if (confirmedReservationId.equals("") && confirmedClientId.equals("")) {
                            databaseReference_events.child(list1.getIdEvent()).child("confirmedReservationId").setValue(list1.getReservationId());
                            databaseReference_events.child(list1.getIdEvent()).child("confirmedClientId").setValue(list1.getIdClient());
                            databaseReference_res.child(list1.getReservationId()).child("confirmed").setValue(true);


                            databaseReference_tokens.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String clientToken = snapshot.child(list1.getIdClient()).getValue(String.class);
                                    FCMSend.pushNotification(
                                            context,
                                            clientToken,
                                            "Ciao ",
                                            "La sua prenotazione a "+titleEvent+" è stata confermata"

                                    );
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(context, "Already confirmed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Gestisci l'errore nella lettura dei dati
                    }
                });
            }
        });

    }

    public void updateData(List<ReservationList> reservationLists) {
        this.reservationLists = reservationLists;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return reservationLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView res_profilePic;
        private TextView reservation_name;
        private TextView reservation_surname;
        private TextView reservation_date;
        private TextView confirmedText;
        private Button btn_statusref;
        private Button btn_confirmstatus;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            res_profilePic = itemView.findViewById(R.id.show_profile);
            reservation_name = itemView.findViewById(R.id.name_reservation);
            reservation_surname = itemView.findViewById(R.id.surname_reservation);
            reservation_date = itemView.findViewById(R.id.date_reservation);
            confirmedText = itemView.findViewById(R.id.confirmedText);
            btn_statusref = itemView.findViewById(R.id.btn_refstatus);
            btn_confirmstatus = itemView.findViewById(R.id.btn_confirmstatus);
        }
    }
}
