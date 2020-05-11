package com.ali.alisimulate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ali.alisimulate.R;

import java.util.List;

/**
 * 底部弹窗
 */
public class DropDownPop {
    private PopupWindow mPopupWindow;//下拉选项弹窗

    public void init(Activity activity) {
        if(activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.view_popup_fromdown, null);
            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,//添加一个布局
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setFocusable(true);//获取焦点
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            mPopupWindow.setAnimationStyle(R.style.pop_fromdown_anim);
        }
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
}
