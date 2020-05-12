package com.ali.alisimulate.activity.orgmain;

import android.text.TextUtils;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.entity.DeviceDetailEntity;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ali.alisimulate.service.AppService;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.util.UserUtils;
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
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getDevices(productKey), new OnResponseListener<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(String entity) {
                getDeviceDetail(entity);
            }

            @Override
            public void onError(ApiException e) {
            }
        });
    }

    @Override
    public void getUserInfo() {
        String userId = UserUtils.getUserId(MyApp.getApp());
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getUserInfo(userId), new OnResponseListener<UserInfoEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(UserInfoEntity entity) {
                mView.getUserInfoSuccess(entity);
            }

            @Override
            public void onError(ApiException e) {

            }
        });
    }

    @Override
    public void logout() {
        ApiUtil.getResponseNoBody(ApiUtil.getService(AppService.class).logout(), new OnResponseListener<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Result entity) {
                SharedPreferencesUtils.save(MyApp.getApp(), Constants.KEY_LOGIN_INFO, null);
                mView.logoutSuccess();
            }

            @Override
            public void onError(ApiException e) {

            }
        });
    }

    @Override
    public void getDeviceDetail(String deviceId) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getDevicesDetail(deviceId), new OnResponseListener<DeviceDetailEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(DeviceDetailEntity entity) {
                mView.getDeviceResult(entity);
            }

            @Override
            public void onError(ApiException e) {
                mView.getDeviceResult(null);
            }
        });
    }

}
