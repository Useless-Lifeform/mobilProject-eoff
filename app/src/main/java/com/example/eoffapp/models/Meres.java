package com.example.eoffapp.models;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Meres {
    String date;
    String id;
    int meres;
    String meroszama;

    public Meres(){}
    public Meres( String id, int meres) {
        this.date = getNowAsString();
        this.id = id;
        this.meres = meres;
    }
    private String getNowAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
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
