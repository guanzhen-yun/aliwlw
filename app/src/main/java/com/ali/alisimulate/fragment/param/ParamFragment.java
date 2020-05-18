package com.ali.alisimulate.fragment.param;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.DeviceDetailActivity;
import com.ali.alisimulate.adapter.ParamAdapter;
import com.ali.alisimulate.entity.FittingDetailEntity;
import com.ali.alisimulate.entity.FittingResetDetailEntity;
import com.ali.alisimulate.entity.LvXinEntity;
import com.ali.alisimulate.entity.ReceiveMsg;
import com.ali.alisimulate.entity.RefreshEvent;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.ali.alisimulate.view.DropDownPop;
import com.ali.alisimulate.view.DropDownScanPop;
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
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;
import com.ziroom.base.BaseFragment;
import com.ziroom.base.ViewInject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

@ViewInject(layoutId = R.layout.fragment_param)
public class ParamFragment extends BaseFragment<ParamPresenter> implements ParamContract.IView {
    @BindView(R.id.rv_list)
    RecyclerView mRvList;
    private List<Property> paramList;
    private ParamAdapter adapter;

    private String deviceId;

    private Map<String, Property> mapLx = new HashMap<>();
    private DropDownPop dropDownPop;
    private DropDownScanPop dropDownScanPop;
    private ArrayList<LvXinEntity> listLx;
    private TopViewCycle topViewCycle;

    public static ParamFragment getInstance(String deviceId) {
        ParamFragment fragment = new ParamFragment();
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", deviceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void fetchIntents(Bundle bundle) {
        deviceId = bundle.getString("deviceId");
    }

    @Override
    public void initViews(View mView) {
        EventBus.getDefault().register(this);
        dropDownPop = new DropDownPop();
        dropDownPop.init(getActivity());
        dropDownScanPop = new DropDownScanPop();
        dropDownScanPop.init(getActivity());
        mRvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ParamAdapter();

        List<Event> events = LinkKit.getInstance().getDeviceThing().getEvents();//TODO
        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();
        paramList = new ArrayList<>();
        for (Property property : properties) {
            List<String> controlList = ((DeviceDetailActivity) getActivity()).getControlList();
            if (controlList != null && controlList.contains(property.getIdentifier())) {
                continue;
            }
            if (property.getIdentifier().contains("Filter")) {
                mapLx.put(property.getIdentifier(), property);
                continue;
            }
            if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(property.getDataType().getType()) || TmpConstant.TYPE_VALUE_ENUM.equals(property.getDataType().getType()) || TmpConstant.TYPE_VALUE_INTEGER.equals(property.getDataType().getType())|| TmpConstant.TYPE_VALUE_DOUBLE.equals(property.getDataType().getType())) {
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

                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(selectId), property.getIdentifier());
                SaveAndUploadAliUtil.saveAndUpload(reportData);
            }

            @Override
            public void onSelectBool(int position, String value) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = paramList.get(position);
                reportData.put(property.getIdentifier(), new ValueWrapper.BooleanValueWrapper(Integer.parseInt(value)));  // 参考示例，更多使用可参考demo

