package com.ali.alisimulate.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;

/**
 * Author:关震
 * Date:2020/5/12 10:17
 * Description:SharedPreferencesUtils
 **/
public class SharedPreferencesUtils {
    public static void save(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if(isNeedAddUid(key)) {
            edit.putString(key + MyApp.getApp().deviceName + UserUtils.getUserId(context), value);
        } else {
            edit.putString(key, value);
        }
        edit.apply();
    }

    public static String getStr(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        if(isNeedAddUid(key)) {
            return sharedPreferences.getString(key+ MyApp.getApp().deviceName + UserUtils.getUserId(context), "");
        } else {
            return sharedPreferences.getString(key, "");
        }
    }

    public static boolean isNeedAddUid(String key) {
        if(key.equals(Constants.KEY_OPEN_TIME) || key.equals(Constants.KEY_OPEN_WEEK)|| key.equals(Constants.KEY_CLOSE_TIME)|| key.equals(Constants.KEY_CLOSE_WEEK)|| key.equals(Constants.KEY_LOCAN_ALI)) {
            return true;
        } else {
            return false;
        }
    }
}
