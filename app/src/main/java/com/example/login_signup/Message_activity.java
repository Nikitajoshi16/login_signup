package com.example.login_signup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class Message_activity<val> extends AppCompatActivity {

    CircleImageView profileview;
    TextView nameview;
    public static final int REQ_CODE = 1;
    Toolbar toolbar;
    ImageButton buttonsend;
    EditText textsend;
    Uri Imageuri;
    messageadapter messageadapter;
    List<chatsclass> chatsclassList;
    String userid;
    String lasttype;

    RecyclerView recyclerView;
    TextView lastseenv;
    String lastmsg;
    ImageButton camera;
    StorageReference storageReference;
    String chatid;
    Intent intent2;
    ImageButton video;
    Uri videouri;

    String url;
    String name;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_activity);
        toolbar = findViewById(R.id.toolbar6);
        profileview = findViewById(R.id.userimage);
        nameview = findViewById(R.id.nameview);
        buttonsend = findViewById(R.id.imageButton);
        textsend = findViewById(R.id.chattext);
        lastseenv = findViewById(R.id.lastseenview);
        camera = findViewById(R.id.camera);
        storageReference = FirebaseStorage.getInstance().getReference("chats");


        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
         name = intent.getStringExtra("username");


        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Message_activity.this, chatuserprofile.class);
                intent1.putExtra("userid", userid);
                intent1.putExtra("username", name);


                startActivity(intent1);
            }
        });


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = String.valueOf(textsend.getText());


                String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (!message.equals("")) {
                    sendmessage(userid, message, sender);
                }
                textsend.setText("");
                textsend.setHint("Type a message");

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sendmessage(userid, "", FirebaseAuth.getInstance().getCurrentUser().getUid());
                // Log.d("chatid1", "onSuccess: " + chatid);
                CropImage.activity().setAspectRatio(1, 1).start(Message_activity.this);
                String s = getIntent().getStringExtra("abc");
                if(s!=null){
                if(s.equals("xyz")){
                    Intent intent1 = new Intent(Message_activity.this,Message_activity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);

                }}


            }
        });



        FirebaseDatabase.getInstance().getReference("users").child(String.valueOf(userid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("taggi", "onDataChange: "+userid);
                user user1 = snapshot.getValue(user.class);
                nameview.setText(name);
                lastseenv.setText(user1.getLastseen());
                Picasso.get().load(user1.getImageuri()).into(profileview);

                readmessage(firebaseUser.getUid(), userid);
                messageadapter = new messageadapter(Message_activity.this, chatsclassList);
                recyclerView.setAdapter(messageadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendmessage(String receiver, String message, String sender) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hm = new HashMap<>();

        hm.put("sender", sender);
        hm.put("receiver", receiver);
        hm.put("message", message);
        hm.put("type", "text");


        reference.child("chats").push().setValue(hm);

        Log.d("tag", "sendmessage: " + reference.child("chats").push().getKey());
        chatid = reference.child("chats").push().getKey();


    }

    private void readmessage(String myid, String userid) {
        chatsclassList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsclassList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    chatsclass chatsc = snapshot1.getValue(chatsclass.class);


//  Log.d("keyget", "onDataChange: " + snapshot1.getKey());
                    if (chatsc.getReceiver().equals(myid) && chatsc.getSender().equals(userid) ||
                            chatsc.getReceiver().equals(userid) && chatsc.getSender().equals(myid)) {
                        //chatsc.setId(snapshot1.getKey());
                        chatsclassList.add(chatsc);

                        lastmsg = chatsc.getMessage();
                        lasttype = chatsc.getType();

                    }

                }
                Log.d("helloji", "onDataChange: " + lastmsg);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HashMap<String, Object> hsmap = new HashMap<>();
        hsmap.put("lastmsg", lastmsg);
        hsmap.put("lasttyp",lasttype);
        FirebaseDatabase.getInstance().getReference("users").child(userid).updateChildren(hsmap);

    }

    public static void lastseenofuser(String lastseen) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastseen", lastseen);
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
        String strDate = mdformat.format(calendar.getTime());

        lastseenofuser("Last seen at " + strDate);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
              url = result.getUri().toString();

            intent2 = new Intent(Message_activity.this, sendimage.class);
            intent2.putExtra("url", url);
            intent2.putExtra("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
            intent2.putExtra("rec", userid);
            intent2.putExtra("username",name);


            startActivity(intent2);


        }

    }
}