package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    DatabaseReference dbPolls;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    public int permissionlocation = 99;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser user;
    String polls;
    DataSnapshot poll_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbPolls = FirebaseDatabase.getInstance().getReference("polls");


        setContentView(R.layout.activity_maps);
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference data2 = mDatabase.child("polls");

        data2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0;
                Log.i("123", "krishna");
                System.out.println("Function is called");
                poll_data = dataSnapshot;
                for (DataSnapshot da : dataSnapshot.getChildren()) {

                    for (DataSnapshot da2 : da.child("Added_users").getChildren()) {
                        System.out.println("called");
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(da2.getKey())) {
                            System.out.println("called1");
                            String p = da.child("status").getValue().toString();
                            System.out.println("Status is : " + p);
                            if (p.equals("active")) {
                                polls = da.getKey();
                                Log.i("123", "era"+polls);
                                break;
                            }

                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        permissionlocation);
                Log.i("MapsActivity2", "requestlocation2: ");

            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        Log.i("MapsActivity", "requestlocation: ");
        if (requestCode == permissionlocation) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);

            } else {
                Toast t = Toast.makeText(getApplicationContext(), "permission Denied", Toast.LENGTH_SHORT);
            }
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());

                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }

                mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override

                    public void onLocationResult(LocationResult locationResult) {
                        DatabaseReference mDatabase;
                        Log.i("123", "polls");
                        Location location = locationResult.getLastLocation();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("polls")
                                .child(polls).child("Added_users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Log.d("123","location");
                        if (location != null) {
                            //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude()); for adding longitude to
                            ref.child("latitude").setValue(location.getLatitude());
                            ref.child("longitude").setValue(location.getLongitude());

                        }

                    }
                }, null);

                //Place current location marker
//getting loc frm db
               /* final DataSnapshot postSnapshot;
                final Polls_1 polls=postSnapshot.getValue(Polls_1.class)
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("polls");
                if (polls.getStatus() != null && polls.getStatus().equalsIgnoreCase("active") &&
                        polls.getCreater_uid().equals(user))


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("polls").child("p2019-10-31T20:52:41Z").child("Added_users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {


                            //Toast.makeText(L_Location_Activity.this,"for",Toast.LENGTH_LONG).show();

                            String latitude_Display = ds.child("location")
                                    .child("latitude")
                                    .getValue().toString();

                            String longitude_Display = ds.child("location")
                                    .child("longitude")
                                    .getValue().toString();


                            String latLng = latitude_Display;
                            String latLng1 = longitude_Display;


                            double latitude = Double.parseDouble(latLng);
                            double longitude = Double.parseDouble(latLng1);

                            // map.clear();
                            LatLng currentLocation = new LatLng(latitude, longitude);

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(currentLocation);
                            markerOptions.title("i'm here").icon(BitmapDescriptorFactory.defaultMarker());
                            mGoogleMap.addMarker( markerOptions );
                                                          mGoogleMap.addMarker(new MarkerOptions()
                                                                  .position(currentLocation)
                                                                  .title("current pos"));
                                                         markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                                                          mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }*/



                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
              /*  DatabaseReference ref = FirebaseDatabase.getInstance().getReference("polls")
                        .child("p1").child("Added_users").child("Personm1").child("location").child("latitude");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            //Toast.makeText(L_Location_Activity.this,"for",Toast.LENGTH_LONG).show();

                            String latitude_Display = ds
                                    .child("latitude")
                                    .getValue().toString();

                            String longitude_Display = ds
                                    .child("longitude")
                                    .getValue().toString();


                            String latLng = latitude_Display;
                            String latLng1 = longitude_Display;


                            double latitude = Double.parseDouble(latLng);
                            double longitude = Double.parseDouble(latLng1);

                            // map.clear();
                            LatLng currentLocation = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(currentLocation);
                            //markerOptions.title("i'm here");
                            //map.addMarker( markerOptions );
                            GoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title("Hello world"));
                        }
                    }
                }
                */


            }
        }
    };
}