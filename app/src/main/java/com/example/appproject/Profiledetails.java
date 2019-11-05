package com.example.appproject;

import java.io.Serializable;

public class Profiledetails implements Serializable {
    public String Uid;
    public String ImageUri;
    public String email;
    public String Name;
    public String Session;
    public  String Status;
    public String Gender;
    public String Age;
    public String Description;
// public String Uri;


    public Profiledetails(String name, String session, String status, String gender, String age, String description) {
        Name = name;
        Session = session;
        Status = status;
        Gender = gender;
        Age = age;
        Description = description;
    }

    public Profiledetails()
    {

    }


    public Profiledetails(String ImageUri,String Name,String Description, String session, String status, String gender, String age) {
        Session = session;
        this.ImageUri=ImageUri;
        Status = status;
        Gender = gender;
        Age = age;
        this.Description=Description;
        this.Name=Name;
    }
}