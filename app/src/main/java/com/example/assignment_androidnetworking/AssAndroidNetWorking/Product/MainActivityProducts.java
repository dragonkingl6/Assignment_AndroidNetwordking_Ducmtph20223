package com.example.assignment_androidnetworking.AssAndroidNetWorking.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.assignment_androidnetworking.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivityProducts extends AppCompatActivity {
    TabLayout tablayout;
    ViewPager2 viewpage;
    ViewPageFrgPro adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_products);
        tablayout=findViewById(R.id.tablauout);
        viewpage=findViewById(R.id.viewpage);
        adapter=new ViewPageFrgPro(this);
        viewpage.setAdapter(adapter);
        new TabLayoutMediator(tablayout, viewpage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Danh Sách Sản phẩm");
                        break;
                    case 1:
                        tab.setText("Thêm Sản phẩm");
                        break;
                }
            }
        }).attach();
    }
}