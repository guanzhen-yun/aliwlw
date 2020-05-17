package com.ali.alisimulate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.LvXinEntity;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import java.util.HashMap;
import java.util.Map;

/**
 * 底部弹窗
 */
public class DropDownScanPop {
    private PopupWindow mPopupWindow;//下拉选项弹窗
    private LvXinEntity mEntity;
    private TextView tv_devicename;

    public void init(Activity activity) {
        if(activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.view_popup_scandown, null);
            TextView tv_cancel = popView.findViewById(R.id.tv_cancel);
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hidePop();
                }
            });
            TextView tv_ok = popView.findViewById(R.id.tv_ok);
            tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveChange();
                }
            });
            tv_devicename = popView.findViewById(R.id.tv_devicename);
            ImageView iv_scan = popView.findViewById(R.id.iv_scan);
            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,//添加一个布局
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setFocusable(true);//获取焦点
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            mPopupWindow.setAnimationStyle(R.style.pop_fromdown_anim);

            iv_scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onChangePjListener != null) {
                        onChangePjListener.onScan();
                    }
                }
            });
        }
    }

    private void saveChange() {
        Map<String, ValueWrapper> reportData = new HashMap<>();
        // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
        if(mEntity.no == 1) {
            if(!TextUtils.isEmpty(tv_devicename.getText().toString())) {
                reportData.put("FilterID_1", new ValueWrapper.StringValueWrapper(tv_devicename.getText().toString()));
            }
        } else if(mEntity.no == 2) {
            if(!TextUtils.isEmpty(tv_devicename.getText().toString())) {
                reportData.put("FilterID_2", new ValueWrapper.StringValueWrapper(tv_devicename.getText().toString()));
            }
        }else if(mEntity.no == 3) {
            if(!TextUtils.isEmpty(tv_devicename.getText().toString())) {
                reportData.put("FilterID_3", new ValueWrapper.StringValueWrapper(tv_devicename.getText().toString()));
            }
        }else if(mEntity.no == 4) {
            if(!TextUtils.isEmpty(tv_devicename.getText().toString())) {
                reportData.put("FilterID_4", new ValueWrapper.StringValueWrapper(tv_devicename.getText().toString()));
            }
        }else if(mEntity.no == 5) {
            if(!TextUtils.isEmpty(tv_devicename.getText().toString())) {
                reportData.put("FilterID_5", new ValueWrapper.StringValueWrapper(tv_devicename.getText().toString()));
            }
        }

        if(onChangePjListener != null) {
            onChangePjListener.onChange(tv_devicename.getText().toString(), mEntity.no);
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

    public void setDeviceInfo(LvXinEntity entity1) {
        mEntity = entity1;
    }

    public void setOnChangePjListener(OnChangePjListener onChangePjListener) {
        this.onChangePjListener = onChangePjListener;
    }

    public void setDeviceScanName(String content) {
        tv_devicename.setText(content);
    }

    public interface OnChangePjListener {
        void onChange(String deviceName, int no);
        void onScan();
    }

    private OnChangePjListener onChangePjListener;
}
