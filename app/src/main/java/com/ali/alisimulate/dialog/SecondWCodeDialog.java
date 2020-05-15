package com.ali.alisimulate.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ali.alisimulate.R;
import com.ali.alisimulate.util.DisplayUtil;
import com.ali.alisimulate.util.ZXingUtils;

/**
 * 二维码
 */
public class SecondWCodeDialog extends Dialog {

    private String mDeviceName;
    private String mDeviceDesc;

    public SecondWCodeDialog(@NonNull Context activity) {
        super(activity, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_erw);
        ImageView iv_code = findViewById(R.id.iv_code);
        Bitmap bitmap = ZXingUtils.createQRImage(mDeviceName, DisplayUtil.dip2px(getContext(), 239), DisplayUtil.dip2px(getContext(), 229));
        iv_code.setImageBitmap(bitmap);
        TextView tv_desc = findViewById(R.id.tv_desc);
        tv_desc.setText(mDeviceDesc);
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public void setDeviceDesc(String deviceDesc) {
        mDeviceDesc = deviceDesc;
    }
}
