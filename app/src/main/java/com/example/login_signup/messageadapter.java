package com.example.login_signup;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageadapter extends RecyclerView.Adapter<messageadapter.viewholder> {
public static final int  MSG_TYPE_LEFT = 0;
public static final int MSG_TYPE_RIGHT = 1;
String urli;

    Context mcntext ;
    List<chatsclass> chats ;
    View view;


    public messageadapter(Context mcntext, List<chatsclass> chats) {
        this.mcntext = mcntext;
        this.chats = chats;
    }

    @NonNull
    @Override
    public messageadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
        view = LayoutInflater.from(mcntext).inflate(R.layout.chat_item_right,parent,false);
            Log.d("right", "onCreateViewHolder: right");
            return new messageadapter.viewholder(view);

        }
        else {
            view = LayoutInflater.from(mcntext).inflate(R.layout.chat_item_left,parent,false);
            Log.d("left", "onCreateViewHolder: left");
            return new messageadapter.viewholder(view);
        }


    }


    public void onBindViewHolder(@NonNull messageadapter.viewholder holder, int position) {

chatsclass chatsclass = chats.get(position);
       int itemtype = getItemViewType(position);
         if(itemtype == MSG_TYPE_RIGHT){
             if(chatsclass.getType()!=null){
             if(chatsclass.getType().equals("text")){

                 holder.linearLayoutright.setVisibility(View.GONE);
                 holder.msgr.setText(chatsclass.getMessage());

                 holder.msgr.setOnLongClickListener(new View.OnLongClickListener() {
                     @Override
                     public boolean onLongClick(View view) {

                         return true;
                     }
                 });
             }
         else if(chatsclass.getType().equals("image"))  {         //holder.linearLayoutright.setVisibility(View.VISIBLE);
                 holder.linearLayoutright.setVisibility(View.VISIBLE);
                 holder.righti.setVisibility(View.VISIBLE);
                 holder.righti.setOnLongClickListener(new View.OnLongClickListener() {
                     @Override
                     public boolean onLongClick(View view) {
                         Dialog dialog = new Dialog(mcntext);
                         dialog.setContentView(R.layout.chatdailog);
                         dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                         ImageButton delete = dialog.findViewById(R.id.imageButton2);
                         ImageButton details= dialog.findViewById(R.id.imageButton4);
                         ImageButton download= dialog.findViewById(R.id.imageButton5);
                         download.setVisibility(View.VISIBLE);

                         delete.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View view) {

                             }
                         });

                         dialog.show();

                         return true;
                     }
                 });
                 if(chatsclass.getMessage()!=null){

                 Picasso.get().load(chatsclass.getMessage()).into(holder.righti);
                 urli = chatsclass.getMessage();}
                 else{
                     Toast.makeText(mcntext , "image not availabe",Toast.LENGTH_SHORT).show();
                 }

             }

             }
             else{
                 holder.msgr.setText(chatsclass.getMessage());
             }}

         else{
             if (chatsclass.getType()!=null){
             if (chatsclass.getType().equals("text")){
                 holder.linearLayoutleft.setVisibility(View.GONE);


        holder.msgl.setText(chatsclass.getMessage());}
             else if(chatsclass.getType().equals("image")) {
                 holder.linearLayoutleft.setVisibility(View.VISIBLE);
                 Picasso.get().load(chatsclass.getMessage()).into(holder.lefti);
                 urli = chatsclass.getMessage();

             }
         }else {
                 holder.msgl.setText(chatsclass.getMessage());
             }}

    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
    public  class viewholder extends  RecyclerView.ViewHolder{
        public TextView msgr;
        public TextView msgl;
        public ImageView lefti;
        public ImageView righti;
        public LinearLayout linearLayoutright;
        public LinearLayout linearLayoutleft;
        public VideoView vdl;
        public  VideoView vdr;



        public viewholder(@NonNull View itemView) {
            super(itemView);


            msgr = itemView.findViewById(R.id.showmessage);
            msgl = itemView.findViewById(R.id.show);
            lefti = itemView.findViewById(R.id.imageleft);
            righti = itemView.findViewById(R.id.rightimage);
            linearLayoutright = itemView.findViewById(R.id.rightlay);
            linearLayoutleft = itemView.findViewById(R.id.leftlay);


        }
    }}