                SaveAndUploadAliUtil.saveBoolean(Integer.parseInt(value) == 1, property.getIdentifier());
                SaveAndUploadAliUtil.saveAndUpload(reportData);
            }

            @Override
            public void onChange(int realPosition, String et) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = paramList.get(realPosition);
                if(property.getDataType().getType().equals(TmpConstant.TYPE_VALUE_DOUBLE)) {
                    reportData.put(property.getIdentifier(), new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et)));  // 参考示例，更多使用可参考demo
                    SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et), property.getIdentifier());
                } else if(property.getDataType().getType().equals(TmpConstant.TYPE_VALUE_INTEGER)) {
                    reportData.put(property.getIdentifier(), new ValueWrapper.IntValueWrapper(Integer.parseInt(et)));  // 参考示例，更多使用可参考demo
                    SaveAndUploadAliUtil.saveInt(Integer.parseInt(et), property.getIdentifier());
                }
                SaveAndUploadAliUtil.saveAndUpload(reportData);
            }
        });
        setLvXin();
    }

    private void setLvXin() {
        if (mapLx.size() > 0) {
            listLx = new ArrayList<>();
            LvXinEntity entity = null;
            if (mapLx.containsKey("FilterID_1")) {
                entity = new LvXinEntity();
                entity.no = 1;
                entity.lvxinName = "滤芯1";
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterID_1");
                if (propertyValue != null) {
                    String value = ((ValueWrapper.StringValueWrapper) propertyValue).getValue();
                    entity.lvxinDeviceName = value;
                }
                if (mapLx.containsKey("FilterLifeTimePercent_1")) {
                    ValueWrapper percent1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimePercent_1");
                    if (percent1 != null) {
                        int value = ((ValueWrapper.IntValueWrapper) percent1).getValue();
                        entity.lifePercent = value +"";
                    }
                }
                if (mapLx.containsKey("FilterLifeTimeDays_1")) {
                    ValueWrapper days_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimeDays_1");
                    if (days_1 != null) {
                        double value = ((ValueWrapper.DoubleValueWrapper) days_1).getValue();
                        entity.lifeDay = value +"";
                    }
                }
                if (mapLx.containsKey("FilterStatus_1")) {
                    ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_1");
                    if (status_1 != null) {
                        int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                        entity.lifeStatus = SaveAndUploadAliUtil.getEnumValue(mapLx.get("FilterStatus_1"), value);
                    }
                }
                listLx.add(entity);
            }
            if (mapLx.containsKey("FilterID_2")) {
                entity = new LvXinEntity();
                entity.no = 2;
                entity.lvxinName = "滤芯2";
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterID_2");
                if (propertyValue != null) {
                    String value = ((ValueWrapper.StringValueWrapper) propertyValue).getValue();
                    entity.lvxinDeviceName = value;
                }
                if (mapLx.containsKey("FilterLifeTimePercent_2")) {
                    ValueWrapper percent1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimePercent_2");
                    if (percent1 != null) {
                        int value = ((ValueWrapper.IntValueWrapper) percent1).getValue();
                        entity.lifePercent = value +"";
                    }
                }
                if (mapLx.containsKey("FilterLifeTimeDays_2")) {
                    ValueWrapper days_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimeDays_2");
                    if (days_1 != null) {
                        double value = ((ValueWrapper.DoubleValueWrapper) days_1).getValue();
                        entity.lifeDay = value +"";
                    }
                }
                if (mapLx.containsKey("FilterStatus_2")) {
                    ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_2");
                    if (status_1 != null) {
                        int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                        entity.lifeStatus = SaveAndUploadAliUtil.getEnumValue(mapLx.get("FilterStatus_2"), value);
                    }
                }
                listLx.add(entity);
            }
            if (mapLx.containsKey("FilterID_3")) {
                entity = new LvXinEntity();
                entity.no = 3;
                entity.lvxinName = "滤芯3";
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterID_3");
                if (propertyValue != null) {
                    String value = ((ValueWrapper.StringValueWrapper) propertyValue).getValue();
                    entity.lvxinDeviceName = value;
                }
                if (mapLx.containsKey("FilterLifeTimePercent_3")) {
                    ValueWrapper percent1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimePercent_3");
                    if (percent1 != null) {
                        int value = ((ValueWrapper.IntValueWrapper) percent1).getValue();
                        entity.lifePercent = value +"";
                    }
                }
                if (mapLx.containsKey("FilterLifeTimeDays_3")) {
                    ValueWrapper days_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimeDays_3");
                    if (days_1 != null) {
                        double value = ((ValueWrapper.DoubleValueWrapper) days_1).getValue();
                        entity.lifeDay = value +"";
                    }
                }
                if (mapLx.containsKey("FilterStatus_3")) {
                    ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_3");
                    if (status_1 != null) {
                        int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                        entity.lifeStatus = SaveAndUploadAliUtil.getEnumValue(mapLx.get("FilterStatus_3"), value);
                    }
                }
                listLx.add(entity);
            }
            if (mapLx.containsKey("FilterID_4")) {
                entity = new LvXinEntity();
                entity.no = 4;
                entity.lvxinName = "滤芯4";
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterID_4");
                if (propertyValue != null) {
                    String value = ((ValueWrapper.StringValueWrapper) propertyValue).getValue();
                    entity.lvxinDeviceName = value;
                }
                if (mapLx.containsKey("FilterLifeTimePercent_4")) {
                    ValueWrapper percent1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimePercent_4");
                    if (percent1 != null) {
                        int value = ((ValueWrapper.IntValueWrapper) percent1).getValue();
                        entity.lifePercent = value +"";
                    }
                }
                if (mapLx.containsKey("FilterLifeTimeDays_4")) {
                    ValueWrapper days_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimeDays_4");
                    if (days_1 != null) {
                        double value = ((ValueWrapper.DoubleValueWrapper) days_1).getValue();
                        entity.lifeDay = value +"";
                    }
                }
                if (mapLx.containsKey("FilterStatus_4")) {
                    ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_4");
                    if (status_1 != null) {
                        int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                        entity.lifeStatus = SaveAndUploadAliUtil.getEnumValue(mapLx.get("FilterStatus_4"), value);
                    }
                }
                listLx.add(entity);
            }
            if (mapLx.containsKey("FilterID_5")) {
                entity = new LvXinEntity();
                entity.no = 5;
                entity.lvxinName = "滤芯5";
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterID_5");
                if (propertyValue != null) {
                    String value = ((ValueWrapper.StringValueWrapper) propertyValue).getValue();
                    entity.lvxinDeviceName = value;
                }
                if (mapLx.containsKey("FilterLifeTimePercent_5")) {
                    ValueWrapper percent1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimePercent_5");
                    if (percent1 != null) {
                        int value = ((ValueWrapper.IntValueWrapper) percent1).getValue();
                        entity.lifePercent = value +"";
                    }
                }
                if (mapLx.containsKey("FilterLifeTimeDays_5")) {
                    ValueWrapper days_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterLifeTimeDays_5");
                    if (days_1 != null) {
                        double value = ((ValueWrapper.DoubleValueWrapper) days_1).getValue();
                        entity.lifeDay = value +"";
                    }
                }
                if (mapLx.containsKey("FilterStatus_5")) {
                    ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_5");
                    if (status_1 != null) {
                        int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                        EnumSpec filterStatus_1 = (EnumSpec) mapLx.get("FilterStatus_5").getDataType().getSpecs();
                        entity.lifeStatus = SaveAndUploadAliUtil.getEnumValue(mapLx.get("FilterStatus_5"), value);
                    }
                }
                listLx.add(entity);
            }

            if(listLx.size() > 0) {
                topViewCycle = new TopViewCycle(getActivity());
                topViewCycle.setAutoCycle(false);
                topViewCycle.loadData(listLx);
                topViewCycle.setOnPageClickListener(new TopViewCycle.OnPageClickListener() {
                    @Override
                    public void onClick(int position) {
                        LvXinEntity entity1 = listLx.get(position);
                        if(TextUtils.isEmpty(entity1.lvxinDeviceName)) {
                            dropDownPop.setFitInfo(null, entity1, mapLx);
                            dropDownPop.showPop(mRvList);
                        } else {
                            mPresenter.getPjInfo(entity1);
                        }
                    }

                    @Override
                    public void onClickButton(int position) {
                        LvXinEntity entity1 = listLx.get(position);
                        dropDownScanPop.setDeviceInfo(entity1);
                        dropDownScanPop.showPop(mRvList);
                    }
                });
                adapter.setHeadView(topViewCycle);
            }

            dropDownPop.setOnChangePjListener(new DropDownPop.OnChangePjListener() {
                @Override
                public void onChange(String lifePercent, int no, String lifeDay, String lifeStatus) {
                    for (LvXinEntity lx : listLx) {
                        if(lx.no == no) {
                            lx.lifeDay = lifeDay;
                            lx.lifePercent = lifePercent;
                            lx.lifeStatus = lifeStatus;
                            topViewCycle.setData(listLx);
                            topViewCycle.getmViewPager().getAdapter().notifyDataSetChanged();
                            break;
                        }
                    }
                }

                @Override
                public void onReset(int no) {

                }
            });

            dropDownScanPop.setOnChangePjListener(new DropDownScanPop.OnChangePjListener() {
                @Override
                public void onChange(String deviceName, int no) {
                    for (LvXinEntity lx : listLx) {
                        if(lx.no == no) {
                            lx.lvxinDeviceName = deviceName;
                            topViewCycle.setData(listLx);
                            topViewCycle.getmViewPager().getAdapter().notifyDataSetChanged();
                            break;
                        }
                    }
                }

                @Override
                public void onScan() {
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                     * 也可以不传这个参数
                     * 不传的话  默认都为默认不震动  其他都为true
                     * */

                    //ZxingConfig config = new ZxingConfig();
                    //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                    //config.setPlayBeep(true);//是否播放提示音
                    //config.setShake(true);//是否震动
                    //config.setShowAlbum(true);//是否显示相册
                    //config.setShowFlashLight(true);//是否显示闪光灯
                    //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, 11);
                }
            });
        }
    }

    @Override
    public ParamPresenter getPresenter() {
        return new ParamPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getPjInfoSuccess(FittingDetailEntity fittingDetailEntity, LvXinEntity entity) {
        dropDownPop.setFitInfo(fittingDetailEntity, entity, mapLx);
        dropDownPop.showPop(mRvList);
    }

    @Override
    public void resetSuccess(FittingResetDetailEntity entity, int no) {
        dropDownPop.setReset(no, entity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetStickyEvent(RefreshEvent message) {
        if (message != null && message.aMessage != null) {
            String data = new String((byte[]) message.aMessage.data);
            try {
                ReceiveMsg receiveMsg = new Gson().fromJson(data, ReceiveMsg.class);
                Map<String, Object> params = receiveMsg.params;
                for (int i = 0; i < paramList.size(); i++) {
                    Property property = paramList.get(i);
                    if (params.containsKey(property.getIdentifier())) {
                        adapter.setDataByPos(i, params.get(property.getIdentifier()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 11 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                dropDownScanPop.setDeviceScanName(content);
            }
        }
    }
}
