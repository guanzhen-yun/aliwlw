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
    private TextView mTvPickerTitle;//弹窗标题
    private WheelPicker mMainWheel;//下拉筛选

    private OnConfirmPopListener mOnConfirmPopListener;//选中某个选项接口
    private int mSelectPosition = 0;//下拉选选中的位置

    public void init(Activity activity) {
        if(activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.hire_view_popup_fromdown, null);
            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,//添加一个布局
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setFocusable(true);//获取焦点
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
            mMainWheel = popView.findViewById(R.id.main_wheel);
            mTvPickerTitle = popView.findViewById(R.id.picker_title);
            TextView tvConfirm = popView.findViewById(R.id.tv_confirm);
            TextView tvCancel = popView.findViewById(R.id.tv_cancel);
            tvCancel.setOnClickListener(v -> mPopupWindow.dismiss());
            tvConfirm.setOnClickListener(v -> {
                if(mOnConfirmPopListener != null) {
                    mOnConfirmPopListener.onConfirm(mSelectPosition);
                    mSelectPosition = 0;
                }
            });
            mMainWheel.setOnItemSelectedListener((picker, data, position) -> mSelectPosition = position);
            mPopupWindow.setAnimationStyle(R.style.pop_fromdown_anim);
        }
    }

    /**
     * 设置选项选中监听
     */

    public void setOnConfirmPopListener(OnConfirmPopListener onConfirmPopListener) {
        this.mOnConfirmPopListener = onConfirmPopListener;
    }

    /**
     * 设置标题
     */

    public void setPopTitle(String popTitle) {
        if(mTvPickerTitle != null && !TextUtils.isEmpty(popTitle)) {
            mTvPickerTitle.setText(popTitle);
        }
    }

    /**
     * 设置下拉选项数据
     */

    public void setPopList(List<String> popList) {
        if(mMainWheel != null) {
            mMainWheel.setData(popList);
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

    /**
     * 选中某个选项接口
     */
    public interface OnConfirmPopListener {
        void onConfirm(int selectPosition);
    }
}
