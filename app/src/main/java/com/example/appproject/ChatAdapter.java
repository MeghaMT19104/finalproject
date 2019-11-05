package com.example.appproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>
{
    Context context;
    List<Message> data;

    String currentUId;

    final int MY_MSG = 1, THEIR_MSG = 0;

    public ChatAdapter(Context context, List<Message> data, String currentUId) {
        this.context = context;
        this.data = data;
        this.currentUId = currentUId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout;
        if(viewType == MY_MSG)
            layout = LayoutInflater.from(context).inflate(R.layout.my_message, parent, false);
        else
            layout = LayoutInflater.from(context).inflate(R.layout.their_message, parent, false);

        return new ChatViewHolder(layout);
    }

    @Override
    public int getItemViewType(int position)
    {
        if(data.get(position).getUserId().equals(currentUId))
            return MY_MSG;
        else
            return THEIR_MSG;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        holder.name.setText(data.get(position).getName());
        holder.text.setText(data.get(position).getText());
        holder.time.setText(data.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
