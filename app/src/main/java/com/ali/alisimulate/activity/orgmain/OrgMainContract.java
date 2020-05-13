package com.ali.alisimulate.activity.orgmain;

import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

import java.util.List;

public interface OrgMainContract {
    interface IView extends IMvpView {
        void getUserInfoSuccess(UserInfoEntity entity);

        void logoutSuccess();

        void getDeviceListSuccess(List<OrgDevice> orgDevices);
    }

    interface IPresenter extends ILifeCircle {
        void getUserInfo();

        void logout();

        void getDeviceList(int pageIndex, int pageSize, String productKey);

    }
}
