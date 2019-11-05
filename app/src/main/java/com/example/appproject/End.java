package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class End extends AppCompatActivity {

    final Context context = this;
    private DatabaseReference mDatabase;
    DataSnapshot poll_data;
    int blockcount,flag=0;
    ValueEventListener vl;
    String poll=null;
    String[] uids = new String[3];
    String[] user = new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference data2 = mDatabase.child("polls");
        data2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag =0;

                System.out.println("Function is called");
                poll_data = dataSnapshot;
                for (DataSnapshot da : dataSnapshot.getChildren()){
                    for( DataSnapshot da2 :da.child("Added_users").getChildren()){
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(da2.getKey())){

                            String p =da.child("status").getValue().toString();
                            System.out.println("Status is : "+p);
                            if (p.equals("active")){
                                poll = da.getKey();
                                break;
                            }

                        }
                    }
                }
                System.out.println("poll name : "+poll);
                for (DataSnapshot da : poll_data.getChildren()){
                    System.out.println("poll id : "+da.getKey());
                    if (poll!=null && poll.equals(da.getKey())){

                        int i =0;
                        for( DataSnapshot da2 :da.child("Added_users").getChildren()){
                            if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(da2.getKey())){
                                user[i] = da2.child("name").getValue().toString();
                                uids[i] = da2.getKey();
                                System.out.println("found");
                                i++;
                            }
                        }
                    }
                }
                TextView t;
                if(user[0] != null){
                    t =findViewById(R.id.user1);
                    t.setText(user[0]);
                    findViewById(R.id.l1).setVisibility(View.VISIBLE);
                    findViewById(R.id.l11).setVisibility(View.VISIBLE);
                }
                if(user[1] != null){
                    t = findViewById(R.id.user2);
                    t.setText(user[1]);
                    findViewById(R.id.l2).setVisibility(View.VISIBLE);
                    findViewById(R.id.l22).setVisibility(View.VISIBLE);
                }
                if (user[2] != null){
                    t = findViewById(R.id.user3);
                    t.setText(user[2]);
                    findViewById(R.id.l3).setVisibility(View.VISIBLE);
                    findViewById(R.id.l33).setVisibility(View.VISIBLE);
                }
                if (user[1] == null && user[0] ==null && user[2]==null){
                    Toast.makeText(context,"You cannot end this journey",Toast.LENGTH_LONG).show();
                    Intent inte = new Intent(context,MainActivity.class);
                    startActivity(inte);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //rem(data2, vl);
        setContentView(R.layout.activity_end);
        Button bt =(Button) findViewById(R.id.block1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                block(uids[0]);
            }
        });
        bt =(Button) findViewById(R.id.block2);
                bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                block(uids[1]);
            }
        });
        bt = findViewById(R.id.block3);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                block(uids[2]);
            }
        });
        findViewById(R.id.rate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ed = findViewById(R.id.rateed1);
                int r = Integer.parseInt(ed.getText().toString());
                rate(r, uids[0]);
            }
        });
        findViewById(R.id.rate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ed = findViewById(R.id.rateed2);
                int r = Integer.parseInt(ed.getText().toString());
                rate(r, uids[1]);
            }
        });
        findViewById(R.id.rate3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ed = findViewById(R.id.rateed3);
                int r = Integer.parseInt(ed.getText().toString());
                rate(r, uids[2]);
            }
        });

    }
    protected void rem(DatabaseReference d,ValueEventListener kl){
        d.removeEventListener(kl);
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
    protected void rate(final int r,final String u){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profiles");
        final DatabaseReference data2 = mDatabase.child(u);
        vl = data2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("RateCount").getValue() == null){
                    FirebaseDatabase.getInstance().getReference("profiles").child(u).child("RateCount").setValue(1);
                    FirebaseDatabase.getInstance().getReference("profiles").child(u).child("Ratings").setValue(r);
                    rem(data2,vl);
                }
                else{
                    int rc =Integer.parseInt(dataSnapshot.child("RateCount").getValue().toString());
                    int rt = Integer.parseInt(dataSnapshot.child("Ratings").getValue().toString());
                    rt = rt*rc;
                    rt = rt+r;
                    rt = rt/(rc+1);
                    FirebaseDatabase.getInstance().getReference("profiles").child(u).child("RateCount").setValue(rc+1);
                    FirebaseDatabase.getInstance().getReference("profiles").child(u).child("Ratings").setValue(rt);
                    rem(data2,vl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    protected void block(final String u){
        final Object[] v = new Object[1];
        String b = new String();
        final String[] kl=new String[1];
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
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockList").child(kl[0]).setValue(u);
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
                                boolean t = check(df, u);
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

                                FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BlockList").child(kl[0]).setValue(u);
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

    public void close(View view) {
        FirebaseDatabase.getInstance().getReference().child("polls").child(poll).child("status").setValue("inactive");
        Intent inte = new Intent(this,MainActivity.class);
        startActivity(inte);
    }
}
