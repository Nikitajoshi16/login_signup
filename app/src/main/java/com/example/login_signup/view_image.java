package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class view_image extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    ImageView view ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        view = findViewById(R.id.imageView);

        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user userprofile = snapshot.getValue(user.class);

                if(userprofile!=null){

                    String imuri = userprofile.imageuri;


                    Picasso.get().load(imuri).into(view);


                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}