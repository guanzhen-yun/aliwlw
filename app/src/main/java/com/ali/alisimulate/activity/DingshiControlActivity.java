package com.ali.alisimulate.activity;

import com.ali.alisimulate.R;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

/**
 * Author:关震
 * Date:2020/5/16 8:06
 * Description:DingshiControlActivity
 **/
@ViewInject(layoutId = R.layout.activity_dingshi_control)
public class DingshiControlActivity extends BaseActivity {
    private String title;
    @Override
    public void fetchIntents() {
        title = getIntent().getStringExtra("title");
    }


}
