package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    EditText email,password,name,phone;
    Button register;
    TextView Switch;
    ProgressBar bar;
    FirebaseAuth firebaseAuth;
    long backpres;





    @Override
    public void onBackPressed() {
        if (backpres + 2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(MainActivity2.this,"press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpres =  System.currentTimeMillis();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        email = findViewById(R.id.name);
        password= findViewById(R.id.editTextTextPassword2);
        name = findViewById(R.id.editTextTextPersonName);
        register= findViewById(R.id.button);
        Switch = findViewById(R.id.switch1);
        bar = findViewById(R.id.progressBar);
        phone = findViewById(R.id.phone);
        //getting current instance of database from firebase
        firebaseAuth = FirebaseAuth.getInstance();





     //  if(firebaseAuth.getCurrentUser() != null){
      //    startActivity(new Intent(MainActivity2.this,entry.class));
      // }

        Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity2.this,MainActivity.class));
            }
        });

        //code to register user to firebase
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email =String.valueOf(email.getText());
                String nm = String.valueOf(name.getText());
                String pw = String.valueOf(password.getText());
                String phn = String.valueOf(phone.getText());
                boolean notcorrect = false;
                if(TextUtils.isEmpty(Email)){ email.setError("Field Required");notcorrect = true;}
                if(TextUtils.isEmpty(nm)){name.setError("Field Required");notcorrect = true;}
                if(TextUtils.isEmpty(pw)){password.setError("Field Required");notcorrect = true;}
                if(TextUtils.isEmpty(phn)){phone.setError("Field Required");notcorrect = true;}
                if(notcorrect){
                    return;
                }
                notcorrect = false;

                if(pw.length()<8){password.setError("password must contain atleast 8 characters");notcorrect = true; }
                if(phn.length()!=10){phone.setError("Invalid phone-number");notcorrect = true; }
                if(notcorrect){
                    return;
                }
                bar.setVisibility(View.VISIBLE);





               //authentication
                firebaseAuth.createUserWithEmailAndPassword(Email,pw).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user us = new user(nm,Email,pw,phn,"Hey",firebaseAuth.getCurrentUser().getUid(),
                                    "https://firebasestorage.googleapis.com/v0/b/authentication-app-9a417.appspot.com/o/users%2F24-248309_transparent-profile-clipart-font-awesome-user-circle-hd.png?alt=media&token=6bbebe94-8674-4e56-a6d0-1290a5acccbc",
                                    "hii",null,null);

                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser()
                                    .getUid()).setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("userregistereddatabase", "onComplete: Yes");
                                    }
                                }
                            });
                            FirebaseUser fus = FirebaseAuth.getInstance().getCurrentUser();
                            fus.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity2.this,"Email sent , Please verify your email first",
                                            Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.INVISIBLE);
                                }
                            });



                            startActivity(new Intent(MainActivity2.this,MainActivity.class));
                        }

                        else{
                            Toast.makeText(MainActivity2.this,"ERROR ! "+task.getException().getMessage()
                                    ,Toast.LENGTH_SHORT).show();
                            bar.setVisibility(View.INVISIBLE);

                        }
                    }
                });










        }});
        

    }



}

