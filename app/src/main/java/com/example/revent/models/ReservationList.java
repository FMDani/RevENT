package com.example.revent.models;

public class ReservationList {

    private String idClient;
    private String idEvent;
    private String reservationDate;
    private boolean confirmed;

    public ReservationList() {
    }

    public ReservationList(String idClient,String idEvent,String reservationDate, boolean confirmed) {
        this.idClient = idClient;
        this.idEvent = idEvent;
        this.reservationDate = reservationDate;
        this.confirmed = confirmed;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
