package com.example.nanodg.tambalban.Model;

/**
 * Created by NanoDG on 11-Jun-18.
 */

public class User {
    private String email;
    private String username;
    private String pemilik;


    public User(){

    }

    public User(String email, String username, String pemilik){
        this.email = email;
        this.username = username;
        this.pemilik = pemilik;

    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email= email;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username= username;
    }
    public String getPemilik(){
        return pemilik;
    }
    public void setPemilik(String pemilik){
        this.pemilik= pemilik;
    }

}
