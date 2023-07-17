package com.example.revent.models;

public class ReservationList {


    private String idClient;
    private String idEvent;
    private String reservationDate;
    private boolean confirmed;

    private String reservationId;

    public ReservationList() {
    }

    public ReservationList(String idClient,String idEvent,String reservationDate, boolean confirmed, String reservationId) {
        this.idClient = idClient;
        this.idEvent = idEvent;
        this.reservationDate = reservationDate;
        this.confirmed = confirmed;
        this.reservationId = reservationId;
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

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}
