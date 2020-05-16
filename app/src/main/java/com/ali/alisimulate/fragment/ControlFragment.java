package com.ali.alisimulate.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.DingShiActivity;
import com.ali.alisimulate.adapter.ControlAdapter;
import com.ali.alisimulate.entity.ReceiveMsg;
import com.ali.alisimulate.entity.RefreshEvent;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.google.gson.Gson;
import com.ziroom.base.BaseFragment;
import com.ziroom.base.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
    private boolean mIsOpen;

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
            if (list_deviceIndenty.contains(property.getIdentifier())) {
                map_control.put(property.getIdentifier(), property);
            }
        }

        if (map_control.size() == 0) {
            return;
        }

        if ("空气净化器".equals(deviceTitle)) {
            if (map_control.containsKey("WorkMode")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("WorkMode"));
                controlList.add(list);
            }
            if (map_control.containsKey("WindSpeed")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("WindSpeed"));
                controlList.add(list);
            }
            if (map_control.containsKey("SleepMode")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("SleepMode"));
                controlList.add(list);
            }
            if (map_control.containsKey("LocalTimer")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("LocalTimer"));
                controlList.add(list);
            }
            if (map_control.containsKey("ChildLockSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("ChildLockSwitch"));
                controlList.add(list);
            }
            if (map_control.containsKey("Humidified")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("Humidified"));
                controlList.add(list);
            }
            if (map_control.containsKey("longSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("longSwitch"));
                controlList.add(list);
            }
        } else if ("净水器".equals(deviceTitle)) {
            if (map_control.containsKey("WashingSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("WashingSwitch"));
                if (map_control.containsKey("WashingState")) {
                    list.add(map_control.get("WashingSwitch"));
                }
                if (map_control.containsKey("WashingPercent")) {
                    list.add(map_control.get("WashingPercent"));
                }
                controlList.add(list);
            }

            if (map_control.containsKey("pureSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("pureSwitch"));
                if (map_control.containsKey("PureState")) {
                    list.add(map_control.get("PureState"));
                }
                if (map_control.containsKey("PurePercent")) {
                    list.add(map_control.get("PurePercent"));
                }
                controlList.add(list);
            }

            if (map_control.containsKey("ChildLockSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("ChildLockSwitch"));
                controlList.add(list);
            }
            if (map_control.containsKey("LocalTimer")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("LocalTimer"));
                controlList.add(list);
            }
        }
        adapter.notifyDataSetChanged();

        if (map_control.containsKey("PowerSwitch")) {
            mIvClose.setVisibility(View.VISIBLE);
            Property powerSwitch = map_control.get("PowerSwitch");
            if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(powerSwitch.getDataType().getType())) {
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(powerSwitch.getIdentifier());
                if (propertyValue != null) {
                    Integer value = ((ValueWrapper.BooleanValueWrapper) propertyValue).getValue();
                    if (value != null && value == 1) {
                        mIsOpen = true;
                        mIvClose.setBackgroundResource(R.mipmap.icon_opendevice);
                    } else {
                        mIsOpen = false;
                        mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                    }
                } else {
                    mIsOpen = false;
                    mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                }
            }
        } else {
            mIvClose.setVisibility(View.GONE);
        }

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, DingShiActivity.class));
            }
        });
    }

    private CountDownTimer countDownTimer = null;

    private String getDateStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map_control.containsKey("PowerSwitch")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            String w = "";
            switch (week) {
                case 0:
                    w = "周日";
                    break;
                case 1:
                    w = "周一";
                    break;
                case 2:
                    w = "周二";
                    break;
                case 3:
                    w = "周三";
                    break;
                case 4:
                    w = "周四";
                    break;
                case 5:
                    w = "周五";
                    break;
                case 6:
                    w = "周六";
                    break;
            }

            String time = getDateStr();

            String year = time.split("-")[0];
            String month = time.split("-")[1];
            String day = time.split("-")[2].split(" ")[0];

            long openT = 0;
            long closeT = 0;

            String weeks = SharedPreferencesUtils.getStr(mContext, Constants.KEY_OPEN_WEEK);
            if (weeks.contains(w)) {
                String str = SharedPreferencesUtils.getStr(mContext, Constants.KEY_OPEN_TIME);
                String hour = str.split(",")[0];
                String minute = str.split(",")[1];

                String openTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + 00;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                Date date = null;
                try {
                    date = sdf.parse(openTime);
                } catch (Exception e) {
                }
                openT = date.getTime();
            }

            String weekC = SharedPreferencesUtils.getStr(mContext, Constants.KEY_CLOSE_WEEK);
            if (weekC.contains(w)) {
                String str = SharedPreferencesUtils.getStr(mContext, Constants.KEY_CLOSE_TIME);
                String hour = str.split(",")[0];
                String minute = str.split(",")[1];

                String openTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + 00;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                Date date = null;
                try {
                    date = sdf.parse(openTime);
                } catch (Exception e) {
                }
                closeT = date.getTime();
            }

            long currentTime = System.currentTimeMillis();
            long totalTime = 0;
            if (mIsOpen && closeT > 0) {//关机的倒计时
                totalTime = closeT - currentTime;
            } else if (!mIsOpen && openT > 0) {
                totalTime = openT - currentTime;
            }

            if (totalTime > 0) {
                countDownTimer = new CountDownTimer(totalTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {//剩余的时间

                    }

                    @Override
                    public void onFinish() {
                        if (mIsOpen) {
                            mIsOpen = false;
                            //关机
                            // 设备上报
                            Map<String, ValueWrapper> reportData = new HashMap<>();
                            // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                            reportData.put("PowerSwitch", new ValueWrapper.BooleanValueWrapper(0));  // 参考示例，更多使用可参考demo
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
                            mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                        } else {
                            mIsOpen = true;
                            //开机
                            // 设备上报
                            Map<String, ValueWrapper> reportData = new HashMap<>();
                            // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                            reportData.put("PowerSwitch", new ValueWrapper.BooleanValueWrapper(1));  // 参考示例，更多使用可参考demo
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
                            mIvClose.setBackgroundResource(R.mipmap.icon_opendevice);
                        }
                    }
                };
                countDownTimer.start();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetStickyEvent(RefreshEvent message) {
        if (message != null && message.aMessage != null) {
            String data = new String((byte[]) message.aMessage.data);
            String s = data.split("data=")[1];
            ReceiveMsg receiveMsg = new Gson().fromJson(s, ReceiveMsg.class);
            Map<String, Object> params = receiveMsg.params;
            for (int i = 0; i < controlList.size(); i++) {
                Property property = controlList.get(i).get(0);
                if (params.containsKey(property.getIdentifier())) {
                    adapter.setDataByPos(i, params.get(property.getIdentifier()));
                }
            }
        }
    }

    @Override
    public void initViews(View mView) {
        EventBus.getDefault().register(this);
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
