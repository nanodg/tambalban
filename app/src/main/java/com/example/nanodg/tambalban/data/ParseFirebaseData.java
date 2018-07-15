package com.example.nanodg.tambalban.data;

import android.content.Context;

import com.example.nanodg.tambalban.Model.ChatMessage;
import com.example.nanodg.tambalban.Model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class ParseFirebaseData {
    private SettingsAPI set;

    public ParseFirebaseData(Context context) {
        set = new SettingsAPI(context);
    }

    public List<User> getUserList(String userData) {
        List<User> frnds = new ArrayList<>();
        String username = null, key = null, email=null, pemilik=null;
        for (String oneUser : userData.split("[}][,]")) {
            String[] temp = oneUser.replace("}", "").split("[{]");
            String[] userParts = temp[temp.length - 1].split(",");
            for (String part : userParts) {
                if (part.split("=")[0].trim().equals("username"))
                    username = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("key"))
                    key = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("email"))
                    email = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("pemilik"))
                    pemilik = part.split("=")[1].trim();

            }
            if (!set.readSetting("myid").equals(key))
                frnds.add(new User(key, email,username,pemilik));
        }
        return frnds;
    }

    public List<ChatMessage> getMessageListForUser(String msgData) {
        List<ChatMessage> chats = new ArrayList<>();
        if(msgData.replace("{","").replace("}","").split(",")[1].trim().equals("value = null"))
            return chats;
        ChatMessage tempMsg = null;
        String text = null, msgTime = null, pengirimKey = null, pengirimNama = null, receiverKey = null, receiverNama = null,receiverPemilik = null,receiverEmail = null,pengirimPemilik = null,pengirimEmail = null;
        for (String msgInConv : msgData.split("[}][,]")) {
            String[] temp = msgInConv.replace("}", "").split("[{]");
            String[] msgParts = temp[temp.length - 1].split(",");
            for (String part : msgParts) {
                if (part.split("=")[0].trim().equals("text"))
                    text = decodeText(part.split("=")[1].trim());
                if (part.split("=")[0].trim().equals("timestamp"))
                    msgTime = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("senderkey"))
                    pengirimKey = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("sendernama"))
                    pengirimNama = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("senderpemilik"))
                    pengirimPemilik = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("senderemail"))
                    receiverEmail = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("receiverkey"))
                    receiverKey = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("receivernama"))
                    receiverNama = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("receiverpemilik"))
                    receiverPemilik = part.split("=")[1].trim();
                if (part.split("=")[0].trim().equals("receiveremail"))
                    receiverEmail = part.split("=")[1].trim();

            }
            tempMsg = new ChatMessage(text, msgTime, receiverKey, receiverNama,receiverPemilik, receiverEmail, pengirimKey, pengirimNama,pengirimPemilik,pengirimEmail);
            chats.add(tempMsg);
        }
        Collections.sort(chats, new Comparator<ChatMessage>() {
            public int compare(ChatMessage c1, ChatMessage c2) {
                return (c1.getComparableTimestamp() > c2.getComparableTimestamp() ? 1 : (c1.getComparableTimestamp() < c2.getComparableTimestamp() ? -1 : 0));
            }
        });
        return chats;
    }

    public List<ChatMessage> getLastMessageList(String msgData) {
        //Return only last messages of every conversation "I" am involved in
        List<ChatMessage> lastChats = new ArrayList<>();
        ChatMessage tempMsg = null;
        List<ChatMessage> tempMsgList;
        long lastTimeStamp;
        String text = null, msgTime = null, pengirimKey = null, pengirimNama = null, receiverKey = null, receiverNama = null,receiverPemilik = null,receiverEmail = null,pengirimPemilik = null,pengirimEmail = null;
        for (String oneConv : msgData.split("[}][}][,]")) {
            tempMsgList = new ArrayList<>();
            lastTimeStamp = 0;
            for (String msgInConv : oneConv.split("[}][,]")) {
                String[] temp = msgInConv.replace("}", "").split("[{]");
                String[] msgParts = temp[temp.length - 1].split(",");
                for (String part : msgParts) {
                    if (part.split("=")[0].trim().equals("text"))
                        text = decodeText(part.split("=")[1].trim());
                    if (part.split("=")[0].trim().equals("timestamp")) {
                        msgTime = part.split("=")[1].trim();
                        if (Long.parseLong(msgTime) > lastTimeStamp)
                            lastTimeStamp = Long.parseLong(msgTime);
                    }
                    if (part.split("=")[0].trim().equals("senderkey"))
                        pengirimKey = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("sendernama"))
                        pengirimNama = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("senderpemilik"))
                        pengirimPemilik = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("senderemail"))
                        receiverEmail = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("receiverkey"))
                        receiverKey = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("receivernama"))
                        receiverNama = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("receiverpemilik"))
                        receiverPemilik = part.split("=")[1].trim();
                    if (part.split("=")[0].trim().equals("receiveremail"))
                        receiverEmail = part.split("=")[1].trim();
                }
                tempMsg = new ChatMessage(text, msgTime, receiverKey, receiverNama,receiverPemilik, receiverEmail, pengirimKey, pengirimNama,pengirimPemilik,pengirimEmail);
                tempMsgList.add(tempMsg);
            }
            for (ChatMessage oneTemp : tempMsgList) {
                if ((set.readSetting("myid").equals(oneTemp.getReceiver().getKey())) || (set.readSetting("myid").equals(oneTemp.getSender().getKey()))) {
                    if (oneTemp.getTimestamp().equals(String.valueOf(lastTimeStamp))) {
                        lastChats.add(oneTemp);
                    }
                }
            }
        }
        return lastChats;
    }

    private String encodeText(String msg) {
        return msg.replace(",", "#comma#").replace("{", "#braceopen#").replace("}", "#braceclose#").replace("=", "#equals#");
    }

    private String decodeText(String msg) {
        return msg.replace("#comma#", ",").replace("#braceopen#", "{").replace("#braceclose#", "}").replace("#equals#", "=");
    }
}
