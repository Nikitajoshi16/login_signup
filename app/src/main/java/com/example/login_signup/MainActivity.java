package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView Switch1;
    ProgressBar bar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.name2);
        password= findViewById(R.id.password);
        login= findViewById(R.id.button2);
        Switch1 = findViewById(R.id.textView);
        bar = findViewById(R.id.progressBar2);
        firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String,Object> hashMap = new HashMap<>();
        user use = new user();
        hashMap.put("lastmsg","hii");
        if(firebaseAuth.getCurrentUser()!=null){
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);}



        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(MainActivity.this,entry.class));
            finish();
        }

        Switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MainActivity2.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email =String.valueOf(email.getText());

                String pw = String.valueOf(password.getText());
                if(TextUtils.isEmpty(Email)){ email.setError("Field Required");return;}

                if(TextUtils.isEmpty(pw)){password.setError("Field Required");return;}

                 if(pw.length()<8){password.setError("password must contain atleast 6 characters");return; }

                bar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(Email,pw).addOnCompleteListener
                        (new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                      FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if(user.isEmailVerified()){

                                    Toast.makeText(MainActivity.this,"You are logged in",
                                            Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(MainActivity.this,entry.class));}
                                    else{
                                        Toast.makeText(MainActivity.this,"Enale to login, please verify your Email",
                                                Toast.LENGTH_SHORT).show();
                                        bar.setVisibility(View.INVISIBLE);

                                    }}


                                else{
                                    Toast.makeText(MainActivity.this,"ERROR ! "+task.getException()
                                                    .getMessage()
                                            ,Toast.LENGTH_SHORT).show();
                                    bar.setVisibility(View.INVISIBLE);
                                }}

                            });




            }
        });

    }



}