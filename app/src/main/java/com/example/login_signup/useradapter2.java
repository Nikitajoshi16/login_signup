package com.example.login_signup;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class useradapter2 extends RecyclerView.Adapter<useradapter2.viewholder> {


    Context mcntext ;
    List<user> users ;
    View view;

    public useradapter2(Context mcntext, List<user> users) {
        this.mcntext = mcntext;
        this.users = users;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mcntext).inflate(R.layout.viewcontacts,parent,false);


        return new useradapter2.viewholder(view);
    }


    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        user use = users.get(position);
        holder.username.setText(use.getName());
        if(use.getLasttyp() == "text"){
        holder.about.setText(use.getLastmsg());}
        else if(use.getLasttyp() == "image"){
            holder.about.setText("image");
        }
        else{
            holder.about.setText("video");
        }
        Log.d("boo", "onBindViewHolder: " + use.getLastmsg());
        Picasso.get().load(use.getImageuri()).into(holder.circleImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcntext,Message_activity.class);
                intent.putExtra("userid" , use.getId());
                intent.putExtra("username", use.getName());
                mcntext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public  class viewholder extends  RecyclerView.ViewHolder{
        public TextView username;
        public TextView about;
        public CircleImageView circleImageView;

        public viewholder(@NonNull View itemView, TextView username, TextView about, CircleImageView circleImageView) {
            super(itemView);
            this.username = username;
            this.about = about;
            this.circleImageView = circleImageView;
        }




        public viewholder(@NonNull View itemView) {
            super(itemView);
            this.username = itemView.findViewById(R.id.textView5username);
            this.about = itemView.findViewById(R.id.textviewabout);
            this.circleImageView = itemView.findViewById(R.id.image);
        }
    }
}


