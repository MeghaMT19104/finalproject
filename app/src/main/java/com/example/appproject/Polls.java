package com.example.appproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class Polls {
    //add defination of polls
    private String pid;
    private String destination;
    private String price;
    private String time;
    private String date;
    private int seats=0;
    private String creater_uid;
    private String chatroom;
    private String status="active";
    private String gender;
    private String type;
    private String session;
    private int max_age;
    private String source;

    public Polls(String destination, String price, String time, String date, String creater_uid, String gender, String type, String session, int max_age, String source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        this.pid = sdf.format(new Date());
        this.destination = destination;
        this.price = price;
        this.time = time;
        this.date = date;
        this.seats = 4;
        this.creater_uid = creater_uid;
        this.chatroom =this.pid;
        this.gender = gender;
        this.type = type;
        this.session = session;
        this.max_age = max_age;
        //changed here
        this.source=source;
    }

    public String getSource() {
        return source;
    }

    public Polls(){

    }
    //public void setDestination(String dest) {

    public String getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }

    public String getSession() {
        return session;
    }

    public int getMax_age() {
        return max_age;
    }
    //  this.destination = dest;
    //}

    public String getStatus() {
        return status;
    }

    public String getDestination() {
        return destination;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getPid() {
        return pid;
    }

    public String getCreater_uid() {
        return creater_uid;
    }



    public String getChatroom() {
        return chatroom;
    }

    public int getSeats() {
        return seats;
    }



/*
    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setCreator_uid(int creator_uid) {
        this.creator_uid = creator_uid;
    }

    public void setAccepted(ArrayList<Integer> accepted) {
        this.accepted = accepted;
    }

    public void setChat(String chatroom) {
        this.chatroom = chatroom;
    }*/
}
