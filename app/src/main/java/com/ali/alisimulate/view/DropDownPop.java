package com.ali.alisimulate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.PopDeviceListAdapter;
import com.ali.alisimulate.entity.FittingDetailEntity;
import com.ali.alisimulate.entity.FittingResetDetailEntity;
import com.ali.alisimulate.entity.LvXinEntity;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.DataType;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.MetaSpec;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * 底部弹窗
 */
public class DropDownPop {
    private PopupWindow mPopupWindow;//下拉选项弹窗
    private FittingDetailEntity mFitingEntity;
    private LvXinEntity mEntity;
    private Map<String, Property> mMapLx;

    private TextView tv_pp_name;
    private TextView tv_xh_name;
    private TextView tv_lx_name;
    private TextView tv_dname;
    private EditText et_kyname;
    private TextView tv_tip_yj;
    private EditText et_syname;
    private TextView tv_tip_sy;
    private RelativeLayout rl_sm;
    private TextView tv_sm;
    private TextView tv_reset;

    private String mSelectStatus;
    private String mSelectStatusName;
    private MetaSpec lifeSpec;

    public void init(Activity activity) {
        if (activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.view_popup_fromdown, null);
            rl_sm = popView.findViewById(R.id.rl_sm);
            tv_sm = popView.findViewById(R.id.tv_sm);
            tv_reset = popView.findViewById(R.id.tv_reset);
            tv_pp_name = popView.findViewById(R.id.tv_pp_name);
            tv_xh_name = popView.findViewById(R.id.tv_xh_name);
            tv_lx_name = popView.findViewById(R.id.tv_lx_name);
            et_kyname = popView.findViewById(R.id.et_kyname);
            tv_tip_yj = popView.findViewById(R.id.tv_tip_yj);
            et_syname = popView.findViewById(R.id.et_syname);
            tv_dname = popView.findViewById(R.id.tv_dname);
            tv_tip_sy = popView.findViewById(R.id.tv_tip_sy);
            TextView tv_cancel = popView.findViewById(R.id.tv_cancel);
            TextView tv_ok = popView.findViewById(R.id.tv_ok);
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hidePop();
                }
            });
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //更改
                    saveChange();
                }
            });

            tv_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //复位 ---走接口 然后设置参数 上传阿里云 保存本地
                    if (onChangePjListener != null) {
                        onChangePjListener.onReset(mEntity.no);
                    }
                }
            });

            rl_sm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEntity.no == 1 && mMapLx.containsKey("FilterStatus_1")) {
                        Property filterStatus_1 = mMapLx.get("FilterStatus_1");
                        if (filterStatus_1.getDataType().getSpecs() != null) {
                            EnumSpec specs = (EnumSpec) filterStatus_1.getDataType().getSpecs();
                            Set<String> strings = specs.keySet();

                            List<String> list = new ArrayList<>();

                            List<String> listKey = new ArrayList<>();

                            for (String string : strings) {
                                list.add(specs.get(string));
                                listKey.add(string);
                            }
                            LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                    R.layout.pop_device, null, true);
                            PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            pw.setOutsideTouchable(true);
                            pw.showAsDropDown(rl_sm);
                            RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                            rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));

                            PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                            rv_device.setAdapter(adapter);
                            adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                                @Override
                                public void onCheck(int pos) {
                                    tv_sm.setText(listKey.get(pos) + "(" + list.get(pos) + ")");
                                    mSelectStatus = listKey.get(pos);
                                    mSelectStatusName = list.get(pos);
                                    pw.dismiss();
                                }
                            });
                        }
                    } else if (mEntity.no == 2 && mMapLx.containsKey("FilterStatus_2")) {
                        Property filterStatus_1 = mMapLx.get("FilterStatus_2");
                        if (filterStatus_1.getDataType().getSpecs() != null) {
                            EnumSpec specs = (EnumSpec) filterStatus_1.getDataType().getSpecs();
                            Set<String> strings = specs.keySet();

                            List<String> list = new ArrayList<>();

                            List<String> listKey = new ArrayList<>();

                            for (String string : strings) {
                                list.add(specs.get(string));
                                listKey.add(string);
                            }
                            LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                    R.layout.pop_device, null, true);
                            PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            pw.setOutsideTouchable(true);
                            pw.showAsDropDown(rl_sm);
                            RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                            rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));

                            PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                            rv_device.setAdapter(adapter);
                            adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                                @Override
                                public void onCheck(int pos) {
                                    tv_sm.setText(listKey.get(pos) + "(" + list.get(pos) + ")");
                                    mSelectStatus = listKey.get(pos);
                                    mSelectStatusName = list.get(pos);
                                    pw.dismiss();
                                }
                            });
                        }
                    } else if (mEntity.no == 3 && mMapLx.containsKey("FilterStatus_3")) {
                        Property filterStatus_1 = mMapLx.get("FilterStatus_3");
                        if (filterStatus_1.getDataType().getSpecs() != null) {
                            EnumSpec specs = (EnumSpec) filterStatus_1.getDataType().getSpecs();
                            Set<String> strings = specs.keySet();

                            List<String> list = new ArrayList<>();

                            List<String> listKey = new ArrayList<>();

                            for (String string : strings) {
                                list.add(specs.get(string));
                                listKey.add(string);
                            }
                            LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                    R.layout.pop_device, null, true);
                            PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            pw.setOutsideTouchable(true);
                            pw.showAsDropDown(rl_sm);
                            RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                            rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));

                            PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                            rv_device.setAdapter(adapter);
                            adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                                @Override
                                public void onCheck(int pos) {
                                    tv_sm.setText(listKey.get(pos) + "(" + list.get(pos) + ")");
                                    mSelectStatus = listKey.get(pos);
                                    mSelectStatusName = list.get(pos);
                                    pw.dismiss();
                                }
                            });
                        }
                    } else if (mEntity.no == 4 && mMapLx.containsKey("FilterStatus_4")) {
                        Property filterStatus_1 = mMapLx.get("FilterStatus_4");
                        if (filterStatus_1.getDataType().getSpecs() != null) {
                            EnumSpec specs = (EnumSpec) filterStatus_1.getDataType().getSpecs();
                            Set<String> strings = specs.keySet();

                            List<String> list = new ArrayList<>();

                            List<String> listKey = new ArrayList<>();

                            for (String string : strings) {
                                list.add(specs.get(string));
                                listKey.add(string);
                            }
                            LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                    R.layout.pop_device, null, true);
                            PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            pw.setOutsideTouchable(true);
                            pw.showAsDropDown(rl_sm);
                            RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                            rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));

                            PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                            rv_device.setAdapter(adapter);
                            adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                                @Override
                                public void onCheck(int pos) {
                                    tv_sm.setText(listKey.get(pos) + "(" + list.get(pos) + ")");
                                    mSelectStatus = listKey.get(pos);
                                    mSelectStatusName = list.get(pos);
                                    pw.dismiss();
                                }
                            });
                        }
                    } else if (mEntity.no == 5 && mMapLx.containsKey("FilterStatus_5")) {
                        Property filterStatus_1 = mMapLx.get("FilterStatus_5");
                        if (filterStatus_1.getDataType().getSpecs() != null) {
                            EnumSpec specs = (EnumSpec) filterStatus_1.getDataType().getSpecs();
                            Set<String> strings = specs.keySet();

                            List<String> list = new ArrayList<>();

                            List<String> listKey = new ArrayList<>();

                            for (String string : strings) {
                                list.add(specs.get(string));
                                listKey.add(string);
                            }
                            LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                    R.layout.pop_device, null, true);
                            PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            pw.setOutsideTouchable(true);
                            pw.showAsDropDown(rl_sm);
                            RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                            rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));

                            PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                            rv_device.setAdapter(adapter);
                            adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                                @Override
                                public void onCheck(int pos) {
                                    tv_sm.setText(listKey.get(pos) + "(" + list.get(pos) + ")");
                                    mSelectStatus = listKey.get(pos);
                                    mSelectStatusName = list.get(pos);
                                    pw.dismiss();
                                }
                            });
                        }
                    }
                }
            });
            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,//添加一个布局
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setFocusable(true);//获取焦点
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            mPopupWindow.setAnimationStyle(R.style.pop_fromdown_anim);

            et_kyname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    DataType dataType = mMapLx.get("FilterLifeTimeDays_" + mEntity.no).getDataType();
                    if (dataType.getSpecs() != null) {
                        MetaSpec specs = (MetaSpec) dataType.getSpecs();
                        String et = et_kyname.getText().toString();
                        if (!TextUtils.isEmpty(et) && (Double.parseDouble(et) > Double.parseDouble(specs.getMax()) || Double.parseDouble(et) < Double.parseDouble(specs.getMin()))) {
                            tv_tip_yj.setTextColor(Color.parseColor("#ff0000"));
                        } else {
                            tv_tip_yj.setTextColor(Color.parseColor("#ADADAD"));
                        }
                    }
                }
            });

            et_syname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    DataType dataType = mMapLx.get("FilterLifeTimePercent_" + mEntity.no).getDataType();
                    if (dataType.getSpecs() != null) {
                        MetaSpec specs = (MetaSpec) dataType.getSpecs();
                        String et = et_syname.getText().toString();
                        if (!TextUtils.isEmpty(et) && (Double.parseDouble(et) > Double.parseDouble(specs.getMax()) || Double.parseDouble(et) < Double.parseDouble(specs.getMin()))) {
                            tv_tip_sy.setTextColor(Color.parseColor("#ff0000"));
                        } else {
                            tv_tip_sy.setTextColor(Color.parseColor("#ADADAD"));
                        }
                    }
                }
            });
        }
    }

    private void saveChange() {
        // 设备上报
        Map<String, ValueWrapper> reportData = new HashMap<>();
        // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
        if (mEntity.no == 1) {
            if (!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_1", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(mSelectStatus), "FilterStatus_1");
            }
            if (!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_1", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
                SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et_kyname.getText().toString()), "FilterLifeTimeDays_1");
            }
            if (!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_1", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
                SaveAndUploadAliUtil.saveInt(Integer.parseInt(et_syname.getText().toString()), "FilterLifeTimePercent_1");
            }
        } else if (mEntity.no == 2) {
            if (!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_2", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(mSelectStatus), "FilterStatus_2");
            }
            if (!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_2", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
                SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et_kyname.getText().toString()), "FilterLifeTimeDays_2");
            }
            if (!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_2", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
                SaveAndUploadAliUtil.saveInt(Integer.parseInt(et_syname.getText().toString()), "FilterLifeTimePercent_2");
            }
        } else if (mEntity.no == 3) {
            if (!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_3", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(mSelectStatus), "FilterStatus_3");
            }
            if (!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_3", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
                SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et_kyname.getText().toString()), "FilterLifeTimeDays_3");
            }
            if (!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_3", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
                SaveAndUploadAliUtil.saveInt(Integer.parseInt(et_syname.getText().toString()), "FilterLifeTimePercent_3");
            }
        } else if (mEntity.no == 4) {
            if (!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_4", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(mSelectStatus), "FilterStatus_4");
            }
            if (!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_4", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
                SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et_kyname.getText().toString()), "FilterLifeTimeDays_4");
            }
            if (!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_4", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
                SaveAndUploadAliUtil.saveInt(Integer.parseInt(et_syname.getText().toString()), "FilterLifeTimePercent_4");
            }
        } else if (mEntity.no == 5) {
            if (!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_5", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
                SaveAndUploadAliUtil.saveEnum(Integer.parseInt(mSelectStatus), "FilterStatus_5");
            }
            if (!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_5", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
                SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et_kyname.getText().toString()), "FilterLifeTimeDays_5");
            }
            if (!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_5", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
                SaveAndUploadAliUtil.saveInt(Integer.parseInt(et_syname.getText().toString()), "FilterLifeTimePercent_5");
            }
        }

        SaveAndUploadAliUtil.saveAndUpload(reportData, new SaveAndUploadAliUtil.OnUploadSuccessListener() {
            @Override
            public void onUnloadSuccess() {
                if (onChangePjListener != null) {
                    onChangePjListener.onChange(et_syname.getText().toString(), mEntity.no, et_kyname.getText().toString(), mSelectStatusName);
                }
                hidePop();
            }
        });
    }

    /**
     * 显示pop
     */

    public void showPop(View view) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 隐藏pop
     */

    public void hidePop() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public void setFitInfo(FittingDetailEntity fittingDetailEntity, LvXinEntity entity, Map<String, Property> mapLx) {
        mFitingEntity = fittingDetailEntity;
        mEntity = entity;
        mMapLx = mapLx;
        if (mFitingEntity != null && !TextUtils.isEmpty(mFitingEntity.productCompany)) {
            tv_pp_name.setText(mFitingEntity.productCompany);
        } else {
            tv_pp_name.setText("-");
        }
        if (mFitingEntity != null && !TextUtils.isEmpty(mFitingEntity.deviceTypeName)) {
            tv_xh_name.setText(mFitingEntity.deviceTypeName);
        } else {
            tv_xh_name.setText("-");
        }
        if (mFitingEntity != null && !TextUtils.isEmpty(mFitingEntity.subType)) {
            tv_lx_name.setText(mFitingEntity.subType);
        } else {
            tv_lx_name.setText("-");
        }

        if (!TextUtils.isEmpty(entity.lvxinDeviceName)) {
            tv_dname.setText(entity.lvxinDeviceName);
            tv_reset.setEnabled(true);
            tv_reset.setTextColor(Color.parseColor("#585858"));
        } else {
            tv_dname.setText("-");
            tv_reset.setTextColor(Color.parseColor("#999999"));
            tv_reset.setEnabled(false);
        }
        if (!TextUtils.isEmpty(entity.lifeDay)) {
            et_kyname.setText(entity.lifeDay);
        } else {
            et_kyname.setText("");
        }
        if (!TextUtils.isEmpty(entity.lifePercent)) {
            et_syname.setText(entity.lifePercent);
        } else {
            et_syname.setText("");
        }
        if (entity.no == 1) {
            if (TextUtils.isEmpty(entity.lifeStatus)) {
                tv_sm.setText("");
            } else {
                ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_1");
                if (status_1 != null) {
                    int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                    tv_sm.setText(value + "(" + entity.lifeStatus + ")");
                } else {
                    tv_sm.setText("");
                }
            }
            if (mapLx.containsKey("FilterLifeTimeDays_1") && mapLx.get("FilterLifeTimeDays_1").getDataType().getSpecs() != null) {
                lifeSpec = (MetaSpec) mapLx.get("FilterLifeTimeDays_1").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + lifeSpec.getMin() + "～" + lifeSpec.getMax() + "，步长" + lifeSpec.getStep());
            }
            if (mapLx.containsKey("FilterLifeTimePercent_1") && mapLx.get("FilterLifeTimePercent_1").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_1").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin() + "～" + specs.getMax() + "，步长" + specs.getStep());
            }
        } else if (entity.no == 2) {
            if (TextUtils.isEmpty(entity.lifeStatus)) {
                tv_sm.setText("");
            } else {
                ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_2");
                if (status_1 != null) {
                    int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                    tv_sm.setText(value + "(" + entity.lifeStatus + ")");
                } else {
                    tv_sm.setText("");
                }
            }
            if (mapLx.containsKey("FilterLifeTimeDays_2") && mapLx.get("FilterLifeTimeDays_2").getDataType().getSpecs() != null) {
                lifeSpec = (MetaSpec) mapLx.get("FilterLifeTimeDays_2").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + lifeSpec.getMin() + "～" + lifeSpec.getMax() + "，步长" + lifeSpec.getStep());
            }
            if (mapLx.containsKey("FilterLifeTimePercent_2") && mapLx.get("FilterLifeTimePercent_2").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_2").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin() + "～" + specs.getMax() + "，步长" + specs.getStep());
            }
        } else if (entity.no == 3) {
            if (TextUtils.isEmpty(entity.lifeStatus)) {
                tv_sm.setText("");
            } else {
                ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_3");
                if (status_1 != null) {
                    int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                    tv_sm.setText(value + "(" + entity.lifeStatus + ")");
                } else {
                    tv_sm.setText("");
                }
            }
            if (mapLx.containsKey("FilterLifeTimeDays_3") && mapLx.get("FilterLifeTimeDays_3").getDataType().getSpecs() != null) {
                lifeSpec = (MetaSpec) mapLx.get("FilterLifeTimeDays_3").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + lifeSpec.getMin() + "～" + lifeSpec.getMax() + "，步长" + lifeSpec.getStep());
            }
            if (mapLx.containsKey("FilterLifeTimePercent_3") && mapLx.get("FilterLifeTimePercent_3").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_3").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin() + "～" + specs.getMax() + "，步长" + specs.getStep());
            }
        } else if (entity.no == 4) {
            if (TextUtils.isEmpty(entity.lifeStatus)) {
                tv_sm.setText("");
            } else {
                ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_4");
                if (status_1 != null) {
                    int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                    tv_sm.setText(value + "(" + entity.lifeStatus + ")");
                } else {
                    tv_sm.setText("");
                }
            }
            if (mapLx.containsKey("FilterLifeTimeDays_4") && mapLx.get("FilterLifeTimeDays_4").getDataType().getSpecs() != null) {
                lifeSpec = (MetaSpec) mapLx.get("FilterLifeTimeDays_4").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + lifeSpec.getMin() + "～" + lifeSpec.getMax() + "，步长" + lifeSpec.getStep());
            }
            if (mapLx.containsKey("FilterLifeTimePercent_4") && mapLx.get("FilterLifeTimePercent_4").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_4").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin() + "～" + specs.getMax() + "，步长" + specs.getStep());
            }
        } else if (entity.no == 5) {
            if (TextUtils.isEmpty(entity.lifeStatus)) {
                tv_sm.setText("");
            } else {
                ValueWrapper status_1 = LinkKit.getInstance().getDeviceThing().getPropertyValue("FilterStatus_5");
                if (status_1 != null) {
                    int value = ((ValueWrapper.EnumValueWrapper) status_1).getValue();
                    tv_sm.setText(value + "(" + entity.lifeStatus + ")");
                } else {
                    tv_sm.setText("");
                }
            }
            if (mapLx.containsKey("FilterLifeTimeDays_5") && mapLx.get("FilterLifeTimeDays_5").getDataType().getSpecs() != null) {
                lifeSpec = (MetaSpec) mapLx.get("FilterLifeTimeDays_5").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + lifeSpec.getMin() + "～" + lifeSpec.getMax() + "，步长" + lifeSpec.getStep());
            }
            if (mapLx.containsKey("FilterLifeTimePercent_5") && mapLx.get("FilterLifeTimePercent_5").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_5").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin() + "～" + specs.getMax() + "，步长" + specs.getStep());
            }
        }
    }

    public void setOnChangePjListener(OnChangePjListener onChangePjListener) {
        this.onChangePjListener = onChangePjListener;
    }

    public void setReset(int no, FittingResetDetailEntity entity) {
        et_syname.setText(entity.percent);
        et_syname.setText(entity.days);
        String enumValue = SaveAndUploadAliUtil.getEnumValue(mMapLx.get("FilterStatus_" + no), Integer.parseInt(entity.status));
        tv_sm.setText(enumValue);
        Map<String, ValueWrapper> reportData = new HashMap<>();
        if (!TextUtils.isEmpty(mSelectStatus)) {
            reportData.put("FilterStatus_" + no, new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
            SaveAndUploadAliUtil.saveEnum(Integer.parseInt(mSelectStatus), "FilterStatus_" + no);
        }
        if (!TextUtils.isEmpty(et_kyname.getText().toString())) {
            reportData.put("FilterLifeTimeDays_" + no, new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
            SaveAndUploadAliUtil.saveDouble(Double.parseDouble(et_kyname.getText().toString()), "FilterLifeTimeDays_" + no);
        }
        if (!TextUtils.isEmpty(et_syname.getText().toString())) {
            reportData.put("FilterLifeTimePercent_" + no, new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
            SaveAndUploadAliUtil.saveInt(Integer.parseInt(et_syname.getText().toString()), "FilterLifeTimePercent_" + no);
        }
        SaveAndUploadAliUtil.saveAndUpload(reportData);
    }

    public interface OnChangePjListener {
        void onChange(String lifePercent, int no, String lifeDay, String lifeStatus);

        void onReset(int no);
    }

    private OnChangePjListener onChangePjListener;

}
