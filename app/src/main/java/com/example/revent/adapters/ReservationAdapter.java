package com.example.revent.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revent.R;
import com.example.revent.models.Reservation;
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

        // TODO: implement the confirm button here with setonclick

        holder.btn_confirmstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference_res = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reservations");

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
        private Button btn_statusref;
        private Button btn_confirmstatus;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            res_profilePic = itemView.findViewById(R.id.show_profile);
            reservation_name = itemView.findViewById(R.id.name_reservation);
            reservation_surname = itemView.findViewById(R.id.surname_reservation);
            reservation_date = itemView.findViewById(R.id.date_reservation);
            btn_statusref = itemView.findViewById(R.id.btn_refstatus);
            btn_confirmstatus = itemView.findViewById(R.id.btn_confirmstatus);
        }
    }
}
