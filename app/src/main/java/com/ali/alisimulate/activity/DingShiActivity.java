package com.ali.alisimulate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.ali.alisimulate.R;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
        List<ValueWrapper> value = SaveAndUploadAliUtil.getList("LocalTimer");
        if(value == null || value.size() == 0) {
            mSwOpen.setChecked(false);
            mSwClose.setChecked(false);
        } else {
            Map<String, ValueWrapper> value1 = (Map<String, ValueWrapper>) value.get(0).getValue();
            if(value1.size() > 0) {
                ValueWrapper.BooleanValueWrapper enable = (ValueWrapper.BooleanValueWrapper) value1.get("Enable");
                if(enable != null && enable.getValue() != null && enable.getValue() == 1) {
                    mSwOpen.setChecked(true);
                } else {
                    mSwOpen.setChecked(false);
                }
            } else {
                mSwOpen.setChecked(false);
            }
            Map<String, ValueWrapper> value2 = (Map<String, ValueWrapper>) value.get(1).getValue();
            if(value2.size() > 0) {
                ValueWrapper.BooleanValueWrapper enable = (ValueWrapper.BooleanValueWrapper) value2.get("Enable");
                if(enable != null && enable.getValue() != null && enable.getValue() == 1) {
                    mSwClose.setChecked(true);
                } else {
                    mSwClose.setChecked(false);
                }
            } else {
                mSwClose.setChecked(false);
            }
        }

        mSwOpen.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                SaveAndUploadAliUtil.saveOpenTime(true);
            } else {
                SaveAndUploadAliUtil.closeStatus(true);
            }
        });

        mSwClose.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                SaveAndUploadAliUtil.saveOpenTime(false);
            } else {
                SaveAndUploadAliUtil.closeStatus(false);
            }
        });
    }



    private void jumpControl(boolean isOpen) {
        Bundle bundle = new Bundle();
        bundle.putString("title", isOpen ? "定时开机" : "定时关机");
        Intent intent = new Intent(DingShiActivity.this, DingshiControlActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.rl_open, R.id.rl_close})
    public void onViewClicked(View v) {
        if(v.getId() == R.id.iv_back) {
            finish();
        } else if(v.getId() == R.id.rl_open) {
            jumpControl(true);
        }else if(v.getId() == R.id.rl_close) {
            jumpControl(false);
        }
    }
}
