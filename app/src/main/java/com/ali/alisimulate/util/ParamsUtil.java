package com.ali.alisimulate.util;

import android.content.Context;

import com.ali.alisimulate.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Author:关震
 * Date:2020/5/17 8:37
 * Description:ParamsUtil
 **/
public class ParamsUtil {
    public static ArrayList<String> getControlParams(String deviceTitle) {
        ArrayList<String> list_deviceIndenty = new ArrayList<>();
        list_deviceIndenty.add("PowerSwitch");//电源开关 bool 0-关闭 1-开启
        if ("空气净化器".equals(deviceTitle)) {
            list_deviceIndenty.add("WindSpeed");//风速  enum 0-自动 1-静音档 2-低档 3-中档 4-高档 5-最高档
            list_deviceIndenty.add("WorkMode");//自动模式 enum 0-自动 1-手动
            list_deviceIndenty.add("ChildLockSwitch");//童锁开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("Humidified");//加湿开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("IonsSwitch");//离子团开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("LocalTimer");//本地定时  array
            list_deviceIndenty.add("SleepMode");//睡眠模式  bool 0-关闭  1-打开
        } else if ("净水器".equals(deviceTitle)) {
            list_deviceIndenty.add("WashingPercent");//冲洗进度  int 0~100
            list_deviceIndenty.add("WashingState");//冲洗状态 enum 0-正常  1-冲洗中
            list_deviceIndenty.add("WashingSwitch");//冲洗开关 bool 0-关闭  1-打开
            list_deviceIndenty.add("ChildLockSwitch");//童锁开关  bool 0-关闭  1-打开
            list_deviceIndenty.add("PurePercent");//制水进度 int 0~100
            list_deviceIndenty.add("PureState");//制水状态 enum 0待机 1制水中
            list_deviceIndenty.add("PureSwitch");//制水开关 bool 0-关闭  1-打开
            list_deviceIndenty.add("LocalTimer");//本地定时  array
        }
        return list_deviceIndenty;
    }

    private static String getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String w = "";
        switch (week) {
            case 0:
                w = "周日";
                break;
            case 1:
                w = "周一";
                break;
            case 2:
                w = "周二";
                break;
            case 3:
                w = "周三";
                break;
            case 4:
                w = "周四";
                break;
            case 5:
                w = "周五";
                break;
            case 6:
                w = "周六";
                break;
        }
        return w;
    }

    private static String getDateStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = formatter.format(new Date());
        String year = time.split("-")[0];
        String month = time.split("-")[1];
        String day = time.split("-")[2].split(" ")[0];
        return year + "-" + month + "-" + day;
    }

    public static long getOpenOrCloseTime(Context context, boolean isOpen) {
        long time = 0;

        String weeks = SharedPreferencesUtils.getStr(context, isOpen ? Constants.KEY_OPEN_WEEK: Constants.KEY_CLOSE_WEEK);
        if (weeks.contains(getCurrentWeek())) {
            String str = SharedPreferencesUtils.getStr(context,isOpen ?  Constants.KEY_OPEN_TIME : Constants.KEY_CLOSE_TIME);
            String hour = str.split(",")[0];
            String minute = str.split(",")[1];

            String openTime = getDateStr() + " " + hour + ":" + minute + ":" + 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            Date date = null;
            try {
                date = sdf.parse(openTime);
                if(date != null) {
                    time = date.getTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
    }
}
