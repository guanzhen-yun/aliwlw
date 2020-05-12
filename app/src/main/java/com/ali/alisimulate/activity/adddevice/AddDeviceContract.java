package com.ali.alisimulate.activity.adddevice;

import com.ali.alisimulate.entity.BranchEntity;
import com.ali.alisimulate.entity.BranchTypeEntity;
import com.ali.alisimulate.entity.RegistDeviceRequest;
import com.ali.alisimulate.entity.RegistDeviceResult;
import com.ziroom.mvp.ILifeCircle;
import com.ziroom.mvp.IMvpView;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/12 15:03
 * Description:AddDeviceContract
 **/
public interface AddDeviceContract {
    interface IView extends IMvpView {
        void getBranchListResult(List<BranchEntity> list);

        void getBranchTypeListResult(List<BranchTypeEntity> list);

        void registDeviceSuccess(RegistDeviceResult registDeviceResult);
    }

    interface IPresenter extends ILifeCircle {
        void getBranchList();

        void getBranchTypeList(String id);

        void registDevice(RegistDeviceRequest request);
    }
}
