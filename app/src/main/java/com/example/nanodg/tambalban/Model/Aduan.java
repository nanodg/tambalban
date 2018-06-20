package com.example.nanodg.tambalban.Model;

/**
 * Created by NanoDG on 20-Jun-18.
 */

public class Aduan {

    private String pembuat;
    private String tanggal;
    private String keterangan;
    private String kategori;
    private String file1;

    public Aduan(String pembuat, String tanggal, String keterangan, String file1, String kategori) {
        this.pembuat = pembuat;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.file1 = file1;
        this.kategori = kategori;
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
}
