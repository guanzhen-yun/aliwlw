package com.ali.alisimulate.config;

import android.text.TextUtils;
import android.util.Log;

import com.ali.alisimulate.BuildConfig;


/**
 * 日志工具类，上线取消打印log
 */

public class LogUtils {
    public static final String TAG = "LogUtils";
    private static boolean debug = BuildConfig.DEBUG;

    public static void d(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (debug) {
            Log.d(TAG, message);
        }
    }

    public static void d(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (debug) {
            Log.d(tag, message);
        }
    }

    public static void i(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (debug) {
            Log.i(TAG, message);
        }
    }

    public static void i(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (debug) {
            Log.i(tag, message);
        }
    }

    public static void e(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (debug) {
            Log.e(TAG, message);
        }
    }

    public static void e(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (debug) {
            Log.e(tag, message);
        }
    }
}
