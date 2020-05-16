package com.ali.alisimulate.fragment.param;

import com.ali.alisimulate.entity.FittingDetailEntity;
import com.ali.alisimulate.entity.LvXinEntity;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

/**
 * Author:关震
 * Date:2020/5/14 21:35
 * Description:ParamContract
 **/
public interface ParamContract {
    interface IView extends IMvpView {
        void getPjInfoSuccess(FittingDetailEntity fittingDetailEntity, LvXinEntity entity);
    }

    interface IPresenter extends ILifeCircle {
        void getPjInfo(LvXinEntity entity);
    }
}
