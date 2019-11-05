package com.example.appproject;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatView;
    ChatAdapter adapter;
    List<Message> chats;

    EditText newMsg;
    ImageButton sendMsg;
    DatabaseReference dbref;

    String currentUName, currentUId, currentChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentChat = getIntent().getStringExtra("poll_id");

        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                DataSnapshot ds1 = ds.child("polls").child(currentChat).child("Added_users");
                boolean flagUser = false;
                for(DataSnapshot dsc: ds1.getChildren())
                {
                    Log.d("Database",dsc.getKey().toString());
                    if(currentUId.equals(dsc.getKey().toString()))
                    {
                        flagUser = true;
                        break;
                    }
                }
                if(!flagUser && !currentUId.equals(ds.child("polls").child(currentChat).child("creater_uid").getValue(String.class)))
                {
                    Toast.makeText(getApplicationContext(),"You do not have access to this chatroom",Toast.LENGTH_SHORT);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database",databaseError.getMessage());
            }
        });



        setContentView(R.layout.activity_chat);



        chatView = findViewById(R.id.chats);
        chats = new ArrayList<>();

        newMsg = findViewById(R.id.writeMsg);
        sendMsg = findViewById(R.id.sendMessage);

        newMsg = findViewById(R.id.writeMsg);
        sendMsg = findViewById(R.id.sendMessage);


        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                DataSnapshot ds1 = ds.child("chatrooms");
                for(DataSnapshot dsc: ds1.child(currentChat).getChildren())
                {
                    Log.d("Database",dsc.getValue().toString());
                    chats.add((Message)dsc.getValue(Message.class));
                    adapter.notifyDataSetChanged();
                }
                currentUName = ds.child("profiles").child(currentUId).child("Name").getValue(String.class);
                Log.d("Database","Name is "+currentUName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Database",databaseError.getMessage());
            }
        });


        adapter = new ChatAdapter(this, chats, currentUId);
        chatView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        chatView.setLayoutManager(llm);
        chatView.scrollToPosition(chatView.getAdapter().getItemCount()-1);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chats.add(new Message(currentUName, newMsg.getText().toString(),currentUId, true));
                adapter.notifyDataSetChanged();
                chatView.scrollToPosition(chatView.getAdapter().getItemCount()-1);
                dbref.child("chatrooms").child(currentChat).child(currentUId+ new Date().toString())
                        .setValue(new Message(currentUName, newMsg.getText().toString(),currentUId, true));


                newMsg.setText("");
            }
        });

    }
}