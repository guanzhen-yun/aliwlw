package com.ali.alisimulate.util;

import android.content.Context;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.entity.LoginSuccess;
import com.google.gson.Gson;

/**
 * Author:关震
 * Date:2020/5/12 11:37
 * Description:UserUtils
 **/
public class UserUtils {
    public static String getUserId(Context context) {
        String strLoginInfo = SharedPreferencesUtils.getStr(context, Constants.KEY_LOGIN_INFO);
        LoginSuccess loginInfo = new Gson().fromJson(strLoginInfo, LoginSuccess.class);
        if(loginInfo != null) {
            return loginInfo.userDetail.id;
        }
        return "";
    }

    public static String getToken(Context context) {
        String strLoginInfo = SharedPreferencesUtils.getStr(context, Constants.KEY_LOGIN_INFO);
        LoginSuccess loginInfo = new Gson().fromJson(strLoginInfo, LoginSuccess.class);
        if(loginInfo != null) {
            return loginInfo.token;
        }
        return "";
    }
}
