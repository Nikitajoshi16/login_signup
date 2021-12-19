package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterFactory;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class entry extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    Adaptivity ada;
    long backpres;
    androidx.appcompat.widget.Toolbar toolbar;
  public HashMap<String,String> list = new HashMap<>();

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.sidemenu,menu);
        //set backgroud for menu
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.fragprofile:
               startActivity(new Intent(entry.this,profileactivity.class));
               break;
            case R.id.fragsettins :
                startActivity(new Intent(entry.this,settings.class));
                break;
            case R.id.item2:
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backpres + 2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else{
            Toast.makeText(entry.this,"press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backpres =  System.currentTimeMillis();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tabLayout = findViewById(R.id.tabs);
        viewPager2 = findViewById(R.id.viewpager);



        Log.d("contacts", "onCreate: " + list.size());

      //  FragmentManager fn = getSupportFragmentManager();
        ada= new Adaptivity(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(ada);
        tabLayout.addTab(tabLayout.newTab().setText("Chats"));
        tabLayout.addTab(tabLayout.newTab().setText("Status"));
        tabLayout.addTab(tabLayout.newTab().setText("Calls"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



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