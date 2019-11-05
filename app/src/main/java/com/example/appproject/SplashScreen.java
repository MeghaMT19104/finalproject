package com.example.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start login activity if login status is false
                Intent i;
                if(!getLoginStatusFromPreferences())
                {
                    i = new Intent(SplashScreen.this, LoginActivity.class);
                }
                else
                {
                    i = new Intent(SplashScreen.this, About.class);
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, 2000);
    }

    private boolean getLoginStatusFromPreferences()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("pref_login", Context.MODE_PRIVATE); // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();
        return pref.contains("emailid");
    }
}
