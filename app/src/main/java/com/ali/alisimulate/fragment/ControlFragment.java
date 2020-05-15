package com.ali.alisimulate.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.ControlAdapter;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.ziroom.base.BaseFragment;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(layoutId = R.layout.fragment_control)
public class ControlFragment extends BaseFragment {

    @BindView(R.id.rv_control)
    RecyclerView mRvControl;
    @BindView(R.id.iv_close)
    ImageView mIvClose;

    private String deviceTitle;

    private ArrayList<String> list_deviceIndenty = new ArrayList<>();//空气净化器 净水器

    private List<List<Property>> controlList = new ArrayList<>();
    private ControlAdapter adapter;
    private Map<String, Property> map_control = new HashMap<>();

    public List<String> getControlList() {
        return list_deviceIndenty;
    }

    public static ControlFragment getInstance(String title) {
        ControlFragment fragment = new ControlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("deviceTitle", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void fetchIntents(Bundle bundle) {
        deviceTitle = bundle.getString("deviceTitle");
    }

    @Override
    public void initDatas() {
        inputDatas();
        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();

        for (Property property : properties) {
            if(list_deviceIndenty.contains(property.getIdentifier())) {
                map_control.put(property.getIdentifier(), property);
            }
        }

        if(map_control.size() == 0) {
            return;
        }

            if("空气净化器".equals(deviceTitle)) {
                if(map_control.containsKey("WorkMode")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("WorkMode"));
                    controlList.add(list);
                }
                if(map_control.containsKey("WindSpeed")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("WindSpeed"));
                    controlList.add(list);
                }
                if(map_control.containsKey("SleepMode")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("SleepMode"));
                    controlList.add(list);
                }
                if(map_control.containsKey("LocalTimer")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("LocalTimer"));
                    controlList.add(list);
                }
                if(map_control.containsKey("ChildLockSwitch")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("ChildLockSwitch"));
                    controlList.add(list);
                }
                if(map_control.containsKey("Humidified")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("Humidified"));
                    controlList.add(list);
                }
                if(map_control.containsKey("longSwitch")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("longSwitch"));
                    controlList.add(list);
                }
            } else if("净水器".equals(deviceTitle)) {
                if(map_control.containsKey("WashingSwitch")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("WashingSwitch"));
                    if(map_control.containsKey("WashingState")) {
                        list.add(map_control.get("WashingSwitch"));
                    }
                    if(map_control.containsKey("WashingPercent")) {
                        list.add(map_control.get("WashingPercent"));
                    }
                    controlList.add(list);
                }

                if(map_control.containsKey("pureSwitch")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("pureSwitch"));
                    if(map_control.containsKey("PureState")) {
                        list.add(map_control.get("PureState"));
                    }
                    if(map_control.containsKey("PurePercent")) {
                        list.add(map_control.get("PurePercent"));
                    }
                    controlList.add(list);
                }

                if(map_control.containsKey("ChildLockSwitch")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("ChildLockSwitch"));
                    controlList.add(list);
                }
                if(map_control.containsKey("LocalTimer")) {
                    List<Property> list = new ArrayList<>();
                    list.add(map_control.get("LocalTimer"));
                    controlList.add(list);
                }
            }
        adapter.notifyDataSetChanged();
    }

    private void inputDatas() {
        list_deviceIndenty.add("PowerSwitch");//电源开关 bool 0-关闭 1-开启
        if ("空气净化器".equals(deviceTitle)) {
            list_deviceIndenty.add("WindSpeed");//风速  enum 0-自动 1-静音档 2-低档 3-中档 4-高档 5-最高档
            list_deviceIndenty.add("WorkMode");//自动模式 enum 0-自动 1-手动
            list_deviceIndenty.add("ChildLockSwitch");//童锁开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("Humidified");//加湿开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("longSwitch");//离子团开关  bool 0-关闭  1-打开
//            list_deviceIndenty.add("LocalTimer");//本地定时  array
            list_deviceIndenty.add("SleepMode");//睡眠模式  bool 0-关闭  1-打开
        } else if ("净水器".equals(deviceTitle)) {
            list_deviceIndenty.add("WashingPercent");//冲洗进度  int 0~100
            list_deviceIndenty.add("WashingState");//冲洗状态 enum 0-正常  1-冲洗中
            list_deviceIndenty.add("WashingSwitch");//冲洗开关 bool 0-关闭  1-打开
            list_deviceIndenty.add("ChildLockSwitch");//童锁开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("PurePercent");//制水进度 int 0~100
            list_deviceIndenty.add("PureState");//制水状态 enum 0待机 1制水中
            list_deviceIndenty.add("pureSwitch");//制水开关 bool 0-关闭  1-打开
//            list_deviceIndenty.add("LocalTimer");//本地定时  array
        }
    }

    @Override
    public void initViews(View mView) {
        mRvControl.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new ControlAdapter(getActivity(), controlList);
        adapter.setOnCheckListener(new ControlAdapter.OnCheckListener() {
            @Override
            public void onCheck(int position, boolean isOpen) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = controlList.get(position).get(0);
                reportData.put(property.getIdentifier(), new ValueWrapper.BooleanValueWrapper(isOpen ? 1 : 0));  // 参考示例，更多使用可参考demo
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
            public void onSelect(int position, String level) {
// 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = controlList.get(position).get(0);
                reportData.put(property.getIdentifier(), new ValueWrapper.EnumValueWrapper(Integer.parseInt(level)));  // 参考示例，更多使用可参考demo
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
        mRvControl.setAdapter(adapter);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        //跳轉定時開關機
    }
}
