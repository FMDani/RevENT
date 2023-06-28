package com.example.revent.fragments;

import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.revent.R;
import com.example.revent.activities.EditProfileActivity;
import com.example.revent.activities.MainActivity;
import com.example.revent.adapters.MyEventAdapter;
import com.example.revent.models.FireBaseWrapper;
import com.example.revent.models.MyEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    public static ArrayList<MyEvent> eventList = new ArrayList<MyEvent>();

    private ListView listView;

    private String selectedFilter = "all";
    private String currentSearchText = "";
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View externalView = inflater.inflate(R.layout.fragment_search, container, false);

        initSearchWidgets(externalView);
        if (eventList.isEmpty()) setupData();

        setUpList(externalView);
        setUpOnclickListener();
        return externalView;
    }


    private void initSearchWidgets(View externalView)
    {


        searchView = (SearchView)  externalView.findViewById(R.id.shapeListSearchView);
        Button allevents_button = externalView.findViewById(R.id.home_allevents);
        Button myevents_button = externalView.findViewById(R.id.home_myevents);
        Button registeredevents_button = externalView.findViewById(R.id.home_registeredevents);


        myevents_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = "myevents";
                String uid = new FireBaseWrapper.Auth().getUid();
                ArrayList<MyEvent> filtereventList = new ArrayList<MyEvent>();

                for(MyEvent event: eventList){
                    if (event.getUserCreatorId().equals(uid)){
                                    filtereventList.add(event);
                    }
                }
                MyEventAdapter adapter = new MyEventAdapter(getActivity(), 0, filtereventList);
                listView.setAdapter(adapter);
            }
        });
        registeredevents_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = "registeredevents";
                String uid = new FireBaseWrapper.Auth().getUid();
                ArrayList<MyEvent> filtereventList = new ArrayList<MyEvent>();

                for(MyEvent event: eventList){
                    if (event.getUserSubscribeId().contains(uid)){
                        filtereventList.add(event);
                    }
                }
                MyEventAdapter adapter = new MyEventAdapter(getActivity(), 0, filtereventList);
                listView.setAdapter(adapter);
            }
        });
        allevents_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilter = "all";
                MyEventAdapter adapter = new MyEventAdapter(getActivity(), 0, eventList);
                listView.setAdapter(adapter);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                String uid = new FireBaseWrapper.Auth().getUid();
                currentSearchText = s;
                ArrayList<MyEvent> filtereventList = new ArrayList<MyEvent>();

                for(MyEvent event: eventList)
                {
                    if(event.getTitle().toLowerCase().contains(s.toLowerCase()))
                    {
                        switch (selectedFilter) {
                            case "all":{
                                // Code to handle "all" filter
                                filtereventList.add(event);
                                break;
                            }
                            case "myevents":{
                                // Code to handle "all" filter
                                    if (event.getUserCreatorId().equals(uid)){
                                        filtereventList.add(event);
                                    }
                                break;
                            }
                            case "registeredevents":{
                                // Code to handle "all" filter
                                if (event.getUserSubscribeId().contains(uid)){
                                    filtereventList.add(event);
                                }
                                break;
                            }
                        }

                    }
                }
                MyEventAdapter adapter = new MyEventAdapter(getActivity(), 0, filtereventList);
                listView.setAdapter(adapter);

                return false;
            }
        });



    }

    private void setupData()
    {
        /*
        //juan
        MyEvent event1 = new MyEvent("1", "matrimonio", "Principe", "Lo smuzzo di sposa", "01/01/2023", "02/01/2023", "10h", "msiqXE1mdOXZmrwUtc1VBD01WKJ3", new ArrayList<String>(Arrays.asList("XdmnkjUQc2OAUOQ60HVLmuZMnUR2", "HvuSpTrIRlbBScWBAlITw9yFoco1")));
        eventList.add(event1);
        //daniel
        MyEvent event2 = new MyEvent("2", "laurea", "albaro", "daniel si laurea", "01/02/2023", "02/02/2023", "5h", "XdmnkjUQc2OAUOQ60HVLmuZMnUR2", new ArrayList<String>(Arrays.asList( "HvuSpTrIRlbBScWBAlITw9yFoco1")));
        eventList.add(event2);
        //pippo
        MyEvent event3 = new MyEvent("3", "partita", "marassi", "genoa in serie A", "01/03/2023", "02/03/2023", "3h", "HvuSpTrIRlbBScWBAlITw9yFoco1", new ArrayList<String>(Arrays.asList("msiqXE1mdOXZmrwUtc1VBD01WKJ3", "XdmnkjUQc2OAUOQ60HVLmuZMnUR2")));
        eventList.add(event3);

         */
        DatabaseReference eventsRef = FirebaseDatabase.getInstance("https://revent-93be7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");

        eventsRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        // Ottieni i dati per ogni evento
                        String eventId = eventSnapshot.child("eventId").getValue(String.class);
                        String title = eventSnapshot.child("title").getValue(String.class);
                        String place = eventSnapshot.child("place").getValue(String.class);
                        String descr = eventSnapshot.child("descr").getValue(String.class);
                        String dtStart = eventSnapshot.child("dtStart").getValue(String.class);
                        String dtEnd = eventSnapshot.child("dtEnd").getValue(String.class);
                        String dur = eventSnapshot.child("dur").getValue(String.class);
                        String userCreatorId = eventSnapshot.child("userCreatorId").getValue(String.class);
                        ArrayList<String> userSubscribeId = new ArrayList<>();

                        for (DataSnapshot userSnapshot : eventSnapshot.child("userSubscribeId").getChildren()) {
                            String userId = userSnapshot.getValue(String.class);
                            userSubscribeId.add(userId);
                        }

                        // Aggiungi i dati all'oggetto MyEvent
                        MyEvent event = new MyEvent(eventId, title, place, descr, dtStart, dtEnd, dur, userCreatorId, userSubscribeId);

                        // Aggiungi l'evento alla lista
                        eventList.add(event);
                    }

                } else {
                    Toast
                            .makeText(getActivity(), "No Events found", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void setUpList(View externalView)
    {
        listView = (ListView)  externalView.findViewById(R.id.shapesListView);

        MyEventAdapter adapter = new MyEventAdapter(getActivity(), 0, eventList);
        listView.setAdapter(adapter);
    }

    private void setUpOnclickListener()
    {
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                Shape selectShape = (Shape) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), DetailActivity.class);
                showDetail.putExtra("id",selectShape.getId());
                startActivity(showDetail);
            }
        });
         */

    }
}