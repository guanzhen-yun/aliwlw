package com.ali.alisimulate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.DingShiActivity;
import com.ali.alisimulate.adapter.ControlAdapter;
import com.ali.alisimulate.entity.ReceiveMsg;
import com.ali.alisimulate.entity.RefreshEvent;
import com.ali.alisimulate.util.ParamsUtil;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.gson.Gson;
import com.ziroom.base.BaseFragment;
import com.ziroom.base.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

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
        list_deviceIndenty = ParamsUtil.getControlParams(deviceTitle);
        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();

        if(properties == null) {
            return;
        }

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
            if (map_control.containsKey("IonsSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("IonsSwitch"));
                controlList.add(list);
            }
        } else if ("净水器".equals(deviceTitle)) {
            if (map_control.containsKey("WashingSwitch")) {//开关
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("WashingSwitch"));
                if (map_control.containsKey("WashingState")) {//状态
                    list.add(map_control.get("WashingState"));
                }
                if (map_control.containsKey("WashingPercent")) {//百分比
                    list.add(map_control.get("WashingPercent"));
                }
                controlList.add(list);
            }

            if (map_control.containsKey("PureSwitch")) {
                List<Property> list = new ArrayList<>();
                list.add(map_control.get("PureSwitch"));
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
            if (powerSwitch != null && TmpConstant.TYPE_VALUE_BOOLEAN.equals(powerSwitch.getDataType().getType())) {
                if(SaveAndUploadAliUtil.getBoolValue(powerSwitch.getIdentifier())) {
                    mIsOpen = true;
                    mIvClose.setBackgroundResource(R.mipmap.icon_opendevice);
                } else {
                    mIsOpen = false;
                    mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                }
                mIvClose.setOnClickListener(view -> {
                    Map<String, ValueWrapper> reportData = new HashMap<>();
                    SaveAndUploadAliUtil.putBoolParam(!mIsOpen,reportData,"PowerSwitch");
                    SaveAndUploadAliUtil.saveAndUpload(reportData);
                    SaveAndUploadAliUtil.saveBoolean(!mIsOpen, "PowerSwitch");
                    if(mIsOpen) {
                        mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                    } else {
                        mIvClose.setBackgroundResource(R.mipmap.icon_opendevice);
                    }
                    mIsOpen = !mIsOpen;
                });
            } else {
                mIvClose.setVisibility(View.GONE);
                mIsOpen = false;
            }
        } else {
            mIvClose.setVisibility(View.GONE);
            mIsOpen = false;
        }
    }

    private CountDownTimer countDownTimer = null;

    @Override
    public void onResume() {
        super.onResume();
        if (map_control.containsKey("PowerSwitch") && map_control.containsKey("LocalTimer")) {
            long openT = ParamsUtil.getOpenOrCloseTime(mContext, true);
            long closeT = ParamsUtil.getOpenOrCloseTime(mContext, false);
            adapter.setLocalOpenOrFalse();

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
                            // 设备上报
                            Map<String, ValueWrapper> reportData = new HashMap<>();
                            SaveAndUploadAliUtil.putBoolParam(mIsOpen, reportData, "PowerSwitch");
                            SaveAndUploadAliUtil.saveAndUpload(reportData);
                            SaveAndUploadAliUtil.saveBoolean(mIsOpen, "PowerSwitch");
                            mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                        } else {
                            mIsOpen = true;
                            Map<String, ValueWrapper> reportData = new HashMap<>();
                            SaveAndUploadAliUtil.putBoolParam(mIsOpen, reportData, "PowerSwitch");
                            SaveAndUploadAliUtil.saveAndUpload(reportData);
                            SaveAndUploadAliUtil.saveBoolean(mIsOpen, "PowerSwitch");
                            mIvClose.setBackgroundResource(R.mipmap.icon_opendevice);
                        }
                    }
                };
                countDownTimer.start();
            }
        }
    }

    /**
     * 处理一次的定时
     */

    private void handleOneTime() {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
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
            try {
                ReceiveMsg receiveMsg = new Gson().fromJson(data, ReceiveMsg.class);
                Map<String, Object> params = receiveMsg.params;
                if(params == null) {
                    return;
                }
                for (int i = 0; i < controlList.size(); i++) {
                    Property property = controlList.get(i).get(0);
                    if (params.containsKey(property.getIdentifier())) {
                        adapter.setDataByPos(i, params.get(property.getIdentifier()));
                    }
                }
                if(params.containsKey("PowerSwitch")) {
                    double pw = (double) params.get("PowerSwitch");
                    Map<String, ValueWrapper> reportData = new HashMap<>();
                    mIsOpen = (1 == pw);
                    SaveAndUploadAliUtil.putBoolParam(mIsOpen,reportData,"PowerSwitch");
                    SaveAndUploadAliUtil.saveAndUpload(reportData);
                    SaveAndUploadAliUtil.saveBoolean(pw == 1, "PowerSwitch");
                    if(mIsOpen) {
                        mIvClose.setBackgroundResource(R.mipmap.icon_opendevice);
                    } else {
                        mIvClose.setBackgroundResource(R.mipmap.icon_closedevice);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int position = msg.what;
            if(controlList.get(position).size() == 3) {
                Property property = controlList.get(position).get(2);
                Integer intVal = SaveAndUploadAliUtil.getIntVal(property.getIdentifier());
                intVal = intVal + 1;
                if(intVal < 100) {
                    Map<String, ValueWrapper> reportData = new HashMap<>();
                    SaveAndUploadAliUtil.putIntParam(intVal, reportData, controlList.get(position).get(2).getIdentifier());
                    SaveAndUploadAliUtil.saveAndUpload(reportData);
                    Integer finalIntVal = intVal;
                    SaveAndUploadAliUtil.saveAndUpload(reportData, new SaveAndUploadAliUtil.OnUploadSuccessListener() {
                        @Override
                        public void onUnloadSuccess() {
                            SaveAndUploadAliUtil.saveInt(finalIntVal, controlList.get(position).get(2).getIdentifier());
                            adapter.notifyItemChanged(position);
                            handler.sendEmptyMessageDelayed(position, 1000);
                        }
                    });
                } else if(intVal == 100) {
                    handler.removeMessages(position);
                    Map<String, ValueWrapper> reportData = new HashMap<>();
                    Property statusP = controlList.get(position).get(1);
                    EnumSpec specs = (EnumSpec) statusP.getDataType().getSpecs();
                    Set<String> strings = specs.keySet();
                    List<String> listKey = new ArrayList<>();
                    for (String string : strings) {
                        listKey.add(string);
                    }
                    SaveAndUploadAliUtil.putIntParam(100, reportData, controlList.get(position).get(2).getIdentifier());
                    SaveAndUploadAliUtil.putEnumParam(Integer.parseInt(listKey.get(0)), reportData, controlList.get(position).get(1).getIdentifier());
                    SaveAndUploadAliUtil.saveAndUpload(reportData);
                    SaveAndUploadAliUtil.saveEnum(Integer.parseInt(listKey.get(0)), controlList.get(position).get(1).getIdentifier());
                    SaveAndUploadAliUtil.saveInt(100, controlList.get(position).get(2).getIdentifier());
                    adapter.notifyItemChanged(position);
                }
            }
        }
    };

    @Override
    public void initViews(View mView) {
        EventBus.getDefault().register(this);
        mRvControl.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        adapter = new ControlAdapter(controlList);
        adapter.setOnCheckListener(new ControlAdapter.OnCheckListener() {
            @Override
            public void onCheck(int position, boolean isOpen) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = controlList.get(position).get(0);
                if(TmpConstant.TYPE_VALUE_BOOLEAN.equals(property.getDataType().getType())) {
                    reportData.put(property.getIdentifier(), new ValueWrapper.BooleanValueWrapper(isOpen ? 1 : 0));  // 参考示例，更多使用可参考demo
                    SaveAndUploadAliUtil.saveBoolean(isOpen, property.getIdentifier());
                    if(controlList.get(position).size() == 3) {
                        Property statusP = controlList.get(position).get(1);
                        EnumSpec specs = (EnumSpec) statusP.getDataType().getSpecs();
                        Set<String> strings = specs.keySet();
                        List<String> listKey = new ArrayList<>();
                        for (String string : strings) {
                            listKey.add(string);
                        }
                        if(isOpen) {
                            SaveAndUploadAliUtil.putEnumParam(Integer.parseInt(listKey.get(listKey.size()-1)), reportData, statusP.getIdentifier());
                            SaveAndUploadAliUtil.putIntParam(0, reportData, controlList.get(position).get(2).getIdentifier());
                            SaveAndUploadAliUtil.saveEnum(Integer.parseInt(listKey.get(listKey.size()-1)), statusP.getIdentifier());
                            SaveAndUploadAliUtil.saveInt(0, controlList.get(position).get(2).getIdentifier());
                            SaveAndUploadAliUtil.saveAndUpload(reportData, new SaveAndUploadAliUtil.OnUploadSuccessListener() {
                                @Override
                                public void onUnloadSuccess() {
                                    handler.sendEmptyMessageDelayed(position, 1000);
                                }
                            });
                        } else {
                            handler.removeMessages(position);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    SaveAndUploadAliUtil.putEnumParam(Integer.parseInt(listKey.get(0)), reportData, statusP.getIdentifier());
                                    SaveAndUploadAliUtil.putIntParam(0, reportData, controlList.get(position).get(2).getIdentifier());
                                    SaveAndUploadAliUtil.saveAndUpload(reportData);
                                    SaveAndUploadAliUtil.saveEnum(Integer.parseInt(listKey.get(0)), statusP.getIdentifier());
                                    SaveAndUploadAliUtil.saveInt(0, controlList.get(position).get(2).getIdentifier());
                                    adapter.notifyItemChanged(position);
                                }
                            }, 500);
                        }
                    } else {
                        SaveAndUploadAliUtil.saveAndUpload(reportData);
                    }
                } else if(TmpConstant.TYPE_VALUE_ARRAY.equals(property.getDataType().getType()) && "LocalTimer".equals(property.getIdentifier())) {
                    //定时
                        reportData.put(property.getIdentifier(), null);
                        SharedPreferencesUtils.save(mContext, Constants.KEY_CLOSE_TIME, "");
                        SharedPreferencesUtils.save(mContext, Constants.KEY_CLOSE_WEEK, "");
                        SharedPreferencesUtils.save(mContext, Constants.KEY_OPEN_WEEK, "");
                        SharedPreferencesUtils.save(mContext, Constants.KEY_OPEN_TIME, "");
                        SaveAndUploadAliUtil.saveAndUpload(reportData);
                }
            }

            @Override
            public void onSelect(int position, String level) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = controlList.get(position).get(0);
                reportData.put(property.getIdentifier(), new ValueWrapper.EnumValueWrapper(Integer.parseInt(level)));  // 参考示例，更多使用可参考demo
                SaveAndUploadAliUtil.saveAndUpload(reportData);
                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(level), property.getIdentifier());
            }
        });
        mRvControl.setAdapter(adapter);
    }
}
