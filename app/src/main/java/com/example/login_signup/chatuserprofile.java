package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class chatuserprofile extends AppCompatActivity {
   ImageView userimage ;
   TextView username;
   TextView lastseen;
   TextView phno;
   CardView blockcontact;
   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatuserprofile);
       toolbar = findViewById(R.id.toolbar5);
       setSupportActionBar(toolbar);
        userimage = findViewById(R.id.userimage);
        username = findViewById(R.id.nameuser);
        lastseen=findViewById(R.id.lastseen);
        phno=findViewById(R.id.phone);
        blockcontact = findViewById(R.id.block);
        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");
        String name = intent.getStringExtra("username");


        FirebaseDatabase.getInstance().getReference("users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user user1 = snapshot.getValue(user.class);
                username.setText(name);
                phno.setText(user1.getPhno());

                Picasso.get().load(user1.getImageuri()).into(userimage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}