package com.ali.alisimulate.activity.orgmain;

import android.text.TextUtils;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ali.alisimulate.service.AppService;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.util.UserUtils;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ApiException;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class OrgMainPresenter extends BaseMvpPresenter<OrgMainContract.IView> implements OrgMainContract.IPresenter {
    public OrgMainPresenter(OrgMainContract.IView view) {
        super(view);
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
    public void getDeviceList(int pageIndex, int pageSize, String productKey) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getDeviceList(pageIndex, pageSize, productKey), new OnResponseListener<List<OrgDevice>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(List<OrgDevice> entity) {
                mView.getDeviceListSuccess(entity);
            }

            @Override
            public void onError(ApiException e) {
                mView.getDeviceListSuccess(null);
            }
        });
    }


}
