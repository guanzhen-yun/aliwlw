package com.ali.alisimulate.activity.testapi;

import com.ali.alisimulate.entity.RegistModel;
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
 * Date:2020/5/11 12:22
 * Description:TestPresenter
 **/
public class TestPresenter extends BaseMvpPresenter<TestContract.IView> implements TestContract.IPresenter {
    public TestPresenter(TestContract.IView view) {
        super(view);
    }

    @Override
    public void sendMvpRequest(RegistModel registModel) {
        ApiUtil.getResponseNoBody(ApiUtil.getService(AppService.class).regist(registModel), new OnResponseListener<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Result entity) {
                mView.getResult();
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ToastUtils.showToast("注册失败" + e.getDisplayMessage());
            }
        });
    }
}
