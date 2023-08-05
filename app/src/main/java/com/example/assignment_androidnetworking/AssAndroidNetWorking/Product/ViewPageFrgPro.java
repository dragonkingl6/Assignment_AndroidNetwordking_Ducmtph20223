package com.example.assignment_androidnetworking.AssAndroidNetWorking.Product;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageFrgPro extends FragmentStateAdapter {


    public ViewPageFrgPro(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ListProductFragment();
            case 1:
                return new createproducts();
        }
        return new ListProductFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
