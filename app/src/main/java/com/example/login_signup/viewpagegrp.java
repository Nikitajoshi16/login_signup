package com.example.login_signup;
import android.provider.CallLog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

class Adaptivity extends FragmentStateAdapter{




    public Adaptivity(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
       if(position== 1){
           return new Status();
       }
       else if(position==2){
           return new calls();
       }
       return new Chats();
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}





