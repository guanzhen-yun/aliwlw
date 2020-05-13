package com.ali.alisimulate.activity.login;

import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.service.AppService;
import com.ali.alisimulate.util.ToastUtils;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.exception.ApiException;

import io.reactivex.disposables.Disposable;

public class LoginPresenter extends BaseMvpPresenter<LoginContract.IView> implements LoginContract.IPresenter{
    public LoginPresenter(LoginContract.IView view) {
        super(view);
    }

    @Override
    public void login(LoginModel loginModel) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).login(loginModel), new OnResponseListener<LoginSuccess>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(LoginSuccess entity) {
                mView.loginResult(entity);
            }

            @Override
            public void onError(ApiException e) {
                ToastUtils.showToast("用户名或密码错误");
            }
        });
    }
}
