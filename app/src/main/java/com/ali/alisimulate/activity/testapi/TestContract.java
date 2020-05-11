package com.ali.alisimulate.activity.testapi;

import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
 * Description:TestContract
 **/
public interface TestContract {
    interface IView extends IMvpView {
        void getMvpResult(String result);
    }

    interface IPresenter extends ILifeCircle {
        void sendMvpRequest();
    }
}
