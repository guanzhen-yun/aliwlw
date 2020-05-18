package com.ali.alisimulate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.ali.alisimulate.util.SharedPreferencesUtils;
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
                mSwOpen.setChecked(true);
            } else {
                mSwOpen.setChecked(false);
            }
            Map<String, ValueWrapper> value2 = (Map<String, ValueWrapper>) value.get(1).getValue();
            if(value2.size() > 0) {
                mSwClose.setChecked(true);
            } else {
                mSwClose.setChecked(false);
            }
        }

        mSwOpen.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                saveOpenTime(true);
            } else {
                closeStatus(true);
            }
        });

        mSwClose.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                saveOpenTime(false);
            } else {
                closeStatus(false);
            }
        });
    }

    private void saveOpenTime(boolean isOpen) {
        Map<String, ValueWrapper> reportData = new HashMap<>();
       List<ValueWrapper> localTimer = SaveAndUploadAliUtil.getList("LocalTimer");
        if (localTimer == null) {
            localTimer = new ArrayList<>();
            if (!isOpen) {//关机
                Map<String, ValueWrapper> value2 = new HashMap<>();
                ValueWrapper.StructValueWrapper structValueWrapper2 = new ValueWrapper.StructValueWrapper();
                structValueWrapper2.setValue(value2);
                localTimer.add(structValueWrapper2);
            }
            Map<String, ValueWrapper> value1 = new HashMap<>();
            addMap(value1, isOpen);
            ValueWrapper.StructValueWrapper structValueWrapper = new ValueWrapper.StructValueWrapper();
            structValueWrapper.setValue(value1);
            localTimer.add(structValueWrapper);
            if (isOpen) {//开机
                Map<String, ValueWrapper> value2 = new HashMap<>();
                ValueWrapper.StructValueWrapper structValueWrapper2 = new ValueWrapper.StructValueWrapper();
                structValueWrapper2.setValue(value2);
                localTimer.add(structValueWrapper2);
            }
        } else if (localTimer.size() == 2) {
            ValueWrapper.StructValueWrapper structValueWrapper = (ValueWrapper.StructValueWrapper) localTimer.get(isOpen ? 0 : 1);
            Map<String, ValueWrapper> value1 = new HashMap<>();
            addMap(value1, isOpen);
            structValueWrapper.setValue(value1);
        }
        SaveAndUploadAliUtil.putList("LocalTimer", reportData, localTimer);
        SaveAndUploadAliUtil.saveAndUpload(reportData);
    }

    private void addMap(Map<String, ValueWrapper> value1, boolean isOpen) {
        value1.put("Enable", new ValueWrapper.BooleanValueWrapper(isOpen ? 1 : 0));
    }

    private void jumpControl(boolean isOpen) {
        Bundle bundle = new Bundle();
        bundle.putString("title", isOpen ? "定时开机" : "定时关机");
        Intent intent = new Intent(DingShiActivity.this, DingshiControlActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void closeStatus(boolean isOpen) {
        SharedPreferencesUtils.save(DingShiActivity.this, isOpen ? Constants.KEY_OPEN_WEEK : Constants.KEY_CLOSE_WEEK, "");
        SharedPreferencesUtils.save(DingShiActivity.this, isOpen ? Constants.KEY_OPEN_TIME : Constants.KEY_CLOSE_TIME, "");
        Map<String, ValueWrapper> reportData = new HashMap<>();
        List<ValueWrapper> localTimer = SaveAndUploadAliUtil.getList("LocalTimer");
        ValueWrapper.StructValueWrapper structValueWrapper = (ValueWrapper.StructValueWrapper) localTimer.get(isOpen ? 0 : 1);
        Map<String, ValueWrapper> value1 = new HashMap<>();
        structValueWrapper.setValue(value1);
        SaveAndUploadAliUtil.putList("LocalTimer", reportData, localTimer);
        SaveAndUploadAliUtil.saveAndUpload(reportData);
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
