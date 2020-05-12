package com.ali.alisimulate.activity.adddevice;

import com.ali.alisimulate.entity.BranchEntity;
import com.ali.alisimulate.entity.BranchTypeEntity;
import com.ali.alisimulate.entity.RegistDeviceRequest;
import com.ali.alisimulate.entity.RegistDeviceResult;
import com.ali.alisimulate.service.AppService;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
import com.ziroom.net.exception.ApiException;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Author:关震
 * Date:2020/5/12 15:04
 * Description:AddDevicePresenter
 **/
public class AddDevicePresenter extends BaseMvpPresenter<AddDeviceContract.IView> implements AddDeviceContract.IPresenter {

    public AddDevicePresenter(AddDeviceContract.IView view) {
        super(view);
    }

    @Override
    public void getBranchList() {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getBranchList(), new OnResponseListener<List<BranchEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(List<BranchEntity> entity) {
                mView.getBranchListResult(entity);
            }

            @Override
            public void onError(ApiException e) {
            }
        });
    }

    @Override
    public void getBranchTypeList(String id) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getBranchTypeList(id), new OnResponseListener<List<BranchTypeEntity>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(List<BranchTypeEntity> entity) {
                mView.getBranchTypeListResult(entity);
            }

            @Override
            public void onError(ApiException e) {
            }
        });
    }

    @Override
    public void registDevice(RegistDeviceRequest request) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).registerDevice(request), new OnResponseListener<RegistDeviceResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(RegistDeviceResult result) {
                mView.registDeviceSuccess(result);
            }

            @Override
            public void onError(ApiException e) {
                mView.registDeviceSuccess(null);
            }
        });
    }
}
