package com.example.appproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class request_poll_view extends Fragment implements OnListFragmentInteractionListener {

    DatabaseReference dbPolls;
    RecyclerView list;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private List<Polls_1> mPolls;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public request_poll_view() {
    }
    FirebaseAuth mFirebaseAuth;
    String user;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mFirebaseAuth=FirebaseAuth.getInstance();
        user=mFirebaseAuth.getCurrentUser().getUid();
        super.onCreate(savedInstanceState);
        dbPolls = FirebaseDatabase.getInstance().getReference("polls");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_polls_list_requests, container, false);
        list = view.findViewById(R.id.list);
        mPolls = new ArrayList<>();
        // Set the adapter
        /*if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new requestpolladapter(poll_list_1.get().getPolls(),this));
        }*/
        dbPolls.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    try {
                        final Polls_1 polls1 = postSnapshot.getValue(Polls_1.class);
                        final String parent = postSnapshot.getKey();


                        if (polls1.getStatus() != null && polls1.getStatus().equalsIgnoreCase("active") &&
                                polls1.getCreater_uid().equals(user)) {

                            postSnapshot.getRef().child("requested").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mPolls.clear();
                                    for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                                        try {
                                            //fetching values
                                            Polls_1 requested_polls = postSnapshot1.getValue(Polls_1.class);
                                            String key = postSnapshot1.getKey();
                                            requested_polls.setParent(parent);
                                            requested_polls.setKey(key);
                                            requested_polls.setCreater_uid(polls1.getCreater_uid());
                                            requested_polls.setDestination(polls1.getDestination());
                                            requested_polls.setPrice(polls1.getPrice());
                                            requested_polls.setTime(polls1.getTime());
                                            requested_polls.setDate(polls1.getDate());

                                            mPolls.add(requested_polls);
                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (mPolls != null && mPolls.size() > 0)
                                        list.setAdapter(new requestpolladapter(mPolls, request_poll_view.this));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }

                            });


                        /*//fetching requested user and name from firebase
                        try {
                            String requestName = postSnapshot.child("requested").child("name").getValue(String.class);
                            Long requestUser = postSnapshot.child("requested").child("user").getValue(Long.class);
                            if (requestName != null && requestUser != null) {
                                polls1.setName(requestName);
                                polls1.setUser(requestUser.toString());
                                polls1.setKey(key);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        //add polls1 to Adapter
                        mPolls.add(polls1);*/
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                //commentsAdapter = new CommentsAdapter(TaskDetailsActivity.this, commentsEntities);
                //if (mPolls != null && mPolls.size() > 0)
                //   list.setAdapter(new requestpolladapter(mPolls, request_poll_view.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }

    @Override
    public void onListFragmentInteraction(Polls_1 item) {
        Toast.makeText(this.getActivity(), "" + item.getId(), Toast.LENGTH_SHORT).show();
    }


}
