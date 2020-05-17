package com.ali.alisimulate.util;

import android.app.Activity;
import android.content.Context;

public class DisplayUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 得到屏幕宽度
     *
     */
    public static int getScreenWight(Activity activity) {

        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }
}
