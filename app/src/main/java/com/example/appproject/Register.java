package com.example.appproject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//hello ji kese ho??
public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText editUsername, editPassword_Register, editConfirmpassword, editEmail_Register;
    static Pattern pat = Pattern.compile("^[0-9a-zA-Z\\s]*.@iiitd.ac.in$");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog);
        editUsername = (EditText) findViewById(R.id.username);
        editPassword_Register = (EditText) findViewById(R.id.password);
        editConfirmpassword = (EditText) findViewById(R.id.confirm);
        editEmail_Register = (EditText) findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.button_submit).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
    }

    private void registeruser() {
        final String Username, Email_Register;
        String Password_Register,Confirmpassword;

        Username = editUsername.getText().toString().trim();
        Password_Register = editPassword_Register.getText().toString().trim();
        Confirmpassword = editConfirmpassword.getText().toString().trim();
        Email_Register = editEmail_Register.getText().toString().trim();
        Matcher matcher = pat.matcher(Email_Register);
        //  while (!Credentials)
        // {
        if(!matcher.find()){
            editEmail_Register.setError("Please enter iiitd email only");
            editEmail_Register.requestFocus();
            return;

        }
        if (Email_Register.isEmpty()) {
            editEmail_Register.setError("Email is required");
            editEmail_Register.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email_Register).matches()) {
            editEmail_Register.setError("Enter valid emailid");
            editEmail_Register.requestFocus();
            return;
        }
        if (Password_Register.isEmpty()) {
            editPassword_Register.setError("Enter Password");
            editPassword_Register.requestFocus();
            //    Credentials = true;
            return;
        }
        if (Password_Register.length() < 6) {
            editPassword_Register.setError("Minimun length of password is 6");
            editPassword_Register.requestFocus();
            // Credentials = true;
            return;
        }
        if (Confirmpassword.isEmpty()) {
            editConfirmpassword.setError("Enter Confirm Password");
            editConfirmpassword.requestFocus();
            //Credentials = true;
            return;
        }
        if (!( Password_Register.equals(Confirmpassword))) {
            editConfirmpassword.setError("Password & confirm password must be same");
            editConfirmpassword.requestFocus();
            //  Credentials = true;
            return;
        }


        mAuth.createUserWithEmailAndPassword(Email_Register,Password_Register).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {   finish();

                    Toast.makeText(getApplicationContext(),"User Regsitered Successfully",Toast.LENGTH_SHORT).show();
                    //Store the data to database
                    Userdetails user= new Userdetails(Username, Email_Register);
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            { finish();
                                Toast.makeText(Register.this,"Data saved",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                //failure
                                Toast.makeText(Register.this,"Data Not saved",Toast.LENGTH_LONG).show();
                            }
                        }
                    });




                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(),"Already Registered",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.button_submit:
                startActivity(new Intent(Register.this,LoginActivity.class));
                registeruser();

                break;
            case R.id.button_cancel:
                startActivity(new Intent(Register.this,LoginActivity.class));
                break;

        }
    }
}