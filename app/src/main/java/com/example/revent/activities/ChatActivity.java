package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.adapters.MessagesAdapter;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.MemoryData;
import com.example.revent.models.MessagesList;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private final List<MessagesList> messagesLists = new ArrayList<>();

    private String user_id;
    private String email;
    private String name;
    private String surname;
    private String token;

    private String lastMessage = "";

    private int unseenMessages = 0;

    private String chatKey= "";

    private static User user = new User();

    private RecyclerView messagesRecyclerView;
    private boolean dataSet = false;
    private MessagesAdapter messagesAdapter;

    // Set the image resource for the CircleImageView
    //circleImageView.setImageResource(R.drawable.usa);

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final CircleImageView userprofilePic = findViewById(R.id.userProfilePic);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);


        //get user data from DataBase

        String uid = new FireBaseWrapper.Auth().getUid();

        DatabaseReference ref_user = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users/"+uid+"/");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                user = dataSnapshot.getValue(User.class);
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        ref_user.addValueEventListener(userListener);

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set adapter to recyclerview

        messagesAdapter = new MessagesAdapter(messagesLists, ChatActivity.this);

        messagesRecyclerView.setAdapter(messagesAdapter);

        //get profile pic from Storage

        StorageReference imageRef = storageRef.child("images_profile/"+uid+"/image_profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024*5;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userprofilePic.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(ChatActivity.this , "Failed to download",Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();
                unseenMessages = 0;
                lastMessage ="";
                chatKey="";
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

                View view = inflater.inflate(R.layout.messages_adapter_layout, null);

                /*

                CircleImageView circleImageView_user = view.findViewById(R.id.profilePic);


                */
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final String user_id = dataSnapshot.getKey();

                    dataSet = false;
                    StorageReference imageRef = storageRef.child("images_profile/"+user_id);

                    if(!user_id.equals(uid)){
                        final String getName = String.valueOf(dataSnapshot.child("name").getValue());
                        final String getSurname = String.valueOf(dataSnapshot.child("surname").getValue());

                        // recupero immagine profile dell'user_id che ha dataSnapshot
                        /*


                        final long ONE_MEGABYTE = 1024 * 1024 * 3;
                        storageRef.child("images_profile/"+user_id).child("image_profile.jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                // Data for "images/island.jpg" is returns, use this as needed
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                circleImageView_user.setImageBitmap(bmp);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        */

                        DatabaseReference databaseReference_chat = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("chat");

                        databaseReference_chat.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {




                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if(getChatCounts > 0) {

                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if((getUserOne.equals(user_id) && getUserTwo.equals(uid)) || (getUserOne.equals(uid) && getUserTwo.equals(user_id))) {

                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {

                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(ChatActivity.this, getKey));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if(getMessageKey > getLastSeenMessage) {
                                                        unseenMessages++;
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }

                                if(!dataSet) {
                                    dataSet = true;
                                    MessagesList messagesList = new MessagesList(getName, getSurname, user_id, lastMessage, unseenMessages, chatKey);
                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        // Inflaziona il layout XML desiderato

                        ;

                        // Trova il CircleImageView all'interno del layout inflazionato





                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}