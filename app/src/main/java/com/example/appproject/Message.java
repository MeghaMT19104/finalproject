package com.example.appproject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String name;
    private String text;
    private String userId;
    private String time;
    private boolean myMsgFlag;

    public Message(String name, String text, String userId) {
        this.name = name;
        this.text = text;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        time = formatter.format(new Date());
        this.userId = userId;
        this.myMsgFlag = false;
    }
    public Message(String name, String text, String userId, boolean myMsgFlag) {
        this.name = name;
        this.text = text;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        time = formatter.format(new Date());
        this.userId = userId;
        this.myMsgFlag = myMsgFlag;
    }

    public Message(String name, String text, String userId, String time) {
        this.name = name;
        this.text = text;
        this.time = time;
        this.userId = userId;
        this.myMsgFlag = false;
    }

    public Message() {
        this.name = "name";
        this.text = "text";
        this.time = "time";
        this.userId = "userId";
        this.myMsgFlag = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}