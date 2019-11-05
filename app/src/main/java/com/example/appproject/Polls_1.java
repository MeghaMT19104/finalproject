package com.example.appproject;

import java.util.List;

class Polls_1 {
    //add other defination of polls
    private int id;
    private String destination;
    private String price;
    private String time;
    private String date;
    private String name;
    private String user;
    private String key;
    private String parent;



    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    private long seat = 4;
    private String status;
    private String creater_uid;
    private List<AddedUserEntity> addedUserEntities;

    public Polls_1() {
    }

    public Polls_1(int id, String destination, String price, String time, String date) {
        this.id = id;
        this.destination = destination;
        this.price = price;
        this.time = time;
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCreater_uid() {
        return creater_uid;
    }

    public void setCreater_uid(String creater_uid) {
        this.creater_uid = creater_uid;
    }

    public List<AddedUserEntity> getAddedUserEntities() {
        return addedUserEntities;
    }

    public void setAddedUserEntities(List<AddedUserEntity> addedUserEntities) {
        this.addedUserEntities = addedUserEntities;
    }

    public long getSeat() {
        return seat;
    }

    public void setSeat(long seat) {
        this.seat = seat;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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
}
