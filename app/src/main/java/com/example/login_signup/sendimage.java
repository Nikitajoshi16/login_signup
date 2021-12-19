package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class sendimage extends AppCompatActivity {
    ImageView imageView ;
    ImageButton button;
    EditText text;
    String sender;
    String rec;
    String url;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendimage);

        imageView = findViewById(R.id.imageView7);
        button = findViewById(R.id.imageButton3);
        text = findViewById(R.id.editTextTextPersonName2);

        Intent intent = getIntent();
        sender = intent.getStringExtra("sender");
        rec = intent.getStringExtra("rec");
        url = intent.getStringExtra("url");
        String name = intent.getStringExtra("username");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        Picasso.get().load(url).into(imageView);
        uri = Uri.parse(url);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagesend();
                Intent intent1 = new Intent(sendimage.this,Message_activity.class);
                intent1.putExtra("userid",rec);
                intent1.putExtra("username",name);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("abc","xxxyz");

                startActivity(intent1);
            }
        });






    }

    private void imagesend() {
        if(url!=null){
            String uniqueID = UUID.randomUUID().toString();
            StorageReference fileref = storageReference.child(uniqueID);

            StorageTask uploadtask = fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String myuri = uri.toString();
                           // sendimage(rec,myuri, FirebaseAuth.getInstance().getCurrentUser().getUid());
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hm = new HashMap<>();

                            hm.put("sender", sender);
                            hm.put("receiver", rec);
                            hm.put("message", myuri);
                            hm.put("type","image");


                            reference.child("chats").push().setValue(hm);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });



        }
        }
    }
