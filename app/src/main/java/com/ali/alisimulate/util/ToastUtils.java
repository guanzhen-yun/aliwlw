package com.ali.alisimulate.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.R;
import com.aliyun.alink.linksdk.tools.ThreadTools;

public class ToastUtils {
    public static void showToast(String message) {
        showToast(message, MyApp.getAppContext());
    }

    private static void showToast(final String message, final Context context) {
        ThreadTools.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showMyToast(String str) {
        Toast toast = new Toast(MyApp.getApp());
        View view = LayoutInflater.from(MyApp.getApp()).inflate(R.layout.toast_custom, null);
        TextView tv_toast = view.findViewById(R.id.tv_toast);
        tv_toast.setText(str);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
