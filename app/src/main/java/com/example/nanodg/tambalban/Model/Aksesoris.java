package com.example.nanodg.tambalban.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.security.PrivateKey;

/**
 * Created by NanoDG on 27-May-18.
 */
@IgnoreExtraProperties
public class Aksesoris implements Serializable{

    public String pembuat;
    public String nama;
    public String no;
    public String buka;
    public String tutup;
    public String kendaraan;
    public String alamat;
    public double lat;
    public double longt;
    public String foto1;
    public String foto2;
    public String foto3;
    public String pemilik;
    public String status;
    public String verif;
    public String info;
    public String key;

    public Aksesoris(){
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getPembuat() {
        return pembuat;
    }
    public void setPembuat(String pembuat){
        this.pembuat = pembuat;
    }
    public String getNama() {
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no){
        this.no = no;
    }
    public String getBuka() {
        return buka;
    }
    public void setBuka(String buka){
        this.buka = buka;
    }
    public String getTutup() {
        return tutup;
    }
    public void setTutup(String tutup){
        this.tutup = tutup;
    }
    public String getKendaraan() {
        return kendaraan;
    }
    public void setKendaraan(String kendaraan){
        this.kendaraan = kendaraan;
    }
    public String getAlamat() {
        return alamat;
    }
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }
    public Double getLat() {
        return lat;
    }
    public void setLat(Double lat){
        this.lat = lat;
    }
    public Double getLongt() {
        return longt;
    }
    public void setLongt(Double longt){
        this.longt = longt;
    }
    public String getFoto1() {
        return foto1;
    }
    public void setFoto1(String foto1){
        this.foto1 = foto1;
    }
    public String getFoto2() {
        return foto2;
    }
    public void setFoto2(String foto2){
        this.foto2 = foto2;
    }
    public String getFoto3() {
        return foto3;
    }
    public void setFoto3(String foto3){
        this.foto3 = foto3;
    }
    public String getPemilik() {
        return pemilik;
    }
    public void setPemilik(String pemilik){
        this.pemilik = pemilik;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getVerif() {
        return verif;
    }
    public void setVerif(String verif){
        this.verif = verif;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info){
        this.info = info;
    }

    @Override
    public String toString(){
        return " "+pembuat+"\n" +
                " "+nama+"\n" +
                " "+no+"\n" +
                " "+buka+"\n" +
                " "+tutup+"\n" +
                " "+kendaraan+"\n" +
                " "+alamat+"\n" +
                " "+lat+"\n" +
                " "+longt+"\n" +
                " "+foto1+"\n" +
                " "+foto2+"\n" +
                " "+foto3+"\n" +
                " "+pemilik+"\n" +
                " "+status+"\n" +
                " "+verif+"\n" +
                " "+info;
    }

    public Aksesoris(String pm, String nm, String n, String bk, String tp, String kn, String al, double la, double lo, String ft1, String ft2, String ft3, String pml, String sts, String vf, String in){
        pembuat = pm;
        nama = nm;
        no = n;
        buka = bk;
        tutup = tp;
        kendaraan = kn;
        alamat = al;
        lat = la;
        longt = lo;
        foto1 = ft1;
        foto2 = ft2;
        foto3 = ft3;
        pemilik = pml;
        status = sts;
        verif = vf;
        info = in;

    }
}
