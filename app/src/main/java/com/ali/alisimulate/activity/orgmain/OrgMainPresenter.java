package com.ali.alisimulate.activity.orgmain;

import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.service.AppService;
import com.ali.alisimulate.util.ToastUtils;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ApiException;

import io.reactivex.disposables.Disposable;

public class OrgMainPresenter extends BaseMvpPresenter<OrgMainContract.IView> implements OrgMainContract.IPresenter {
    public OrgMainPresenter(OrgMainContract.IView view) {
        super(view);
    }

    @Override
    public void getDevice(String productKey) {
        ApiUtil.getResponseNoBody(ApiUtil.getService(AppService.class).getDevices(productKey), new OnResponseListener<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Result entity) {

            }

            @Override
            public void onError(ApiException e) {
//                super.onError(e);
            }
        });
    }
}
