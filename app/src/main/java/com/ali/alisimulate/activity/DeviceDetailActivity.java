package com.ali.alisimulate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ali.alisimulate.R;
import com.ali.alisimulate.fragment.ControlFragment;
import com.ali.alisimulate.fragment.ParamFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DeviceDetailActivity extends FragmentActivity {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private String[] strings = new String[]{"控制","参数设置"};
    private ImageView iv_close;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicedetail);
        iv_close = findViewById(R.id.iv_close);
        fragmentList.add(new ControlFragment());
        fragmentList.add(new ParamFragment());
        initView();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO关闭设备
            }
        });
    }

    private void initView() {
        TabLayout tab_layout = findViewById(R.id.tblayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        MyAdapter fragmentAdater = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdater);
        tab_layout.setupWithViewPager(viewPager);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return strings[position];
        }
    }
}
