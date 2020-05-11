package com.ali.alisimulate.activity.orgmain;

import com.ali.alisimulate.entity.LoginSuccess;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

public interface OrgMainContract {
    interface IView extends IMvpView {
        void getDeviceResult();
    }

    interface IPresenter extends ILifeCircle {
        void getDevice(String productKey);
    }
}
