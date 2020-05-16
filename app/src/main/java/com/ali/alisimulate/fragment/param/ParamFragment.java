package com.ali.alisimulate.fragment.param;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.DeviceDetailActivity;
import com.ali.alisimulate.adapter.ParamAdapter;
import com.ali.alisimulate.entity.DeviceDetail;
import com.ali.alisimulate.entity.ReceiveMsg;
import com.ali.alisimulate.entity.RefreshEvent;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.view.DropDownPop;
import com.ali.alisimulate.view.TopViewCycle;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.google.gson.Gson;
import com.ziroom.base.BaseFragment;
import com.ziroom.base.ViewInject;
import com.ziroom.mvp.base.BaseMvpPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

@ViewInject(layoutId = R.layout.fragment_param)
public class ParamFragment extends BaseFragment<BaseMvpPresenter> implements ParamContract.IView {
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    private List<Property> paramList;
    private ParamAdapter adapter;

    @Override
    public void initViews(View mView) {
        EventBus.getDefault().register(this);
        DropDownPop dropDownPop = new DropDownPop();
        dropDownPop.init(getActivity());
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ParamAdapter();

        List<Event> events = LinkKit.getInstance().getDeviceThing().getEvents();

        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();
        paramList = new ArrayList<>();
        for (Property property : properties) {
            List<String> controlList = ((DeviceDetailActivity) getActivity()).getControlList();
            if (controlList != null && controlList.contains(property.getIdentifier())) {
                continue;
            }
            if (TmpConstant.TYPE_VALUE_ENUM.equals(property.getDataType().getType()) || TmpConstant.TYPE_VALUE_INTEGER.equals(property.getDataType().getType())) {
                paramList.add(property);
            }
        }

        adapter.addDatas(paramList);
        mRvList.setAdapter(adapter);

        adapter.setOnCheckedListener(new ParamAdapter.OnCheckedListener() {
            @Override
            public void onSelect(int position, String selectId) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = paramList.get(position);
                reportData.put(property.getIdentifier(), new ValueWrapper.EnumValueWrapper(Integer.parseInt(selectId)));  // 参考示例，更多使用可参考demo
                LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
                    @Override
                    public void onSuccess(String resID, Object o) {
                        // 属性上报成功 resID 设备属性对应的唯一标识
                        Log.e("ProductActivity", "属性上报成功");
                    }

                    @Override
                    public void onError(String resId, AError aError) {
                        // 属性上报失败
                        Log.e("ProductActivity", "属性上报失败");
                    }
                });
            }

            @Override
            public void onChange(int realPosition, String et) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = paramList.get(realPosition);
                reportData.put(property.getIdentifier(), new ValueWrapper.IntValueWrapper(Integer.parseInt(et)));  // 参考示例，更多使用可参考demo
                LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
                    @Override
                    public void onSuccess(String resID, Object o) {
                        // 属性上报成功 resID 设备属性对应的唯一标识
                        Log.e("ProductActivity", "属性上报成功");
                    }

                    @Override
                    public void onError(String resId, AError aError) {
                        // 属性上报失败
                        Log.e("ProductActivity", "属性上报失败");
                    }
                });
            }
        });

        TopViewCycle topViewCycle = new TopViewCycle(getActivity());
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("顶部");
        list1.add("顶部");
        list1.add("顶部");
        list1.add("顶部");
        topViewCycle.loadData(list1);
        topViewCycle.setOnPageClickListener(new TopViewCycle.OnPageClickListener() {
            @Override
            public void onClick(int position) {
                ToastUtils.showToast(position + "");
                dropDownPop.showPop(mRvList);
            }
        });
        adapter.setHeadView(topViewCycle);
    }

    @Override
    public void getDeviceInfoSuccess(DeviceDetail deviceDetail) {
        List<DeviceDetail.FittingDetail> fittingDetails = deviceDetail.fittingDetails;
        for (DeviceDetail.FittingDetail fittingDetail : fittingDetails) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getPjInfoSuccess() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetStickyEvent(RefreshEvent message) {
        if (message != null && message.aMessage != null) {
            String data = new String((byte[]) message.aMessage.data);
            String s = data.split("data=")[1];
            ReceiveMsg receiveMsg = new Gson().fromJson(s, ReceiveMsg.class);
            Map<String, Object> params = receiveMsg.params;
            for (int i = 0; i < paramList.size(); i++) {
                Property property = paramList.get(i);
                if(params.containsKey(property.getIdentifier())) {
                    adapter.setDataByPos(i, params.get(property.getIdentifier()));
                }
            }
        }
    }
}
