package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.wannago.past_poll_view.OnListFragmentInteractionListener;
//import com.example.wannago.dummy.DummyContent.DummyItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

///**
// * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
// * specified {@link OnListFragmentInteractionListener}.
// * TODO: Replace the implementation with code for your data type.
// */
public class PastPollAdapter extends RecyclerView.Adapter<PastPollAdapter.PollHolder> {

    private final List<Polls> mValues;
    //private final OnListFragmentInteractionListener mListener;

    public PastPollAdapter(List<Polls> items) {
        mValues = items;
        //mListener = listener;
    }

    @Override
    public PollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_polls_past, parent, false);
        return new PollHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PollHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.dest.setText(mValues.get(position).getDestination());
        holder.price.setText(mValues.get(position).getPrice());
        holder.time.setText(mValues.get(position).getTime());
        holder.date.setText(mValues.get(position).getDate());
        holder.seats.setText("Seats Available :"+mValues.get(position).getSeats());
        DatabaseReference mds1,mds2;
        mds1= FirebaseDatabase.getInstance().getReference().child("polls").child("p"+mValues.get(position).getPid()).child("Added_users");
        mds1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    System.out.println(ds.child("name").getValue().toString());
                    holder.seat1[i++].setText(ds.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mds1.child(MainActivity.userId).child("user").setValue(MainActivity.userId);



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
        public final TextView seat1[]=new TextView[3];
        //public final TextView seat4;
        public final TextView seats;
        public Polls mItem;

        public PollHolder(View view) {
            super(view);
            mView = view;
            dest = (TextView) view.findViewById(R.id.dest_r);
            price= (TextView) view.findViewById(R.id.price_r);
            time= (TextView) view.findViewById(R.id.time_r);
            date= (TextView) view.findViewById(R.id.date_r);
            seats=(TextView) view.findViewById(R.id.seat);
            seat1[0]=(TextView)view.findViewById(R.id.seat1);
            seat1[1]=(TextView)view.findViewById(R.id.seat2);
            seat1[2]=(TextView)view.findViewById(R.id.seat3);
            //seat4=(TextView)view.findViewById(R.id.seat4);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dest.getText() + "'";
        }
    }
}
