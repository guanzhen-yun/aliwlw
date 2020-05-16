package com.ali.alisimulate.activity;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.WeekAdapter;
import com.ali.alisimulate.entity.KeyValue;
import com.ali.alisimulate.entity.WeekEntity;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.view.wheelview.WheelView;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void fetchIntents() {
        title = getIntent().getStringExtra("title");
    }

    @Override
    public void initViews() {
        mTvTitle.setText(title);
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
        setHour();
        setMinute();

        setListener();
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
        List<KeyValue> listMinute = new ArrayList<>();
        for (int i=0;i<=59;i++) {
            KeyValue keyValue = null;
            if(i < 10) {
                keyValue = new KeyValue("0" + i);
            } else {
                keyValue = new KeyValue(String.valueOf(i));
            }
            listMinute.add(keyValue);
        }
        mWvContentMinute.setItems(listMinute, 0);
    }

    private void setHour() {
        List<KeyValue> listHour = new ArrayList<>();
        for (int i=1;i<=24;i++) {
            KeyValue keyValue = null;
            if(i < 10) {
                keyValue = new KeyValue("0" + i);
            } else {
                keyValue = new KeyValue(String.valueOf(i));
            }
            listHour.add(keyValue);
        }
        mWvContentHour.setItems(listHour, 0);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        String selectWeek = "";
        for (WeekEntity weekEntity : weekEntities) {
            if(weekEntity.isSelect) {
                selectWeek = selectWeek + weekEntity.week + ",";
            }
        }
        if(title.equals("定时关机")) {
            SharedPreferencesUtils.save(this, Constants.KEY_CLOSE_WEEK, selectWeek);
            SharedPreferencesUtils.save(this, Constants.KEY_CLOSE_TIME, hour + "," + minite);
        } else {
            SharedPreferencesUtils.save(this, Constants.KEY_OPEN_WEEK, selectWeek);
            SharedPreferencesUtils.save(this, Constants.KEY_OPEN_TIME, hour + "," + minite);
        }
        finish();
    }
}
