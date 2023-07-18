package com.example.revent.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Notification;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.revent.R;
import com.example.revent.adapters.ConversationAdapter;
import com.example.revent.models.ConversationList;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.MemoryData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationActivity extends AppCompatActivity {

    SplashActivity splashActivity = new SplashActivity();

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    private final List<ConversationList> conversationLists = new ArrayList<>();
    private String chatKey;

    String getUserid = "";

    private RecyclerView chattingRecyclerView;

    private ConversationAdapter conversationAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.name);
        final TextView surnameTV = findViewById(R.id.surname);
        final EditText messageEditText = findViewById(R.id.messageEditTxt);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final ImageView sendBtn = findViewById(R.id.sendBtn);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);


        // get data from messages adapter class

        final String getName = getIntent().getStringExtra("name");
        final String getSurName = getIntent().getStringExtra("surname");
        final String user_id = getIntent().getStringExtra("user_id");
        chatKey = getIntent().getStringExtra("chat_key");


        String uid = new FireBaseWrapper.Auth().getUid();

        getUserid = uid;


        nameTV.setText(getName);
        surnameTV.setText(getSurName);

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(ConversationActivity.this));

        conversationAdapter = new ConversationAdapter(conversationLists, ConversationActivity.this);
        chattingRecyclerView.setAdapter(conversationAdapter);

        StorageReference imageRef = storageRef.child("images_profile/"+user_id+"/image_profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 3;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(chatKey.isEmpty()) {
                        // generate chat key. by default chatKey is 1
                        chatKey = "1";

                        if (snapshot.hasChild("chat")) {
                            chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                        }

                    }

                    if (snapshot.hasChild("chat")) {
                        if (snapshot.child("chat").child(chatKey).hasChild("messages")) {

                            conversationLists.clear();

                            for (DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()) {

                                if (messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("user_id")) {


                                    final String messageTimestamps = messagesSnapshot.getKey();
                                    final String getUserid = messagesSnapshot.child("user_id").getValue(String.class);
                                    final String getMsg = messagesSnapshot.child("msg").getValue(String.class);



                                    Date date = new Date(Long.parseLong(messageTimestamps));
                                    Timestamp timestamp = new Timestamp(date);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                    ConversationList conversationList = new ConversationList(getUserid, getName, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                    conversationLists.add(conversationList);

                                    if (loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(ConversationActivity.this, chatKey))) {

                                        loadingFirstTime = false;

                                        MemoryData.saveLastMsgTS(messageTimestamps, chatKey, ConversationActivity.this);

                                        conversationAdapter.updateConversationList(conversationLists);

                                        chattingRecyclerView.scrollToPosition(conversationLists.size() - 1);
                                    }
                                }

                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getTxtMessage = messageEditText.getText().toString();

                // get current timestamps
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0,10);

                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserid);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(user_id);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("user_id").setValue(getUserid);

                //clear edit text
                messageEditText.setText("");
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}