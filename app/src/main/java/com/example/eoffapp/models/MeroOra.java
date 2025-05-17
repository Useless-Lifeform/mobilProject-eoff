package com.example.eoffapp.models;

public class MeroOra {///adatmodell
    String gyariszam;
    Meres[] meresek;
    public MeroOra(String gyariszam, Meres[] meresek) {
        this.gyariszam = gyariszam;
        this.meresek = meresek;
    }
    public MeroOra() {}
    public String getGyariszam() {
        return gyariszam;
    }
    public Meres[] getMeresek() {
        return meresek;
    }
    public void setGyariszam(String gyariszam) {
        this.gyariszam = gyariszam;
    }
    public void setMeresek(Meres[] meresek) {
        this.meresek = meresek;}

}
