package com.example.appproject;


import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
                    final AlertDialog alert = new AlertDialog.Builder(ChatActivity.this).create();
                    alert.setMessage("You don't have access to this chatroom");
                    alert.show();
                    final Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @Override
                        public void run() {
                            alert.cancel();
                            finish();
                        }
                    };
                    handler.postDelayed(runnableCode, 3000);
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

        newMsg = findViewById(R.id.writeMsg);
        sendMsg = findViewById(R.id.sendMessage);

        adapter = new ChatAdapter(this, chats, currentUId);
        chatView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        chatView.setLayoutManager(llm);
        chatView.scrollToPosition(chatView.getAdapter().getItemCount()-1);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbref.child("chatrooms").child(currentChat).child(new Date().toString()+currentUId)
                        .setValue(new Message(currentUName, newMsg.getText().toString(),currentUId, true));
                chats.add(new Message(currentUName, newMsg.getText().toString(),currentUId, true));
                adapter.notifyDataSetChanged();
                chatView.scrollToPosition(chatView.getAdapter().getItemCount()-1);

                newMsg.setText("");
            }
        });


        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Database", "Refreshing chat data");
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        DataSnapshot ds1 = ds.child("chatrooms");
                        chats.clear();
                        for(DataSnapshot dsc: ds1.child(currentChat).getChildren())
                        {
                            chats.add((Message)dsc.getValue(Message.class));
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Database",databaseError.getMessage());
                    }
                });
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnableCode);

    }
}