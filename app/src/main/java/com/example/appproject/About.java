package com.example.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void about(MenuItem item) {
        Intent i= new Intent(this,About.class);
        startActivity(i);
    }

    public void edit(MenuItem item) {
        Intent i= new Intent(this,editprofile.class);
        startActivity(i);
    }
}
