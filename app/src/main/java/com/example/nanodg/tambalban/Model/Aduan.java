package com.example.nanodg.tambalban.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by NanoDG on 20-Jun-18.
 */
@IgnoreExtraProperties
public class Aduan implements Serializable {

    public String pembuat;
    public String tanggal;
    public String keterangan;
    public String kategori;
    public String file1;
    public String namatambal;
    public String pemilik;
    public String status;
    public String key;

    public Aduan(){
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getPembuat(){
        return pembuat;
    }
    public void setPembuat(String pembuat){
        this.pembuat= pembuat;
    }
    public String getTanggal(){
        return tanggal;
    }
    public void setTanggal(String tanggal){
        this.tanggal= tanggal;
    }
    public String getKeterangan(){
        return keterangan;
    }
    public void setKeterangan(String keterangan){
        this.keterangan= keterangan;
    }
    public String getFile1(){
        return file1;
    }
    public void setFile1(String file1){
        this.file1= file1;
    }
    public String getKategori(){
        return kategori;
    }
    public void setKategori(String kategori){
        this.kategori= kategori;
    }
    public String getNamatambal(){
        return namatambal;
    }
    public void setNamatambal(String namatambal){
        this.namatambal= namatambal;
    }
    public String getPemilik(){
        return pemilik;
    }
    public void setPemilik(String pemilik){
        this.pemilik= pemilik;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status= status;
    }

    @Override
    public String toString(){
        return " "+pembuat+"\n" +
                " "+tanggal+"\n" +
                " "+keterangan+"\n" +
                " "+kategori+"\n" +
                " "+file1+"\n" +
                " "+namatambal+"\n" +
                " "+pemilik+"\n" +
                " "+status;
    }

    public Aduan(String pm, String tgl, String ket, String fl1, String kat,String nmt,String pmk,String st) {
       pembuat = pm;
       tanggal = tgl;keterangan = ket;
        file1 = fl1;
        kategori = kat;
        namatambal = nmt;
        pemilik = pmk;
        status = st;
    }
}
