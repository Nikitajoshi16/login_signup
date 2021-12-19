package com.example.login_signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HashMap<String,String> hm = new HashMap<>();
    RecyclerView recyclerView ;
    useradapter2 useradapter;
    ArrayList<user> muser;
    public static final int REQUEST_READ_CONTACTS = 79;
    HashSet<String> hs = new HashSet<>();

    FloatingActionButton actionButton;
    String lstm;

    public Chats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chats.
     */
    // TODO: Rename and change types and number of parameters
    public static Chats newInstance(String param1, String param2) {
        Chats fragment = new Chats();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void readusers() {
        //SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
       //lstm =  preferences.getString("lastmsg",);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hs.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    chatsclass chatsclass = snapshot1.getValue(chatsclass.class);
                    hs.add(chatsclass.getSender());
                    hs.add(chatsclass.getReceiver());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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




                    if(!id.equals(firebaseUser.getUid()) && hm.containsKey(user1.getPhno())){

                        Log.d("isok", "onDataChange: "+ id + " " + firebaseUser.getUid());
                        if(hs.contains(id)){
                            Log.d("hello", "onDataChange: "+user1.getLastmsg());
                        muser.add(user1);}}


                }
                Log.d("users", "onDataChange: " + muser.size() );
                useradapter = new useradapter2(getActivity(),muser);
                recyclerView.setAdapter(useradapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        actionButton = view.findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
                startActivity(new Intent(getActivity(),usersactivity.class));
          }
       });

        recyclerView = view.findViewById(R.id.review);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        muser = new ArrayList<>();


        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            hm = getAllContacts();
        } else {
            requestPermission();
        }

        Log.d("contacts", "onCreateView: "+hm.size());
        readusers();


       return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readusers();
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS) ;
        }
    }

    private HashMap<String, String> getAllContacts() {
        HashMap<String,String> hm = new HashMap<>();

        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String newno =  phoneNumber.replaceAll(" ","")
                    .replaceAll("-","");
            if(newno.length()>10){
                newno =   newno.substring(newno.length()-10);
            }

            Log.d("new", "getAllContacts: " );
            hm.put(newno,name);


        }
        phones.close();
        return  hm;
    }


}