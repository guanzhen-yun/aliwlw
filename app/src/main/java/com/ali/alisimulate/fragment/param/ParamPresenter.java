package com.ali.alisimulate.fragment.param;

import com.ali.alisimulate.entity.FittingDetailEntity;
import com.ali.alisimulate.entity.LvXinEntity;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ali.alisimulate.service.AppService;
import com.ziroom.mvp.base.BaseMvpPresenter;
import com.ziroom.net.ApiUtil;
import com.ziroom.net.OnResponseListener;
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
    public void getPjInfo(LvXinEntity entity) {
        ApiUtil.getResponse(ApiUtil.getService(AppService.class).getFitingInfo(entity.lvxinDeviceName), new OnResponseListener<FittingDetailEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(FittingDetailEntity detailEntity) {
                mView.getPjInfoSuccess(detailEntity, entity);
            }

            @Override
            public void onError(ApiException e) {

            }
        });
    }
}
