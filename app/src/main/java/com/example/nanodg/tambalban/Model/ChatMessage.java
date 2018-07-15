
package com.example.nanodg.tambalban.Model;

import com.example.nanodg.tambalban.data.Tools;

public class ChatMessage {

    private String text;
    private String userKey;
    private String userNama;
    private String userPemilik;
    private String userEmail;
    private String pengirimKey;
    private String pengirimNama;
    private String pengirimPemilik;
    private String pengirimEmail;

    private String timestamp;

    public ChatMessage(String text, String timestamp, String userKey, String userNama,  String userPemilik,String userEmail, String pengirimKey, String pengirimNama,  String pengirimPemilik, String pengirimEmail) {
        this.text = text;
        this.timestamp = timestamp;
        this.userKey=userKey;
        this.userNama=userNama;
        this.userPemilik=userPemilik;
        this.userEmail=userEmail;
        this.pengirimKey=pengirimKey;
        this.pengirimNama=pengirimNama;
        this.pengirimPemilik=pengirimPemilik;
        this.pengirimEmail=pengirimEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReadableTime()
    {
        try {
            return Tools.formatTime(Long.valueOf(timestamp));
        }
        catch (NumberFormatException ignored) {
            return null;
        }
    }

    public User getReceiver() {
        return new User(userKey,userEmail,userNama, userPemilik);
    }

    public User getSender() {
        return new User(pengirimKey,pengirimEmail,pengirimNama,pengirimPemilik);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getComparableTimestamp()
    {
        return Long.parseLong(timestamp);
    }
}
