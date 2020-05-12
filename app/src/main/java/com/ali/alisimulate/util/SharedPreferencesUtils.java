package com.ali.alisimulate.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ali.alisimulate.Constants;

/**
 * Author:关震
 * Date:2020/5/12 10:17
 * Description:SharedPreferencesUtils
 **/
public class SharedPreferencesUtils {
    public static void save(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getStr(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
