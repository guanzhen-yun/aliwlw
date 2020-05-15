package com.ali.alisimulate.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ali.alisimulate.R;
import com.ali.alisimulate.fragment.ControlFragment;
import com.ali.alisimulate.fragment.param.ParamFragment;
import com.google.android.material.tabs.TabLayout;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备详情
 */

@ViewInject(layoutId = R.layout.activity_devicedetail)
public class DeviceDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tblayout)
    TabLayout mTblayout;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private String[] strings = new String[]{"控制", "参数设置"};
    private String productKey;
    private String deviceName;
    private String deviceSecret;
    private String title;
    private ControlFragment controlFragment;

    @Override
    public void fetchIntents() {
        productKey = getIntent().getStringExtra("productKey");
        deviceName = getIntent().getStringExtra("deviceName");
        deviceSecret  = getIntent().getStringExtra("deviceSecret");
        title  = getIntent().getStringExtra("title");
    }

    @Override
    public void initViews() {
        mTvTitle.setText(title);
        controlFragment = ControlFragment.getInstance(title);
        fragmentList.add(controlFragment);
        fragmentList.add(new ParamFragment());
        TabLayout tab_layout = findViewById(R.id.tblayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        MyAdapter fragmentAdater = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdater);
        tab_layout.setupWithViewPager(viewPager);
    }

    public List<String> getControlList() {
        return controlFragment.getControlList();
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
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
