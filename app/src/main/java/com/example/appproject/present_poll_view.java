package com.example.appproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.example.wannago.dummy.DummyContent.DummyItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class present_poll_view extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static TextView time;
    private static TextView date;
    private static TextView price;
    private EditText source;
    private Button filter;
    private TextView close;
    private EditText dest;
    private EditText max_Age;
    public static PresentPollAdapter mAdapter;
    public static RecyclerView recyclerView;
    public static AppCompatSpinner sp[]=new AppCompatSpinner[3];
    public static ArrayList<Polls> present_poll_list=new ArrayList<>();
    public static boolean flg=false;
    public static ArrayList<String> block_list=new ArrayList<>();
    //private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //poll_list.get().getPolls().clear();

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        DatabaseReference mDatabase1;
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("users").child(MainActivity.userId).child("BlockList");
        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    block_list.add(ds.getValue().toString());
                    System.out.println(block_list.get(block_list.size()-1));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        View view = inflater.inflate(R.layout.fragment_polls_list_present, container, false);
        FirebaseApp.initializeApp(view.getContext());
        // Set the adapter
        if (view.findViewById(R.id.list) instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //poll_list.get().getPolls().clear();
            set_present_poll_list();
            mAdapter=new PresentPollAdapter(present_poll_list);
            recyclerView.setAdapter(mAdapter);
        }
        price=(TextView)view.findViewById(R.id.price);
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pr=price();
                price.setText(String.valueOf(pr));
            }
        });
        time=(TextView) view.findViewById(R.id.time);
        //time_p=(TextView)view.findViewById(R.id.time_p);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                Context cn=getContext();
                TimePickerDialog timePickerDialog = new TimePickerDialog(cn,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            //changed here
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(hourOfDay<10) {
                                    if (minute<10)
                                        time.setText("0" + hourOfDay + ":" + "0"+minute);
                                    else
                                        time.setText("0" + hourOfDay + ":" +minute);
                                }
                                else{
                                    if (minute<10)
                                        time.setText(hourOfDay + ":" + "0"+minute);
                                    else
                                        time.setText(hourOfDay + ":" +minute);
                                }
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        date=(TextView) view.findViewById(R.id.date);
        //date_p=(TextView)view.findViewById(R.id.date_p) ;
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        close=(TextView)view.findViewById(R.id.close);
        final CardView k=(CardView)view.findViewById(R.id.card);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                k.setVisibility(View.GONE);
            }
        });
        //changed here
        source=view.findViewById(R.id.src);
        Button add=(Button)view.findViewById(R.id.add_button);
        dest=(EditText)view.findViewById(R.id.des);
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            //changed here
            public void onClick(View view) {
                if (date.getText().toString().equals("Date") || time.getText().toString().equals("Time")) {
                    Toast.makeText(getContext(), "Enter correct Date and Time", Toast.LENGTH_LONG).show();
                } else {
                    if (searchFromLocationName(dest.getText().toString())) {
                        if (searchFromLocationName(source.getText().toString())) {
                            int sh = 0;
                            for (int j = 0; j < present_poll_list.size(); j++) {
                                if (MainActivity.userId.equals(present_poll_list.get(j).getCreater_uid())) {
                                    sh = 1;
                                    break;
                                }
                            }
                            if (sh == 0) {
                                Polls p = new Polls(dest.getText().toString(), "200", time.getText().toString(), date.getText().toString(), MainActivity.userId, " ", " ", " ", 200, source.getText().toString());
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("polls");
                                mDatabase.child("p" + p.getPid()).setValue(p);
                                mDatabase.child("p" + p.getPid()).child("Added_users").child(MainActivity.userId).child("name").setValue(MainActivity.userName);
                                mDatabase.child("p" + p.getPid()).child("Added_users").child(MainActivity.userId).child("user").setValue(MainActivity.userId);
                                poll_list.makeNull();
                                set_present_poll_list();
                                //mAdapter.notifyItemInserted(present_poll_list.size()-1);
                                mAdapter = new PresentPollAdapter(present_poll_list);
                                recyclerView.setAdapter(mAdapter);
                                Toast.makeText(getContext(), "Poll Added Successfully", Toast.LENGTH_SHORT).show();
                                //onCreate(savedInstanceState);
                            } else {
                                Toast.makeText(getContext(), "You Already have an ACTIVE Poll", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Source not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Destination not Found", Toast.LENGTH_SHORT).show();
                    }

                }

            }

        });
        filter=(Button)view.findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            //changed here
            public void onClick(View view) {
                if(date.getText().toString().equals("Date") || time.getText().toString().equals("Time")){
                    Toast.makeText(getContext(), "Enter correct Date and Time", Toast.LENGTH_LONG).show();
                }
                else {
                    if (searchFromLocationName(dest.getText().toString())) {
                        if (searchFromLocationName(source.getText().toString())) {
                            int sh = 0;
                            for (int j = 0; j < present_poll_list.size(); j++) {
                                if (MainActivity.userId.equals(present_poll_list.get(j).getCreater_uid())) {
                                    sh = 1;
                                    break;
                                }
                            }
                            if (sh == 0)
                                fil(getView());
                            else {
                                Toast.makeText(getContext(), "You Already have an ACTIVE Poll", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Source not Found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Destination not Found", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return view;
        //card

    }
    protected void fil(View v){
        AlertDialog alertDialog;
        LayoutInflater li = LayoutInflater.from(v.getContext());
        final View promptsView = li.inflate(R.layout.dialog_filters, null);
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(v.getContext());
        builder.setTitle("Filters");
        builder.setCancelable(false);
        sp[0] = promptsView.findViewById(R.id.Fstatusspinner);
        String[] status = new String[]{
                "Student",
                "Faculty"
        };
        ArrayAdapter<String> spinnerA = new ArrayAdapter<String>(v.getContext(),R.layout.spinner2,status);
        spinnerA.setDropDownViewResource(R.layout.spinner2);
        sp[0].setAdapter(spinnerA);
        sp[1] = promptsView.findViewById(R.id.Fsesionspinner);
        String[] session = new String[]{
                "BTECH-First Year",
                "BTECH-Second Year",
                "BTECH-Third Year",
                "BTECH-Fourth Year",
                "MTECH-First Year",
                "MTECH-Second Year",
                "N/A"
        };
        ArrayAdapter<String> spinnerB = new ArrayAdapter<String>(v.getContext(),R.layout.spinner2,session);
        spinnerB.setDropDownViewResource(R.layout.spinner2);
        sp[1].setAdapter(spinnerB);
        sp[2] = promptsView.findViewById(R.id.Fgenderspinner);
        String[] gender = new String[]{
                "Male",
                "Female",
                "Others"
        };
        ArrayAdapter<String> spinnerC = new ArrayAdapter<String>(v.getContext(),R.layout.spinner2,gender);
        spinnerC.setDropDownViewResource(R.layout.spinner2);
        sp[2].setAdapter(spinnerC);
        builder.setView(promptsView);
        max_Age=(EditText) promptsView.findViewById(R.id.ageedit);
        //Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String type=sp[0].getSelectedItem().toString();
                String gender=sp[2].getSelectedItem().toString();
                String session=sp[1].getSelectedItem().toString();
                int age=Integer.parseInt(max_Age.getText().toString());
                //changed here
                Polls p= new Polls(dest.getText().toString(),"200",time.getText().toString(),date.getText().toString(),MainActivity.userId,gender,type,session,age,source.getText().toString());
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("polls");
                //poll_list.get().getPolls().clear();
                // MainActivity.con=poll_list.get().getPolls().size();
                mDatabase.child("p"+p.getPid()).setValue(p);
                mDatabase.child("p"+p.getPid()).child("Added_users").child(MainActivity.userId).child("name").setValue(MainActivity.userName);
                mDatabase.child("p"+p.getPid()).child("Added_users").child(MainActivity.userId).child("user").setValue(MainActivity.userId);
                poll_list.makeNull();
                set_present_poll_list();
                mAdapter=new PresentPollAdapter(present_poll_list);
                recyclerView.setAdapter(mAdapter);
                //mAdapter.notifyDataSetChanged();
                //MainActivity.con=poll_list.get().getPolls().size();
                //System.out.println(MainActivity.con);


            }
        });
        builder
                .setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then register_dialog box is canceled.

                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();

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
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onListFragmentInteraction(DummyItem item);
    }
    public static void set_present_poll_list(){
        present_poll_list.clear();
        List<Polls> temp=poll_list.get().getPolls();
        for(int i=0;i<temp.size();i++){
            final Polls p=temp.get(i);
            System.out.println("poll size :"+present_poll_list.size());
            if(p!=null && p.getStatus().equals("active")  && (p.getType().equals(MainActivity.status) || p.getType().equals(" ")) && (p.getGender().equals(MainActivity.gender) || p.getGender().equals(" "))
                    && (p.getMax_age()>=MainActivity.age) && (p.getSession().equals(MainActivity.session) || p.getSession().equals(" "))){
                int fl=0;
                for(int j=0;j<block_list.size();j++){
                    if(p.getCreater_uid().equals(block_list.get(j))){
                        fl=1;
                        break;
                    }
                }
                if(fl==0)
                    present_poll_list.add(p);

            }
        }
    }
    protected int price(){
        int p=30;
        Random rand = new Random();
        int pr = 5*rand.nextInt(40);
        if(pr>p){
            return pr;
        }
        else {
            return p;
        }
    }
    private boolean searchFromLocationName(String name){
        int flag = 0;
        Geocoder geocoder = new Geocoder(getContext() , Locale.getDefault());

        try {
            List<Address> addresses=geocoder.getFromLocationName(name, 1);
            for(Address a : addresses){
                flag =1;
                System.out.println("address"+a);
                System.out.println("price is : "+price());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(flag == 0){
            return false;
        }
        else{
            return true;
        }

    }
}
