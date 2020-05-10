package com.ali.alisimulate.util;

import android.content.Context;
import android.widget.Toast;

import com.ali.alisimulate.MyApp;
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
}
