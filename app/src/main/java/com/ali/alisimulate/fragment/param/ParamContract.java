package com.ali.alisimulate.fragment.param;

import com.ali.alisimulate.entity.DeviceDetail;
import com.ali.alisimulate.entity.RegistModel;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
 * Author:关震
 * Date:2020/5/14 21:35
 * Description:ParamContract
 **/
public interface ParamContract {
    interface IView extends IMvpView {
        void getDeviceInfoSuccess(DeviceDetail deviceDetail);
        void getPjInfoSuccess();
    }

    interface IPresenter extends ILifeCircle {
        void getDeviceInfo(String deviceId);
        void getPjInfo();
    }
}
