package com.ali.alisimulate.activity.regist;

import com.ali.alisimulate.entity.RegistModel;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
 * Description:RegistContract
 **/
public interface RegistContract {
    interface IView extends IMvpView {
        void getResult();
    }

    interface IPresenter extends ILifeCircle {
        void sendMvpRequest(RegistModel registModel);
    }
}
