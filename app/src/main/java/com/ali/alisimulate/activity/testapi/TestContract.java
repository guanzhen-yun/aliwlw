package com.ali.alisimulate.activity.testapi;

import com.ali.alisimulate.entity.RegistModel;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
 * Description:TestContract
 **/
public interface TestContract {
    interface IView extends IMvpView {
        void getResult();
    }

    interface IPresenter extends ILifeCircle {
        void sendMvpRequest(RegistModel registModel);
    }
}
