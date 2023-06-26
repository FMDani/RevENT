package com.example.revent.models;


import java.util.ArrayList;

public class MyEvent {

    // TODO: Levare - è simile a quella del prof ma io non uso il contentprovider infatti levo calenid e isallday(attributi)

    private long eventId;
    private String title;
    private String descr;
    private long dtStart;
    private long dtEnd;
    private String dur;

    private String userCreatorId;

    private ArrayList<String> userSubscribeId;

    public MyEvent() {}

    public MyEvent(long eventId, String title, String descr, long dtStart, long dtEnd, String dur, String userCreatorId ) {
        this.userCreatorId = userCreatorId;
        this.eventId = eventId;
        this.title = title;
        this.descr = descr;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.dur = dur;
    }

    public long getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescr() {
        return descr;
    }

    public long getDtStart() {
        return dtStart;
    }

    public long getDtEnd() {
        return dtEnd;
    }

    public String getDur() {
        return dur;
    }


    public void setEventId(long eventId) {
        this.eventId = eventId;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setDtStart(long dtStart) {
        this.dtStart = dtStart;
    }

    public void setDtEnd(long dtEnd) {
        this.dtEnd = dtEnd;
    }

    public void setDur(String dur) {
        this.dur = dur;
    }


}
