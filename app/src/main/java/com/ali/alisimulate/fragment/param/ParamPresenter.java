package com.ali.alisimulate.fragment.param;

import com.ali.alisimulate.entity.DeviceDetail;
import com.ali.alisimulate.service.AppService;
import com.ali.alisimulate.util.ToastUtils;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ApiException;

import io.reactivex.disposables.Disposable;

/**
 * Author:关震
 * Date:2020/5/14 21:35
 * Description:ParamPresenter
 **/
public class ParamPresenter extends BaseMvpPresenter<ParamContract.IView> implements ParamContract.IPresenter{
    public ParamPresenter(ParamContract.IView view) {
        super(view);
    }

    @Override
    public void getDeviceInfo(String deviceId) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getDeviceDetail(deviceId), new OnResponseListener<DeviceDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(DeviceDetail entity) {
                mView.getDeviceInfoSuccess(entity);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ToastUtils.showToast("注册失败" + e.getDisplayMessage());
            }
        });
    }

    @Override
    public void getPjInfo() {

    }
}
