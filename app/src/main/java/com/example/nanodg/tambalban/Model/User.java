package com.example.nanodg.tambalban.Model;

import java.io.Serializable;

/**
 * Created by NanoDG on 11-Jun-18.
 */

public class User implements Serializable {
    public String email;
    public String username;
    public String pemilik;
    public String key;


    public User(){

    }

    public User(String email, String username, String pemilik){
        this.email = email;
        this.username = username;
        this.pemilik = pemilik;

    }
    public String getKey() {
        return key;
    }
    public void setKey(String key){
        this.key = key;
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
