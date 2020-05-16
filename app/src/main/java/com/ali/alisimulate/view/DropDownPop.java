package com.ali.alisimulate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
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
import com.ali.alisimulate.entity.LvXinEntity;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
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
    private TextView tv_cancel;
    private TextView tv_ok;
    private RelativeLayout rl_sm;
    private TextView tv_sm;

    private String mSelectStatus;
    private String mSelectStatusName;

    public void init(Activity activity) {
        if(activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.view_popup_fromdown, null);
            RelativeLayout rl_body = popView.findViewById(R.id.rl_body);
            rl_sm = popView.findViewById(R.id.rl_sm);
            tv_sm = popView.findViewById(R.id.tv_sm);
            tv_pp_name = popView.findViewById(R.id.tv_pp_name);
            tv_xh_name = popView.findViewById(R.id.tv_xh_name);
            tv_lx_name = popView.findViewById(R.id.tv_lx_name);
            et_kyname = popView.findViewById(R.id.et_kyname);
            tv_tip_yj = popView.findViewById(R.id.tv_tip_yj);
            et_syname = popView.findViewById(R.id.et_syname);
            tv_dname = popView.findViewById(R.id.tv_dname);
            tv_tip_sy = popView.findViewById(R.id.tv_tip_sy);
            tv_cancel = popView.findViewById(R.id.tv_cancel);
            tv_ok = popView.findViewById(R.id.tv_ok);
            rl_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hidePop();
                }
            });
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
            rl_sm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mEntity.no == 1 && mMapLx.containsKey("FilterStatus_1")) {
                        Property filterStatus_1 = mMapLx.get("FilterStatus_1");
                        if(filterStatus_1.getDataType().getSpecs() != null) {
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
        }
    }

    private void saveChange() {
        // 设备上报
        Map<String, ValueWrapper> reportData = new HashMap<>();
        // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
        if(mEntity.no == 1) {
            if(!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_1", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
            }
            if(!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_1", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
            }
            if(!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_1", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
            }
        } else if(mEntity.no == 2) {
            if(!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_2", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
            }
            if(!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_2", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
            }
            if(!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_2", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
            }
        }else if(mEntity.no == 3) {
            if(!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_3", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
            }
            if(!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_3", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
            }
            if(!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_3", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
            }
        }else if(mEntity.no == 4) {
            if(!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_4", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
            }
            if(!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_4", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
            }
            if(!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_4", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
            }
        }else if(mEntity.no == 5) {
            if(!TextUtils.isEmpty(mSelectStatus)) {
                reportData.put("FilterStatus_5", new ValueWrapper.EnumValueWrapper(Integer.parseInt(mSelectStatus)));  // 参考示例，更多使用可参考demo
            }
            if(!TextUtils.isEmpty(et_kyname.getText().toString())) {
                reportData.put("FilterLifeTimeDays_5", new ValueWrapper.DoubleValueWrapper(Double.parseDouble(et_kyname.getText().toString())));
            }
            if(!TextUtils.isEmpty(et_syname.getText().toString())) {
                reportData.put("FilterLifeTimePercent_5", new ValueWrapper.IntValueWrapper(Integer.parseInt(et_syname.getText().toString())));
            }
        }

        if(onChangePjListener != null) {
            onChangePjListener.onChange(et_syname.getText().toString(), mEntity.no, et_kyname.getText().toString(), mSelectStatusName);
        }

        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
            @Override
            public void onSuccess(String resID, Object o) {
                // 属性上报成功 resID 设备属性对应的唯一标识
                Log.e("ProductActivity", "属性上报成功");
                hidePop();
            }

            @Override
            public void onError(String resId, AError aError) {
                // 属性上报失败
                Log.e("ProductActivity", "属性上报失败");
                hidePop();
            }
        });
    }

    /**
     * 显示pop
     */

    public void showPop(View view) {
        if(mPopupWindow != null) {
            mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    }

    /**
     * 隐藏pop
     */

    public void hidePop() {
        if(mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public void setFitInfo(FittingDetailEntity fittingDetailEntity, LvXinEntity entity, Map<String, Property> mapLx) {
        mFitingEntity = fittingDetailEntity;
        mEntity = entity;
        mMapLx = mapLx;
        if(mFitingEntity != null && !TextUtils.isEmpty(mFitingEntity.productCompany)) {
            tv_pp_name.setText(mFitingEntity.productCompany);
        } else {
            tv_pp_name.setText("-");
        }
        if(mFitingEntity != null && !TextUtils.isEmpty(mFitingEntity.deviceTypeName)) {
            tv_xh_name.setText(mFitingEntity.deviceTypeName);
        } else {
            tv_xh_name.setText("-");
        }
        if(mFitingEntity != null && !TextUtils.isEmpty(mFitingEntity.rversion)) {//TODO 类型
            tv_lx_name.setText(mFitingEntity.rversion);
        } else {
            tv_lx_name.setText("-");
        }

        if(!TextUtils.isEmpty(entity.lvxinDeviceName)) {
            tv_dname.setText(entity.lvxinDeviceName);
        } else {
            tv_dname.setText("-");
        }
        if(!TextUtils.isEmpty(entity.lifeDay)) {
            et_kyname.setText(entity.lifeDay);
        } else {
            et_kyname.setText("");
        }
        if(!TextUtils.isEmpty(entity.lifePercent)) {
            et_syname.setText(entity.lifePercent);
        } else {
            et_syname.setText("");
        }
        if(entity.no == 1) {
            if(TextUtils.isEmpty(entity.lifeStatus)) {
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
            if(mapLx.containsKey("FilterLifeTimeDays_1") && mapLx.get("FilterLifeTimeDays_1").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimeDays_1").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
            if(mapLx.containsKey("FilterLifeTimePercent_1") && mapLx.get("FilterLifeTimePercent_1").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_1").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
        } else if(entity.no == 2) {
            if(TextUtils.isEmpty(entity.lifeStatus)) {
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
            if(mapLx.containsKey("FilterLifeTimeDays_2") && mapLx.get("FilterLifeTimeDays_2").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimeDays_2").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
            if(mapLx.containsKey("FilterLifeTimePercent_2") && mapLx.get("FilterLifeTimePercent_2").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_2").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
        } else if(entity.no == 3) {
            if(TextUtils.isEmpty(entity.lifeStatus)) {
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
            if(mapLx.containsKey("FilterLifeTimeDays_3") && mapLx.get("FilterLifeTimeDays_3").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimeDays_3").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
            if(mapLx.containsKey("FilterLifeTimePercent_3") && mapLx.get("FilterLifeTimePercent_3").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_3").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
        }else if(entity.no == 4) {
            if(TextUtils.isEmpty(entity.lifeStatus)) {
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
            if(mapLx.containsKey("FilterLifeTimeDays_4") && mapLx.get("FilterLifeTimeDays_4").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimeDays_4").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
            if(mapLx.containsKey("FilterLifeTimePercent_4") && mapLx.get("FilterLifeTimePercent_4").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_4").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
        }else if(entity.no == 5) {
            if(TextUtils.isEmpty(entity.lifeStatus)) {
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
            if(mapLx.containsKey("FilterLifeTimeDays_5") && mapLx.get("FilterLifeTimeDays_5").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimeDays_5").getDataType().getSpecs();
                tv_tip_yj.setText("double型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
            if(mapLx.containsKey("FilterLifeTimePercent_5") && mapLx.get("FilterLifeTimePercent_5").getDataType().getSpecs() != null) {
                MetaSpec specs = (MetaSpec) mapLx.get("FilterLifeTimePercent_5").getDataType().getSpecs();
                tv_tip_sy.setText("int32型，范围：" + specs.getMin()+ "～" +specs.getMax() +"，步长" + specs.getStep());
            }
        }
    }

    public void setOnChangePjListener(OnChangePjListener onChangePjListener) {
        this.onChangePjListener = onChangePjListener;
    }

    public interface OnChangePjListener {
        void onChange(String lifePercent, int no, String lifeDay, String lifeStatus);
    }

    private OnChangePjListener onChangePjListener;

}
