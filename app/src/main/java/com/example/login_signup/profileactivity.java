package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

public class profileactivity extends AppCompatActivity {
    TextView username ;
    TextView about;
    TextView ph_no ;
    FirebaseDatabase db;
    DatabaseReference dr;
    FirebaseUser us;
    String usid;
    ImageView updatename;
    ImageView updateabt;
    TextView heading ;
    EditText updatevalue;
    TextView saveit;
    TextView cancelit;
    HashMap hm = new HashMap<>();
    StorageReference storageReference;
    public static final int imagereq = 1;
    private Uri Imageuri;
    private StorageTask uploadtask;
    CircleImageView imageView;
    FloatingActionButton floatingActionButton;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilelayout);
        username = findViewById(R.id.textView5username);
        about = findViewById(R.id.textView7);
        ph_no = findViewById(R.id.phoneno);
        updatename = findViewById(R.id.imageView5);
        updateabt = findViewById(R.id.imageView6);
        imageView = findViewById(R.id.profilepic);
        floatingActionButton = findViewById(R.id.actionbutton);



        storageReference = FirebaseStorage.getInstance().getReference("users");
        db = FirebaseDatabase.getInstance();
        dr = db.getReference("users");
        us = FirebaseAuth.getInstance().
                getCurrentUser();
        usid = us.getUid();





dr.child(usid).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        user userprofile = snapshot.getValue(user.class);

        if(userprofile!=null){
            String fullnm = userprofile.name;
            String phno = userprofile.phno;
            String abot = userprofile.about;
            String email = userprofile.email;
            String pw = userprofile.pw;
            String imuri = userprofile.imageuri;

            hm.put("name" , fullnm);
            hm.put("phno" , phno);
            hm.put("email" , email);
            hm.put("about" , abot);
            hm.put("pw" , pw);
            hm.put("imageuri" , imuri);

            Picasso.get().load(imuri).into(imageView);


            username.setText(fullnm);
            ph_no.setText(phno);
            about.setText(abot);
        }


    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(profileactivity.this , "Something went wrong" , Toast.LENGTH_SHORT).show();
    }
});
//view profile pic
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(profileactivity.this,view_image.class));


            }
        });
 //updaterofile
 floatingActionButton.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         CropImage.activity().setAspectRatio(1,1).start(profileactivity.this);
     }
 });



//update database
        updateabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog( profileactivity.this);
                dialog.setContentView(R.layout.update_dialog);
               dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


                updatevalue = dialog.findViewById(R.id.enter);
                heading = dialog.findViewById(R.id.textView5);
                saveit = dialog.findViewById(R.id.save);
                cancelit = dialog.findViewById(R.id.cancel);
                heading.setText("Currently set to...");
                dialog.show();

                saveit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hm.put("about" , updatevalue.getText().toString());
                         FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).updateChildren(hm).addOnSuccessListener(new OnSuccessListener() {
                             @Override
                             public void onSuccess(Object o) {
                                 Log.d("updated", "onSuccess: yes");
                             }
                         });

                         about.setText(updatevalue.getText().toString());
                         dialog.dismiss();
                    }
                });
                cancelit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });
        updatename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(profileactivity.this);
                dialog.setContentView(R.layout.update_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


                updatevalue = dialog.findViewById(R.id.enter);
                heading = dialog.findViewById(R.id.textView5);
                saveit = dialog.findViewById(R.id.save);
                cancelit = dialog.findViewById(R.id.cancel);
                heading.setText("Enter your name");
                dialog.show();
                saveit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hm.put("name" , updatevalue.getText().toString());
                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).updateChildren(hm).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Log.d("updated", "onSuccess: yes");
                            }
                        });

                        username.setText(updatevalue.getText().toString());
                        dialog.dismiss();
                    }
                });
                cancelit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Imageuri = result.getUri();

            imageView.setImageURI(Imageuri);
             StorageReference fileref = storageReference.child(FirebaseAuth.getInstance().getCurrentUser()
                    .getUid());
            StorageTask uploadtask = fileref.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
        String myuri = uri.toString();

        hm.put("imageuri" , myuri);
        dr.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hm);
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