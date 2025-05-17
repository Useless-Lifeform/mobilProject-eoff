package com.example.eoffapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Meres {
    LocalDateTime date;
    String id;
    int meres;
    String meroszama;

    public Meres(){}
    public Meres( String id, int meres) {
        this.date = LocalDateTime.now();
        this.id = id;
        this.meres = meres;
    }

    /*public LocalDateTime getDate() {
        return date;
    }*/
    public String getDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        return  date.format(formatter);
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMeres() {
        return meres;
    }

    public void setMeres(int meres) {
        this.meres = meres;
    }

    public String getMeroszama() {
        return meroszama;
    }

    public void setMeroszama(String meroszama) {
        this.meroszama = meroszama;
    }
}
