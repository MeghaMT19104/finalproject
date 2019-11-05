package com.example.appproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import static com.google.android.gms.auth.api.signin.GoogleSignInApi.*;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = " ";
    TextView tx;
    EditText t;
    EditText editTextEmail,editTextPassword,editTextEmailforget;
    TextView editTextforgotpassword;
    String user;
    public static String personId;
    View Dialog_reg,Dialog_for,Dialog_ch;
    CountDownTimer ctimer;
    final Context context = this;
    AlertDialog alertDialog;
    Boolean userloggedin=false;
    boolean is_Reg_active = false,is_for_active = false,is_cpass_active;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference ref,pref;
    Userdetails userz;

    GoogleSignInResult result;
    private SignInButton signIn;
    public static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    static Pattern pattern = Pattern.compile("^[a-zA-Z\\s]*.iiitd.ac.in$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText) findViewById(R.id.login_emailid);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        findViewById(R.id.new_user).setOnClickListener(this);
        findViewById(R.id.forgot_password).setOnClickListener(this);
        signIn = findViewById(R.id.sign_in_button);



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("Dialog") == true) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.register_dialog, null);
                Dialog_reg = promptsView;
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(LoginActivity.this);
                builder.setTitle("Register");
                builder.setCancelable(false);
                builder.setView(promptsView);
                builder
                        .setPositiveButton(
                                "Submit",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        is_Reg_active = false;
                                        dialog.cancel();

                                        //Save data
                                    }
                                });

                // Set the Negative button with No name
                // OnClickListener method is use
                // of DialogInterface interface.
                builder
                        .setNegativeButton(
                                "Cancel",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        // If user click no
                                        // then register_dialog box is canceled.
                                        is_Reg_active = false;
                                        dialog.cancel();
                                    }
                                });
                alertDialog = builder.create();
                alertDialog.show();
                t = promptsView.findViewById(R.id.username);
                t.setText(savedInstanceState.getString("user"));
                t = promptsView.findViewById(R.id.password);
                t.setText(savedInstanceState.getString("pass"));
                t = promptsView.findViewById(R.id.confirm);
                t.setText(savedInstanceState.getString("cpass"));
                t = promptsView.findViewById(R.id.email);
                t.setText(savedInstanceState.getString("email"));
                is_Reg_active = alertDialog.isShowing();

            }
            if (savedInstanceState.getBoolean("is_for_active") == true) {

                LayoutInflater li = LayoutInflater.from(context);
                final View promptsView = li.inflate(R.layout.forgot_pass, null);
                Dialog_for = promptsView;
                editTextEmailforget = promptsView.findViewById(R.id.email1);
                editTextEmailforget.setText(savedInstanceState.getString("email1"));
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(LoginActivity.this);
                builder.setTitle("Change Password");
                builder.setCancelable(false);
                builder.setView(promptsView);

                // Set the Negative button with No name
                // OnClickListener method is use
                // of DialogInterface interface.
                builder
                        .setNegativeButton(
                                "Cancel",
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

                builder
                        .setPositiveButton(
                                "Submit",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        editTextEmailforget = promptsView.findViewById(R.id.email1);
                                        final String Email_forget = editTextEmailforget.getText().toString().trim();
                                        if (!Patterns.EMAIL_ADDRESS.matcher(Email_forget).matches()) {
                                            editTextEmailforget.setError("Enter valid emailid");
                                            editTextEmailforget.requestFocus();
                                            return;
                                        }

                                        mFirebaseAuth.sendPasswordResetEmail(Email_forget)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(LoginActivity.this, "Password set to your mail", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                        //Save data
                                        dialog.cancel();
                                    }
                                });
                alertDialog = builder.create();
                alertDialog.show();
                is_for_active = alertDialog.isShowing();
            }
        }
    }
    private void userlogin()
    {
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter valid emailid");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Enter Password");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Minimun length of password is 6");
            editTextPassword.requestFocus();
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    finish();
                    Intent intent=new Intent(LoginActivity.this,editprofile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    userloggedin=true;
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    protected void forgot(View v){
//        LayoutInflater li = LayoutInflater.from(context);
//        final View promptsView = li.inflate(R.layout.forgot_pass, null);
//        Dialog_for = promptsView;
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(LoginActivity.this);
//        builder.setTitle("OTP Request");
//        builder.setCancelable(false);
//        builder.setView(promptsView);
//
//
//        // Set the Negative button with No name
//        // OnClickListener method is use
//        // of DialogInterface interface.
//        builder
//                .setNegativeButton(
//                        "Cancel",
//                        new DialogInterface
//                                .OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//
//                                // If user click no
//                                // then register_dialog box is canceled.
//                                is_for_active = false;
//                                if(ctimer!=null){
//                                    ctimer.cancel();
//                                }
//                                dialog.cancel();
//                            }
//                        });
//        alertDialog = builder.create();
//        alertDialog.show();
//        is_for_active = alertDialog.isShowing();
//        Button b = Dialog_for.findViewById(R.id.Send);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog_for.findViewById(R.id.layout1).setVisibility(View.VISIBLE);
//                tx = Dialog_for.findViewById(R.id.timer);
//                ctimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here
//
//                    public void onTick(long millisUntilFinished) {
//                        int time = (int)millisUntilFinished/1000;
//
//                        tx.setText("Remaining : 00:"+time);
//                    }
//
//                    public void onFinish() {
//                        tx.setText("done!");
//                        ctimer.cancel();
//                    }
//                }.start();
//            }
//        });
//
//
//
//    }
//    protected void  submit_otp(View view){
//        LayoutInflater li = LayoutInflater.from(context);
//        ctimer.cancel();
//        is_for_active = false;
//        alertDialog.cancel();
//        final View promptsView = li.inflate(R.layout.change_pass, null);
//        Dialog_ch = promptsView;
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(LoginActivity.this);
//        builder.setTitle("Reset Password");
//        builder.setCancelable(false);
//        builder.setView(promptsView);


        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
//        builder
//                .setNegativeButton(
//                        "Cancel",
//                        new DialogInterface
//                                .OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which)
//                            {
//
//                                // If user click no
//                                // then register_dialog box is canceled.
//                                is_cpass_active = false;
//                                dialog.cancel();
//                            }
//                        });
//        alertDialog = builder.create();
//        alertDialog.show();
//        is_cpass_active = alertDialog.isShowing();
//
//        Button b = Dialog_ch.findViewById(R.id.cpass_sub);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                is_cpass_active = false;
//            }
//        });
//
//    }
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if(is_Reg_active == true){
            String value;
            t = Dialog_reg.findViewById(R.id.username);
            value = t.getText().toString();
            state.putString("user",value);
            t = Dialog_reg.findViewById(R.id.password);
            value = t.getText().toString();
            state.putString("pass", value);
            t = Dialog_reg.findViewById(R.id.confirm);
            value = t.getText().toString();
            state.putString("cpass", value);
            t = Dialog_reg.findViewById(R.id.email);
            value = t.getText().toString();
            state.putString("email", value);



        }
        if (is_for_active == true){
            String value;
            EditText ed = Dialog_for.findViewById(R.id.email1);
            value = ed.getText().toString();
            state.putString("email1", value);



        }

        state.putBoolean("Dialog", is_Reg_active);
        state.putBoolean("is_for_active", is_for_active);

    }

    protected void Register(View view){
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.register_dialog, null);
        Dialog_reg = promptsView;
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginActivity.this);
        builder.setTitle("Register");
        builder.setCancelable(false);
        builder.setView(promptsView);
        builder
                .setPositiveButton(
                        "Submit",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                is_Reg_active=false;
                                //Save data
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
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
                                is_Reg_active = false;
                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
        Log.d("Value", "Re Bhaiya!!  Dialog bnayaa bada maza aya");
        is_Reg_active = alertDialog.isShowing();

        Log.d("Value", "acha chalta hu duao m yaad rakhna......I hate u");
    }

    public void login(View view) {
        Intent i= new Intent(this,editprofile.class);
        startActivity(i);
    }
    protected void forgotpassword()
    {

        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.forgot_pass, null);
        Dialog_for = promptsView;
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginActivity.this);
        builder.setTitle("Change Password");
        builder.setCancelable(false);
        builder.setView(promptsView);

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
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

        builder
                .setPositiveButton(
                        "Submit",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                editTextEmailforget=promptsView.findViewById(R.id.email1);
                                final String Email_forget=editTextEmailforget.getText().toString().trim();
                                if (!Patterns.EMAIL_ADDRESS.matcher(Email_forget).matches()) {
                                    editTextEmailforget.setError("Enter valid emailid");
                                    editTextEmailforget.requestFocus();
                                    return;
                                }

                                mFirebaseAuth.sendPasswordResetEmail(Email_forget)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(LoginActivity.this,"Password set to your mail",Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                //Save data
                                dialog.cancel();
                            }
                        });
        alertDialog = builder.create();
        alertDialog.show();
        is_for_active = alertDialog.isShowing();
        //  Button b = Dialog_for.findViewById(R.id.Send);




    }
    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.new_user:
                finish();
                Intent inte = new Intent(this,Register.class);
                startActivity(inte);
                break;
            case R.id.loginBtn:
                userlogin();
                break;
            case R.id.forgot_password:
                forgotpassword();
                break;
        }

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.d("s","In try");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("s","In mid try");
                if (result.isSuccess()) {
                    // Signed in successfully.
                    GoogleSignInAccount acct = result.getSignInAccount();
                    String email = acct.getEmail();
                    String[] split = email.split("@");
                    String domain = split[1]; //This Will Give You The Domain After '@'
                    if(domain.equals("iiitd.ac.in"))
                    {
                        //Proceed Ahead.
                        firebaseAuthWithGoogle(account);
                        startActivity(new Intent(LoginActivity.this,editprofile.class));
                    }
                    else
                    {
                        //Show User Warning UI.
                        Toast.makeText(getApplicationContext(),"It is invalid",Toast.LENGTH_LONG).show();
                        signOut();



                    }
                } else {
                    // Signed out, show unauthenticated UI.
                    // updateUI(false);
                }

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this,"you are not able to log in to google",Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
/*
private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                    }
                        // Sign in success, update UI with the signed-in user's information
                        if (result.isSuccess()) {
                            // Signed in successfully.
                            GoogleSignInAccount acct = result.getSignInAccount();
                            String email = acct.getEmail();
                            String[] split = email.split("@");
                            String domain = split[1]; //This Will Give You The Domain After '@'
                            if(domain.equals("iiitd.ac.in"))
                            {
                                //Proceed Ahead.
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            else
                            {
                                //Show User Warning UI.
                                Toast.makeText(MainActivity.this,"you are not able to log in to google",Toast.LENGTH_LONG).show();

                            }

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(MainActivity.this,"you are not able to log in to google",Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }

                    // ...
                }
            });
}*/





    private void updateUI(FirebaseUser user) {

        //signOut.setVisibility(View.VISIBLE);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            ref=FirebaseDatabase.getInstance().getReference("users");
            userz= new Userdetails(personName,personEmail);
            ref.child(personId).child("Username").setValue(personName);
            ref.child(personId).child("email").setValue(personEmail);
            pref=FirebaseDatabase.getInstance().getReference("profiles");
            pref.child(personId).child("Age").setValue(0);
            pref.child(personId).child("Description").setValue(null);
            pref.child(personId).child("Gender").setValue(null);
            pref.child(personId).child("Name").setValue(personName);
            pref.child(personId).child("RateCount").setValue(0);
            pref.child(personId).child("Ratings").setValue(0);
            pref.child(personId).child("Session").setValue(null);
            pref.child(personId).child("Status").setValue(null);
            //database=FirebaseDatabase.getInstance();
           // ref = database.getReference("https://demoapp-67088.firebaseio.com/demoapp-67088/");
            /*
            ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(personName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            { finish();
                                Toast.makeText(MainActivity.this,"Data saved",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                //failure
                                Toast.makeText(MainActivity.this,"Data Not saved",Toast.LENGTH_LONG).show();
                            }

                    }
                    });
            */
            Log.e(TAG,"User name "+personName);



            //Toast.makeText(this, "Name of the user :" + personName + " user id is : " + personId, Toast.LENGTH_SHORT).show();

        }


    }

}
