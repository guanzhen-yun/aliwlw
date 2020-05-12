package com.ali.alisimulate.activity.orgmain;

import com.ali.alisimulate.entity.DeviceDetailEntity;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

public interface OrgMainContract {
    interface IView extends IMvpView {
        void getDeviceResult(DeviceDetailEntity deviceDetailEntity);
        void getUserInfoSuccess(UserInfoEntity entity);
        void logoutSuccess();
    }

    interface IPresenter extends ILifeCircle {
        void getDevice(String productKey);
        void getUserInfo();
        void logout();
        void getDeviceDetail(String deviceId);
    }
}
