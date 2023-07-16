package com.example.revent.models;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyEvent {

    // TODO: Levare - è simile a quella del prof ma io non uso il contentprovider infatti levo calenid e isallday(attributi)

    private String eventId;
    private String title;

    private String place;
    private String descr;
    private String dtStart;
    private String dtEnd;
    private String dur;
    public String getPlace() {
        return place;
    }

    public String getDtStart() {
        return dtStart;
    }

    public void setDtStart(String dtStart) {
        this.dtStart = dtStart;
    }

    public String getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(String dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(String userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public ArrayList<String> getUserSubscribeId() {
        return userSubscribeId;
    }

    public void setUserSubscribeId(ArrayList<String> userSubscribeId) {
        this.userSubscribeId = userSubscribeId;
    }



    public void setPlace(String place) {
        this.place = place;
    }





    private String userCreatorId;

    private ArrayList<String> userSubscribeId;

    public MyEvent() {}

    public MyEvent(String eventId, String title, String place, String descr, String dtStart, String dtEnd, String dur, String userCreatorId, ArrayList<String> userSubscribeId) {
        this.eventId = eventId;
        this.title = title;
        this.place = place;
        this.descr = descr;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.dur = dur;
        this.userCreatorId = userCreatorId;
        this.userSubscribeId = userSubscribeId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescr() {
        return descr;
    }



    public String getDur() {
        return dur;
    }





    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }



    public void setDur(String dur) {
        this.dur = dur;
    }





}



