/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
