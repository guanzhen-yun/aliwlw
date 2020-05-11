package com.ali.alisimulate.activity.testapi;

import android.util.Log;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.Regist;
import com.ali.alisimulate.service.AppService;
import com.ali.alisimulate.util.ToastUtils;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.bean.Result;
import com.ziroom.net.exception.ApiException;

import java.util.HashMap;
import java.util.Map;

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
    public void sendMvpRequest() {
        Regist regist = new Regist();
        regist.Name = "张三";

        ApiUtil.getResponseNoBody(ApiUtil.getService(AppService.class).regist(regist), new OnResponseListener<Result>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Result entity) {
                if (entity.getCode() == 0) {
                    ToastUtils.showToast("成功");
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }
        });
    }
}
