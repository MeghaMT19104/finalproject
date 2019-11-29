package com.example.appproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class editprofile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText Desc, age, ed, name;
    TextView Desc1, age1, ed1, name1,gender1,session1,status1;
    boolean chooseimage,uploadedatstorage;
    int blockcount,flag=0;
    final Context context = this;
    private static final int Camera_Request=100;
    private static final int Storage_Request=200;
    private static final int Imagepickgallery_Request=300;
    private static final int Imagepickcamera_Request=400;
    Spinner sp1,sp2,sp3,sp;
    TextView t;
    ImageView Editpic;
    String cameraPermission[];
    String storagePermission[];
    String[] options=new String[10];
    private static final int CHOOSE_IMAGE = 101;
    String  Storethisuri;
    String Storeuri,uploadin;
    GoogleApiClient mGoogleApiClient;
    private ValueEventListener vl;
    private DatabaseReference mDatabase,mDatabase1;
    private FirebaseDatabase data;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mFirebaseAuth;
    Uri mImageUri;
    TextView rating;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    FirebaseUser user;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase1=firebaseDatabase.getReference("profiles");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference data1 = mDatabase.child("users");
        mFirebaseAuth=FirebaseAuth.getInstance();
        user=mFirebaseAuth.getCurrentUser();
        //firebaseDatabase=FirebaseDatabase.getInstance();
        // mDatabase=firebaseDatabase.getReference("profiles");
        // DatabaseReference da = FirebaseDatabase.getInstance().getReference("profiles");
        Editpic=(ImageView)findViewById(R.id.Edit_pic);
        Desc = (EditText) findViewById(R.id.descriptionedit);


        age = (EditText) findViewById(R.id.ageedit);
        name = (EditText) findViewById(R.id.yournameedit);
        Desc1 = (TextView) findViewById(R.id.descriptiontext);
        age1 = (TextView) findViewById(R.id.agetext);
        name1 = (TextView) findViewById(R.id.yournametext);
        status1 = (TextView) findViewById(R.id.statustext);
        gender1 = (TextView) findViewById(R.id.gendertext);
        session1 = (TextView) findViewById(R.id.sessiontext);
        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads");
        cameraPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        rating=(TextView)findViewById(R.id.rating);
        String[] status = new String[]{
                "Student",
                "Faculty"
        };
        String[] session = new String[]{
                "BTECH-First Year",
                "BTECH-Second Year",
                "BTECH-Third Year",
                "BTECH-Fourth Year",
                "MTECH-First Year",
                "MTECH-Second Year",
                "N/A"
        };
        String[] gender = new String[]{
                "Male",
                "Female",
                "Others"
        };
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        sp1 = findViewById(R.id.statusspinner);
        ArrayAdapter<String> spinnerA = new ArrayAdapter<String>(this, R.layout.spinner1, status);
        spinnerA.setDropDownViewResource(R.layout.spinner1);
        sp1.setAdapter(spinnerA);
        sp2 = findViewById(R.id.sesionspinner);

        ArrayAdapter<String> spinnerB = new ArrayAdapter<String>(this, R.layout.spinner1, session);
        spinnerB.setDropDownViewResource(R.layout.spinner1);
        sp2.setAdapter(spinnerB);
        sp3 = findViewById(R.id.genderspinner);

        ArrayAdapter<String> spinnerC = new ArrayAdapter<String>(this, R.layout.spinner1, gender);
        spinnerC.setDropDownViewResource(R.layout.spinner1);
        sp3.setAdapter(spinnerC);


        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                //get data
                Picasso.get().load(R.drawable.abc).into(Editpic);
                String Name=""+ds.child(user.getUid()).child("Name").getValue();
                System.out.println("checking name"+Name);
                String desc=""+ds.child(user.getUid()).child("Description").getValue();
                String session=""+ds.child(user.getUid()).child("Session").getValue();
                String status=""+ds.child(user.getUid()).child("Status").getValue();
                String gender=""+ds.child(user.getUid()).child("Gender").getValue();
                System.out.println(ds.child(user.getUid()).child("ImageUri").getValue());
                String Image=""+ds.child(user.getUid()).child("ImageUri").getValue();
                String Age=""+ds.child(user.getUid()).child("Age").getValue();
                String rate="";
                String i_check = ""+null;
                try {
                    rate=ds.child(user.getUid()).child("Ratings").getValue().toString();
                }
                catch (Exception e){
                    rate="5";
                }
                System.out.println("AMEE IN" +Name);


                if(desc.equals(i_check))
                {
                }
                else
                {
                    Desc1.setText(desc);
                    Desc.setText(desc);
                }

                if(Name.equals(i_check))
                {}
                else {
                    name1.setText(Name);
                    name.setText(Name);

                }


                if(Age.equals(i_check))
                {}
                else {

                    age.setText(Age);
                    age1.setText(Age);
                }
                if(status.equals(i_check))
                {}
                else {
                    status1.setText(status);
                    sp1.setSelection(status.indexOf(status));
                }
                if(gender.equals(i_check))
                {
                }
                else
                {
                    gender1.setText(gender);
                    sp3.setSelection(gender.indexOf(gender));
                }
                if(session.equals(i_check))
                {
                }
                else
                {
                    session1.setText(session);
                    sp2.setSelection(session.indexOf(session));
                }


                Storethisuri = Image;
                rating.setText(rate);
                // findViewById(R.id.sesionspinner).setSeletion(2);


                if(Image.equals(i_check))
                {//if image is there
                    Picasso.get().load(R.drawable.abc).into(Editpic);

                    // System.out.println("your image is stored here" +Uri);
                }
                else
                { //if not there set an defalut image
                    Picasso.get().load(Image).into(Editpic);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;


        // sp = findViewById(R.id.blockspinner);

        //   ArrayAdapter<String> spinnerD = new ArrayAdapter<String>(this, R.layout.spinner1, options);
//        spinnerD.setDropDownViewResource(R.layout.spinner1);
//        sp.setAdapter(spinnerD);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference demoRef = rootRef.child("users");
        findViewById(R.id.pen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit(view);

            }
        });
        findViewById(R.id.butonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();

            }
        });
        findViewById(R.id.Edit_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogbox();
            }
        });
    }


    private void showdialogbox()
    {
        String Items[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(editprofile.this);
        builder.setTitle("Choose From");
        builder.setItems(Items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle dialog items
                if (i == 0)
                {
                    //Choose Camera
                    //pd.addContentView();
                    if(!checkcameraPermission())
                    {
                        requestcameraPermission();
                    }
                    else
                    {
                        pickfromcamera();
                    }
                }
                else
                {
                    //choose gallery
                    if(!checkstoragePermission())
                    {
                        requestStoragePermission();
                    }
                    else
                    {
                        pickfromgallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private boolean checkstoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(editprofile.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(editprofile.this,storagePermission,Storage_Request);
    }
    private boolean checkcameraPermission()
    {
        boolean result=ContextCompat.checkSelfPermission(editprofile.this,
                Manifest.permission.CAMERA )==(PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(editprofile.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void pickfromgallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Imagepickgallery_Request);

    }

    private void pickfromcamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        //put image uri
        mImageUri=(editprofile.this).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //intent to start camera
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);
        startActivityForResult(cameraIntent,Imagepickcamera_Request);
    }
    private void requestcameraPermission()
    {
        ActivityCompat.requestPermissions(editprofile.this,cameraPermission,Camera_Request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Imagepickgallery_Request) {
                mImageUri = data.getData();
                //  System.out.println("OnActivityresult"+(mImageUri.toString()));
                uploadPhotoinstorage(mImageUri);
            }
            if (requestCode == Imagepickcamera_Request) {
                uploadPhotoinstorage(mImageUri);

            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case Camera_Request:
            {
                if(grantResults.length>0)
                {
                    boolean cameraAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writestorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writestorageAccepted)
                    {
                        pickfromcamera();
                    }
                    else
                    {
                        Toast.makeText(editprofile.this,"Please Enable camera and storage permission",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case Storage_Request:
            {
                if(grantResults.length>0)
                {

                    // boolean writestorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITESTORAGE=grantResults[1]==PackageManager.PERMISSION_GRANTED;

                    if(WRITESTORAGE)
                    {
                        pickfromgallery();
                    }
                    else
                    {
                        Toast.makeText(editprofile.this,"Please Enable storage permission",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    private void uploadPhotoinstorage(final android.net.Uri uri) {
        final     String filePathName=user.getUid();
        StorageReference storageReference2nd = mStorageRef.child("User"+"Profilepic"+filePathName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        System.out.println("in upload"+(uri));
                        System.out.println("User"+"Profilepic"+ filePathName);
                        //check if uploaded ornot

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();
                        if(uriTask.isSuccessful()){
                            // mDatabase.child(user.getUid()).updateChildren(res)
                            uploadin= downloadUri.toString();
                            HashMap<String,Object> results = new HashMap<>();
                            uploadedatstorage=true;
                            results.put("ImageUri",downloadUri.toString());
                            mDatabase1.child(user.getUid()).updateChildren(results)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });

                        }
                        else {//error
                            Toast.makeText(editprofile.this,"Some Error Occured",Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editprofile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
    protected boolean check(final String  z, final String u){
        if(u.equals(z))
        {
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editprofile, menu);
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
                                    Intent i = new Intent(editprofile.this, LoginActivity.class);
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
    public void submit() {
        String Session, Description, Age, Status, Gender, Name;
        sp = findViewById(R.id.sesionspinner);
        Session = sp.getSelectedItem().toString();

        sp = findViewById(R.id.genderspinner);
        Gender = sp.getSelectedItem().toString();

        if(Desc.getText().toString()== Desc1.getText().toString())
        {
            Description=Desc1.getText().toString();
        }
        else
        {
            Description=Desc.getText().toString();
        }
        if(name.getText().toString() == name1.getText().toString())
        {
            Name=name1.getText().toString();
        }
        else {
            Name = name.getText().toString();
        }
        if(age.getText().toString() == age1.getText().toString())
        {
            Age=age1.getText().toString();
        }
        else
        {
            Age= age.getText().toString();
        }
        sp = findViewById(R.id.statusspinner);
        Status = sp.getSelectedItem().toString();


        if(Name.isEmpty())
        {
            name.setError("Your name");
            name.requestFocus();
            return;
        }
        if (Description.isEmpty()) {
            Desc.setError("Tell people something about yourself");
            Desc.requestFocus();
            return;
        }
        if (Age.isEmpty()) {
            age.setError("Age is left blank");
            age.requestFocus();
            return;
        }
        try {
            // checking valid integer using parseInt() method
            Integer.parseInt(Age);
            if (Integer.parseInt(Age) <= 15) {
                age.setError("your age must be >15");
                age.requestFocus();
                return;
            }
            if (Integer.parseInt(Age) >= 100) {
                age.setError("you can't be that old");
                age.requestFocus();
                return;
            }
        }
        catch (NumberFormatException e)
        {
            age.setError("Age should be integer");
            age.requestFocus();
            return;
        }
        if(uploadedatstorage==true  )
        {
            Storeuri=uploadin;
        }
        else
        {
            Storeuri= Storethisuri;
        }

        Profiledetails profile = new Profiledetails(Storeuri,Name, Description, Session, Status, Gender, Age);
        Log.d("profile", "profile details");
        FirebaseDatabase.getInstance().getReference("profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(profile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();

                            Toast.makeText(editprofile.this, "Data saved", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(editprofile.this, editprofile
                                    .class));
                        } else {
                            //failure
                            Toast.makeText(editprofile.this, "Profile details not saved", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(editprofile.this, editprofile
                                    .class));
                        }
                    }
                });
    }

    protected void edit(View view) {

        t = findViewById(R.id.yournametext);
        t.setVisibility(View.GONE);
        ed = findViewById(R.id.yournameedit);
        ed.setVisibility(View.VISIBLE);
        t = findViewById(R.id.descriptiontext);
        t.setVisibility(View.GONE);
        ed = findViewById(R.id.descriptionedit);
        ed.setVisibility(View.VISIBLE);
        t = findViewById(R.id.statustext);
        t.setVisibility(View.GONE);
        sp = findViewById(R.id.statusspinner);
        sp.setVisibility(View.VISIBLE);
        t = findViewById(R.id.sessiontext);
        t.setVisibility(View.GONE);
        sp = findViewById(R.id.sesionspinner);
        sp.setVisibility(View.VISIBLE);
        t = findViewById(R.id.gendertext);
        t.setVisibility(View.GONE);
        sp = findViewById(R.id.genderspinner);
        sp.setVisibility(View.VISIBLE);
        t = findViewById(R.id.agetext);
        t.setVisibility(View.GONE);
        ed = findViewById(R.id.ageedit);
        ed.setVisibility(View.VISIBLE);
        Button bt = findViewById(R.id.butonSave);
        bt.setVisibility(View.VISIBLE);

    }
    protected void rem(DatabaseReference d,ValueEventListener kl){
        d.removeEventListener(kl);
    }

    public void send(View view) {
        Intent inte = new Intent(this, editprofile.class);
        startActivity(inte);
    }


    protected void block(View V){
        final Object[] v = new Object[1];
        String b = new String();
        final String[] kl=new String[1];
        // sp = findViewById(R.id.blockspinner);
        final String value = sp.getSelectedItem().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference d2=mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockList");
        flag=0;



        final DatabaseReference data1 = mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        vl = data1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() < 3) {
                    kl[0] = "User" + 1;
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockCount").setValue(1);
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockList").child(kl[0]).setValue(value);
                    rem(data1, vl);
                    return;

                } else {
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {

                        String k = locationSnapshot.getKey();
                        if (k.equals("BlockCount")) {
                            blockcount = Integer.parseInt(locationSnapshot.getValue().toString());

                        }
                        if (k.equals("BlockList")) {
                            flag = 0;
                            for (DataSnapshot gh : locationSnapshot.getChildren()) {
                                String df = gh.getValue().toString();
                                boolean t = check(df, value);
                                if (t == true) {


                                    rem(data1, vl);
                                    Toast.makeText(context, "Already blocked!! Have Fun!!! ", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    flag = 1;
                                }

                            }

                            if (flag == 1) {
                                Log.d("galti", locationSnapshot.getValue().toString());
                                DatabaseReference d = locationSnapshot.getRef();
                                int h = blockcount + 1;
                                kl[0] = "User" + h;
                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockCount").setValue(h);

                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockList").child(kl[0]).setValue(value);
                                rem(data1, vl);
                                return;
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
}