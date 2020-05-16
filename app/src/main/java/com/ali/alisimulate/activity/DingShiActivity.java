package com.ali.alisimulate.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ali.alisimulate.R;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:关震
 * Date:2020/5/16 7:45
 * Description:DingShiActivity
 **/
@ViewInject(layoutId = R.layout.activity_dingshi)
public class DingShiActivity extends BaseActivity {
    @BindView(R.id.sw_open)
    Switch mSwOpen;
    @BindView(R.id.sw_close)
    Switch mSwClose;

    @Override
    public void initViews() {
        mSwOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                }
            }
        });

        mSwClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                }
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
