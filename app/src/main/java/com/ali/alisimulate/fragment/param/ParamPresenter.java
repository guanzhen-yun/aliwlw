package com.ali.alisimulate.fragment.param;

import com.ziroom.mvp.base.BaseMvpPresenter;

/**
 * Author:关震
 * Date:2020/5/14 21:35
 * Description:ParamPresenter
 **/
public class ParamPresenter extends BaseMvpPresenter<ParamContract.IView> implements ParamContract.IPresenter{
    public ParamPresenter(ParamContract.IView view) {
        super(view);
    }

    @Override
    public void getDeviceInfo() {

    }

    @Override
    public void getPjInfo() {

    }
}
