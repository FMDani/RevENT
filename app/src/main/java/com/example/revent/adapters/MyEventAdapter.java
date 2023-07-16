package com.example.revent.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.revent.R;
import com.example.revent.activities.ConversationActivity;
import com.example.revent.activities.ShowEventActivity;
import com.example.revent.models.MyEvent;
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

public class MyEventAdapter extends ArrayAdapter<MyEvent>
{
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    private final Context context;

    public MyEventAdapter(Context context, int resource, List<MyEvent> shapeList)
    {
        super(context,resource,shapeList);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyEvent event = getItem(position);



        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_adapter, parent, false);
        }



        ImageView imageevent = (ImageView) convertView.findViewById(R.id.imageView3);

        StorageReference imageRef = storageRef.child("images_events/"+event.getEventId()+"/.image_event.jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 3;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageevent.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        TextView eventName = (TextView) convertView.findViewById(R.id.eventName_cell);
        TextView location = (TextView) convertView.findViewById(R.id.placeName_cell);


        eventName.setText(event.getTitle());
        location.setText(event.getPlace());

        Button btn_moreinfo = (Button) convertView.findViewById(R.id.button3);

        btn_moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");

                ref.child(event.getEventId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String descr = String.valueOf(snapshot.child("descr").getValue());
                            String dtEnd = String.valueOf(snapshot.child("dtEnd").getValue());
                            String dtStart = String.valueOf(snapshot.child("dtStart").getValue());
                            String dur = String.valueOf(snapshot.child("dur").getValue());
                            String eventId = String.valueOf(snapshot.child("eventId").getValue());
                            String place = String.valueOf(snapshot.child("place").getValue());
                            String title = String.valueOf(snapshot.child("title").getValue());
                            String userCreatorId = String.valueOf(snapshot.child("userCreatorId").getValue());

                            Intent intent = new Intent(context, ShowEventActivity.class);
                            intent.putExtra("descr",descr);
                            intent.putExtra("dtEnd",dtEnd);
                            intent.putExtra("dtStart",dtStart);
                            intent.putExtra("dur",dur);
                            intent.putExtra("eventId",eventId);
                            intent.putExtra("place",place);
                            intent.putExtra("title",title);
                            intent.putExtra("userCreatorId",userCreatorId);
                            context.startActivity(intent);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        return convertView;
    }
}