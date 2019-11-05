package com.example.appproject;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class requestpolladapter extends RecyclerView.Adapter<requestpolladapter.PollHolder> {

    private final List<Polls_1> mValues;
    private final OnListFragmentInteractionListener mListener;

    public requestpolladapter(List<Polls_1> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public PollHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_polls_requests, parent, false);
        return new PollHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PollHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.dest.setText("" + mValues.get(position).getDestination());
        holder.price.setText("Rs." + mValues.get(position).getPrice());
        holder.time.setText("Time :" + mValues.get(position).getTime());
        holder.date.setText("Date :" + mValues.get(position).getDate());
        holder.name.setText("" + mValues.get(position).getName());

        //holder.seats.setText("Seats Available :"+mValues.get(position).getSeat());

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            inp(mValues.get(position).getUser(),view);

            }
        });


        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(mValues.get(position));

                    final long[] count = {-1};

                    //Database reference for Creater details
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("polls");

                    Map<String, String> creater = new HashMap<>();
                    creater.put("name", null);
                    creater.put("user", "" + mValues.get(position).getCreater_uid());
                    creater.put("poll", "" + mValues.get(position).getParent());

                    databaseReference2.child("" + mValues.get(position).getParent()).child("Added_users").child("" + mValues.get(position).getCreater_uid()).setValue(creater);

                    //Database reference for Added_users details
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("polls");

                    databaseReference.child("" + mValues.get(position).getParent()).child("Added_users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count[0] = dataSnapshot.getChildrenCount();
                            if (count[0] > -1 && count[0] <= 3) {
                                //Storing Added Users in Firebase
                                Map<String, String> Added_users = new HashMap<>();
                                Added_users.put("name", "" + mValues.get(position).getName());
                                Added_users.put("user", "" + mValues.get(position).getUser());
                                Added_users.put("poll", "" + mValues.get(position).getParent());
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("polls");
                                databaseReference1.child("" + mValues.get(position).getParent()).child("Added_users").child("" + mValues.get(position).getUser()).setValue(Added_users);
                                Toast.makeText(v.getContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext(), "You Exceed Maximum Limit, Cannot Add More Than 3 Persons", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    /*databaseReference.child("" + mValues.get(position).getParent()).child("Added_users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count[0] = dataSnapshot.getChildrenCount();

                            if (count[0] > -1 && count[0] < 3) {
                                //Storing Added Users in Firebase
                                Map<String, String> Added_users = new HashMap<>();
                                Added_users.put("name", "" + mValues.get(position).getName());
                                Added_users.put("user", "" + mValues.get(position).getUser());
                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("polls");
                                databaseReference1.child("" + mValues.get(position).getParent()).child("Added_users").child("Person" + mValues.get(position).getKey()).setValue(Added_users);
                            } else {
                                Toast.makeText(v.getContext(), "You Exceed Maximum Limit, Cannot Add More Than 3 Persons", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/



                }
            }
        });

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
        public final Button btnAdd;
        public final Button profile;
        public TextView name;
        public Polls_1 mItem;
        String added_name;
        String added_user;

        public PollHolder(View view) {
            super(view);
            mView = view;
            dest = (TextView) view.findViewById(R.id.dest_r);
            price = (TextView) view.findViewById(R.id.price_r);
            time = (TextView) view.findViewById(R.id.time_r);
            date = (TextView) view.findViewById(R.id.date_r);
            seats = (TextView) view.findViewById(R.id.seat);
            name = (TextView) view.findViewById(R.id.seat3);
            btnAdd = view.findViewById(R.id.btnAdd);
            profile = view.findViewById(R.id.profile);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dest.getText() + "'";
        }
    }
    protected void inp(final String u,final View v){
        DatabaseReference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference data2 = mDatabase.child("profiles");
        final ValueEventListener gh = data2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren() ){
                    if(u.equals(d.getKey())){
                        LayoutInflater li = LayoutInflater.from(v.getContext());
                        final View promptsView = li.inflate(R.layout.profile_dialog, null);
                        TextView t = promptsView.findViewById(R.id.name);
                        t.setText(d.child("Name").getValue().toString());
                        t = promptsView.findViewById(R.id.desc);
                        t.setText(d.child("Description").getValue().toString());
                        t = promptsView.findViewById(R.id.ag);
                        t.setText(d.child("Age").getValue().toString());
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(v.getContext());
                        builder.setTitle("Profile");

                        builder.setCancelable(false);
                        builder.setView(promptsView);
                        builder
                                .setNegativeButton(
                                        "OK",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

                                                // If user click no
                                                // then register_dialog box is canceled.

                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
