package com.ali.alisimulate.activity.login;

import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

public interface LoginContract {
    interface IView extends IMvpView {
        void loginResult(LoginSuccess loginSuccess);
    }

    interface IPresenter extends ILifeCircle {
        void login(LoginModel loginModel);
    }
}
