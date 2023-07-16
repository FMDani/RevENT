package com.example.revent.models;

public class Reservation {
    private String idClient;
    private String idOwner;

    private String idEvent;
    private String reservationDate;

    private boolean confirmed;

    public Reservation() {
        // Costruttore vuoto richiesto per Firebase Realtime Database
    }

    public Reservation(String idClient, String idOwner, String idEvent, String reservationDate, boolean confirmed) {
        this.idClient = idClient;
        this.idOwner = idOwner;
        this.idEvent = idEvent;
        this.reservationDate = reservationDate;
        this.confirmed = confirmed;
    }

    // Metodi getter e setter per le proprietà della prenotazione
    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public String getIdEvent() {return idEvent; }



    public String getreservationDate() {
        return reservationDate;
    }

    public boolean getConfirmed() { return confirmed; }

    public void setreservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}

