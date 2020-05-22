package com.ali.alisimulate.activity;

import android.content.Intent;
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
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.google.android.material.tabs.TabLayout;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import org.jetbrains.annotations.NotNull;

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
    private String deviceComment;
    private String deviceId;
    private String title;
    private ControlFragment controlFragment;
    private ParamFragment paramFragment;

    @Override
    public void fetchIntents() {
        deviceId = getIntent().getStringExtra("deviceId");
        title = getIntent().getStringExtra("title");
        deviceComment = getIntent().getStringExtra("deviceComment");
    }

    @Override
    public void initViews() {
        mTvTitle.setText(deviceComment);
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue("PowerSwitch");
        if (propertyValue != null) {//阿里云有数据 没断开过连接
            addFragment();
        } else {//本地有数据
            upLoadLocal();
        }
    }

    /**
     * 同步本地数据
     */

    private void upLoadLocal() {
        SaveAndUploadAliUtil.upLoadLocalData(this::addFragment);
    }

    private void addFragment() {
        controlFragment = ControlFragment.getInstance(title);
        fragmentList.add(controlFragment);
        paramFragment = ParamFragment.getInstance(deviceId);
        fragmentList.add(paramFragment);
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
        if (view.getId() == R.id.iv_back) {
            finish();
        }
    }

    public class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NotNull
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (paramFragment != null) {
            paramFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
