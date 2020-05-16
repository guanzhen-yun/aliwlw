package com.ali.alisimulate.activity;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.WeekAdapter;
import com.ali.alisimulate.entity.KeyValue;
import com.ali.alisimulate.entity.WeekEntity;
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

    private String title;
    private String hour;
    private String minite;

    @Override
    public void fetchIntents() {
        title = getIntent().getStringExtra("title");
    }

    @Override
    public void initViews() {
        mTvTitle.setText(title);
        List<WeekEntity> list = new ArrayList<>();
        list.add(new WeekEntity("周一"));
        list.add(new WeekEntity("周二"));
        list.add(new WeekEntity("周三"));
        list.add(new WeekEntity("周四"));
        list.add(new WeekEntity("周五"));
        list.add(new WeekEntity("周六"));
        list.add(new WeekEntity("周日"));
        WeekAdapter adapter = new WeekAdapter(list);
        mRvWeek.setAdapter(adapter);

        setHour();
        setMinute();

        setListener();
    }

    private void setListener() {

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
        finish();
    }
}
