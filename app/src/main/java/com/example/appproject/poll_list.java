package com.example.appproject;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class poll_list {
    private static poll_list sPollList;
    private List<Polls> mPolls;
    private poll_list() {
        mPolls=new ArrayList<>();
        System.out.println("The poll size is "+mPolls.size());
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("polls");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPolls.clear();
                //changed here
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Polls p=dataSnapshot1.getValue(Polls.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    DateTimeFormatter sdf2 = DateTimeFormatter.ofPattern("HH:mm");
                    Date date1=new Date();
                    Date date2=new Date();
                    LocalTime time1=LocalTime.now();
                    LocalTime time2=LocalTime.now();
                    try {
                        date1 = sdf.parse(p.getDate());
                        time1 = LocalTime.parse(p.getTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    boolean b=sdf.format(date1).compareTo(sdf.format(date2))==0;
                    boolean t=(sdf2.format(time1).compareTo(sdf2.format(time2))>=0);
                    System.out.println(b+" "+t);
                    if(date1.after(date2))
                        mPolls.add(p);
                    else if(b && t)
                        mPolls.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static poll_list get(){

        if(sPollList==null){
            sPollList=new poll_list();
        }
        return sPollList;
    }
    public static void makeNull(){
        sPollList=null;
    }
    public List<Polls> getPolls() {
        return mPolls;
    }
}
