package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

//import com.example.wannago.present_poll_view.OnListFragmentInteractionListener;
//import com.example.wannago.dummy.DummyContent.DummyItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

///**
// * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
// * specified {@link OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
public class PresentPollAdapter extends RecyclerView.Adapter<PresentPollAdapter.PollHolder> {

    public final List<Polls> mValues;
    //private final OnListFragmentInteractionListener mListener;

    public PresentPollAdapter(List<Polls> items) {
        mValues = items;
        //mListener = listener;
    }

    @Override
    public PresentPollAdapter.PollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_polls_present, parent, false);
        return new PollHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PollHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        //changed here
        holder.src.setText(mValues.get(position).getSource());
        holder.dest.setText(mValues.get(position).getDestination());
        holder.price.setText(mValues.get(position).getPrice());
        holder.time.setText(mValues.get(position).getTime());
        holder.date.setText(mValues.get(position).getDate());
        holder.seats.setText("Seats Available :"+mValues.get(position).getSeats());
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),ChatActivity.class);
                i.putExtra("poll_id","p"+mValues.get(position).getPid());
                view.getContext().startActivity(i);
            }
        });


        //notifyDataSetChanged();
        holder.ej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(view.getContext(),End.class);
//                i.putExtra("poll_id","p"+mValues.get(position).getPid());
               view.getContext().startActivity(i);
            }
        });
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("profiles").child(mValues.get(position).getCreater_uid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//changed here
                holder.host.setText(dataSnapshot.child("Name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(mValues.get(position).getCreater_uid().equals(MainActivity.userId))
            holder.req.setEnabled(false);
        holder.req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("polls");
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                            Polls p = dataSnapshot1.getValue(Polls.class);
                            if (p.getPid()==mValues.get(position).getPid()) {
                                //changed here
                                mDatabase.child("p" + p.getPid()).child("requested").child(MainActivity.userId).child("user").setValue(MainActivity.userId);
                                mDatabase.child("p" + p.getPid()).child("requested").child(MainActivity.userId).child("name").setValue(MainActivity.userName);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.k.setVisibility(View.GONE);
                //mValues.remove(position);
                present_poll_view.mAdapter.notifyItemRemoved(position);
            }
        });

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/

    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class PollHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView dest;
        public final TextView price;
        public final TextView time;
        public final TextView date;
        public final TextView seats;
        public final TextView close;
        public final TextView host;
        public final Button req;
        public final Button chat;
        public final CardView k;
        public final Button ej;
        //changed here
        public final TextView src;
        public Polls mItem;

        public PollHolder(View view) {
            super(view);
            mView = view;
            dest = (TextView) view.findViewById(R.id.dest_r);
            price= (TextView) view.findViewById(R.id.price_r);
            time= (TextView) view.findViewById(R.id.time_r);
            date= (TextView) view.findViewById(R.id.date_r);
            seats=(TextView) view.findViewById(R.id.seat);
            close=(TextView)view.findViewById(R.id.close);
            host=(TextView)view.findViewById(R.id.hostby);
            k=(CardView)view.findViewById(R.id.card_r);
            req=(Button)view.findViewById(R.id.req);
            chat=(Button)view.findViewById(R.id.chat);
            ej=(Button)view.findViewById(R.id.end_journey);
            //changed here
            src=(TextView)view.findViewById(R.id.src_r);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + dest.getText() + "'";
        }
    }
}
