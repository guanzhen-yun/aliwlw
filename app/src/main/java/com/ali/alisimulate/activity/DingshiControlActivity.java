package com.ali.alisimulate.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.WeekAdapter;
import com.ali.alisimulate.entity.KeyValue;
import com.ali.alisimulate.entity.WeekEntity;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.view.wheelview.WheelView;
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
 * Date:2020/5/16 8:06
 * Description:DingshiControlActivity
 **/
@ViewInject(layoutId = R.layout.activity_dingshi_control)
public class DingshiControlActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.wv_content_hour)
    WheelView mWvContentHour;
    @BindView(R.id.wv_content_minute)
    WheelView mWvContentMinute;
    @BindView(R.id.rv_week)
    RecyclerView mRvWeek;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private String title;
    private String hour;
    private String minite;
    private WeekAdapter adapter;
    private List<WeekEntity> weekEntities;
    private List<ValueWrapper> localTimer;

    @Override
    public void fetchIntents() {
        title = getIntent().getStringExtra("title");
    }

    @Override
    public void initViews() {
        mTvTitle.setText(title);
        localTimer = SaveAndUploadAliUtil.getList("LocalTimer");
        weekEntities = new ArrayList<>();
        weekEntities.add(new WeekEntity("周一"));
        weekEntities.add(new WeekEntity("周二"));
        weekEntities.add(new WeekEntity("周三"));
        weekEntities.add(new WeekEntity("周四"));
        weekEntities.add(new WeekEntity("周五"));
        weekEntities.add(new WeekEntity("周六"));
        weekEntities.add(new WeekEntity("周日"));
        adapter = new WeekAdapter(weekEntities);
        mRvWeek.setAdapter(adapter);
        hour = "01";
        minite = "00";
        tvTime.setText(hour + ":" + minite);
        setInitData();
        setHour();
        setMinute();
        setListener();
    }

    //    selectWeek + ";" + hour + "," + minite
    private void setInitData() {
        if (localTimer != null && localTimer.size() == 2) {
            ValueWrapper valueWrapper = localTimer.get("定时关机".equals(title) ? 1 : 0);
            Map<String, ValueWrapper> value = (Map<String, ValueWrapper>) valueWrapper.getValue();
            if(value != null && value.size() > 0) {
                ValueWrapper.StringValueWrapper timer = (ValueWrapper.StringValueWrapper) value.get("Timer");
                if(timer != null) {
                    String value1 = timer.getValue();
                    String week = value1.split(";")[0];
                    String t = value1.split(";")[1];
                    if(!TextUtils.isEmpty(week)) {
                        for (WeekEntity weekEntity : weekEntities) {
                            if(week.contains(weekEntity.week)) {
                                weekEntity.isSelect = true;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if(!TextUtils.isEmpty(t)) {
                        hour = t.split(",")[0];
                        minite = t.split(",")[1];
                        tvTime.setText(hour + ":" + minite);
                    }
                }
            }
        }
    }

    private void setListener() {
        adapter.setOnSelectListener(new WeekAdapter.OnSelectListener() {
            @Override
            public void onSelect(int position, boolean isSelect) {
                weekEntities.get(position).isSelect = isSelect;
                adapter.notifyItemChanged(position);
            }
        });
        mWvContentHour.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, KeyValue item, int viewId) {
                hour = item.getValue();
                tvTime.setText(hour + ":" + minite);
            }
        });

        mWvContentMinute.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, KeyValue item, int viewId) {
                minite = item.getValue();
                tvTime.setText(hour + ":" + minite);
            }
        });
    }

    private void setMinute() {
        int initPos = 0;
        List<KeyValue> listMinute = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            KeyValue keyValue = null;
            if (i < 10) {
                keyValue = new KeyValue("0" + i);
            } else {
                keyValue = new KeyValue(String.valueOf(i));
            }
            if(keyValue.getValue().equals(minite)) {
                initPos = i;
            }
            listMinute.add(keyValue);
        }
        mWvContentMinute.setItems(listMinute, initPos);
    }

    private void setHour() {
        List<KeyValue> listHour = new ArrayList<>();
        int initPos = 0;
        for (int i = 1; i <= 24; i++) {
            KeyValue keyValue = null;
            if (i < 10) {
                keyValue = new KeyValue("0" + i);
            } else {
                keyValue = new KeyValue(String.valueOf(i));
            }
            if(keyValue.getValue().equals(hour)) {
                initPos = i-1;
            }
            listHour.add(keyValue);
        }
        mWvContentHour.setItems(listHour, initPos);
    }

    @OnClick({R.id.iv_back, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_ok:
                String selectWeek = "";
                for (WeekEntity weekEntity : weekEntities) {
                    if (weekEntity.isSelect) {
                        selectWeek = selectWeek + weekEntity.week + ",";
                    }
                }
                if (!TextUtils.isEmpty(selectWeek)) {
                    selectWeek = selectWeek.substring(0, selectWeek.length() - 1);
                }
                //第一个为开机  第二个为关机
                if (title.equals("定时关机")) {
                    saveTime(false, selectWeek);
                } else {
                    saveTime(true, selectWeek);
                }
                finish();
                break;
        }
    }

    private void saveTime(boolean isOpen, String selectWeek) {
        SharedPreferencesUtils.save(this, isOpen ? Constants.KEY_OPEN_WEEK : Constants.KEY_CLOSE_WEEK, selectWeek);
        SharedPreferencesUtils.save(this, isOpen ? Constants.KEY_OPEN_TIME : Constants.KEY_CLOSE_TIME, hour + "," + minite);
        Map<String, ValueWrapper> reportData = new HashMap<>();

        if (localTimer == null) {
            localTimer = new ArrayList<>();
            if (!isOpen) {//关机
                Map<String, ValueWrapper> value2 = new HashMap<>();
                ValueWrapper.StructValueWrapper structValueWrapper2 = new ValueWrapper.StructValueWrapper();
                structValueWrapper2.setValue(value2);
                localTimer.add(structValueWrapper2);
            }
            Map<String, ValueWrapper> value1 = new HashMap<>();
            addMap(value1, selectWeek, isOpen);
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
            addMap(value1, selectWeek, isOpen);
            structValueWrapper.setValue(value1);
        }
        SaveAndUploadAliUtil.putList("LocalTimer", reportData, localTimer);
        SaveAndUploadAliUtil.saveAndUpload(reportData);
    }

    private void addMap(Map<String, ValueWrapper> value1, String selectWeek, boolean isOpen) {
        value1.put("Timer", new ValueWrapper.StringValueWrapper(selectWeek + ";" + hour + "," + minite));
        value1.put("Enable", new ValueWrapper.BooleanValueWrapper(isOpen ?  1: 0));
    }
}
