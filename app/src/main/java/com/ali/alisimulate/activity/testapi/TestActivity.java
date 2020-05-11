package com.ali.alisimulate.activity.testapi;

import com.ali.alisimulate.R;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import butterknife.OnClick;

/**
 * Description:TestActivity
 **/
@ViewInject(layoutId = R.layout.activity_test)
public class TestActivity extends BaseActivity<TestPresenter> implements TestContract.IView {

    @Override
    public void initDatas() {

    }

    @Override
    public void getMvpResult(String result) {

    }

    @OnClick(R.id.btn_get)
    public void onViewClicked() {
        mPresenter.sendMvpRequest();
    }

    @Override
    public TestPresenter getPresenter() {
        return new TestPresenter(this);
    }
}
