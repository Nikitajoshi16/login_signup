package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class usersactivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private  useradapter useradapter;
    private List<user> muser;
    Toolbar toolbar;
    entry ent = new entry();
    HashMap<String,String> hm = new HashMap<>();
    public static final int REQUEST_READ_CONTACTS = 79;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            Intent intent = new Intent(usersactivity.this,Chats.class);
            startActivity(intent);
        }
        return true ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersactivity);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            hm = getAllContacts();
        } else {
            requestPermission();
        }

        recyclerView = findViewById(R.id.rview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(usersactivity.this));
        muser = new ArrayList<>();

        Log.d("hmsize", "onCreate: " + hm.size());


        readusers();

    }

    private void readusers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                muser.clear();



                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d("size", "onDataChange: " + dataSnapshot.getKey());
                    user user1 = dataSnapshot.getValue(user.class);
                   String id = user1.setId(dataSnapshot.getKey());
                   // Log.d("check", "onDataChange: " + id + firebaseUser.getUid());
                    user1.setName(hm.get(user1.getPhno()));

                    Log.d("bye", "onDataChange: "+user1.getLastmsg());

                    if(!id.equals(firebaseUser.getUid()) && hm.containsKey(user1.getPhno())){
                        Log.d("isok", "onDataChange: "+ id + " " + firebaseUser.getUid());
                    muser.add(user1);}


                }
                Log.d("users", "onDataChange: " + muser.size() );
                useradapter = new useradapter(usersactivity.this,muser);
                recyclerView.setAdapter(useradapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hm = getAllContacts();
                }

        }
        return;
    }

    public HashMap<String,String> getAllContacts() {
        HashMap<String,String> hm = new HashMap<>();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String newno =  phoneNumber.replaceAll(" ","")
                    .replaceAll("-","");
            if(newno.length() >10){
                newno =   newno.substring(newno.length()-10);
            }

            Log.d("new", "getAllContacts: " );
            hm.put(newno,name);


        }
        phones.close();

        return  hm;

    }

    public void  requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS) ;
        }
    }



    public static void lastseenofuser(String lastseen){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("lastseen",lastseen);
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lastseenofuser("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate =  mdformat.format(calendar.getTime());

        lastseenofuser("Last seen at " + strDate);

    }


}