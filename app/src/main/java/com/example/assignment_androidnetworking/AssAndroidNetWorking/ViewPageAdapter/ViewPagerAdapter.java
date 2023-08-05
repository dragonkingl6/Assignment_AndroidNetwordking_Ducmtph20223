package com.example.assignment_androidnetworking.AssAndroidNetWorking.ViewPageAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.assignment_androidnetworking.AssAndroidNetWorking.Login.LoginFragment;
import com.example.assignment_androidnetworking.AssAndroidNetWorking.Register.RegisterFragment;


public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
       if(position == 0)
           return new LoginFragment();
         return new RegisterFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
