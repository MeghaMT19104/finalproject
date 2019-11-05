package com.example.appproject;

import android.view.View;
import android.widget.EditText;

import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

public class ChatViewHolder extends RecyclerView.ViewHolder
{
    EditText name, text, time;

    public ChatViewHolder(@NotNull View chat)
    {
        super(chat);

        name = chat.findViewById(R.id.chatname);
        text = chat.findViewById(R.id.chattext);
        time = chat.findViewById(R.id.chattime);

    }
}
