package com.example.appproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //variables
    private Button past;
    private Button present;
    private Button map;
    public static int frag=0;
    public static FragmentManager fm;
    public static Fragment f;
    static int con=0;
    public static String userId="";
    public static String userName="U1";
    public static String status="Student";
    public static String gender="Male";
    public static String session="BTECH-First Year";
    public static int age=40;
    final Context context = this;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            userId = user.getUid();
            System.out.println("user id :"+userId);
            DatabaseReference mDatabase2;
            mDatabase2 = FirebaseDatabase.getInstance().getReference().child("profiles").child(userId);
            mDatabase2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    userName = dataSnapshot.child("Name").getValue().toString();
                    status = dataSnapshot.child("Status").getValue().toString();
                    gender=dataSnapshot.child("Gender").getValue().toString();
                    session=dataSnapshot.child("Session").getValue().toString();
                    age=Integer.parseInt(dataSnapshot.child("Age").getValue().toString());
                    System.out.println(userName+status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //my code starts
        fm=getSupportFragmentManager();
        f=fm.findFragmentById(R.id.fragment_container);

        if(f==null){
            f=new present_poll_view();
            fm.beginTransaction().add(R.id.fragment_container,f).commit();
            frag=0;
            //onCreate(savedInstanceState);
        }
        past=(Button)findViewById(R.id.past_button);
        present=(Button)findViewById(R.id.present_button);
        map=(Button)findViewById(R.id.map_button);
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().remove(f).commit();
                f=new request_poll_view();
                fm.beginTransaction().add(R.id.fragment_container,f).commit();
                MainActivity.frag=1;

            }
        });
        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().remove(f).commit();
                f=new present_poll_view();
                fm.beginTransaction().add(R.id.fragment_container,f).commit();
                MainActivity.frag=0;

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), locattion.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_edit:
                Intent i = new Intent(this, editprofile.class);
                startActivity(i);
                return true;

            case R.id.nav_about:
                Intent i1 = new Intent(this, about1.class);
                startActivity(i1);
                return true;

            case R.id.nav_logout:
                if(GoogleSignIn.getLastSignedInAccount(context) != null) {

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // ...
                                    Toast.makeText(getApplicationContext(), "Logged Out google", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                }
                            });
                }
                else
                {
                    Intent i2 = new Intent(this, LoginActivity.class);
                    startActivity(i2);
                }
                return true;
            case R.id.nav_request:
                Intent i3=new Intent(this,MainActivity.class);
                startActivity(i3);
                return true;
         /*   case R.id.nav_past:
                Intent i4=new Intent(this,past_poll_activity.class);
                startActivity(i4);
                return true;
                */
            default:
                return true;
        }
    }
}
