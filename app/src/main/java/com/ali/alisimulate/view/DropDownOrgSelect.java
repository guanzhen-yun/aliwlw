package com.ali.alisimulate.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:DropDownOrgSelect
 * 底部弹窗 三级筛选
 **/
public class DropDownOrgSelect {
    private PopupWindow mPopupWindow;//下拉选项弹窗

    private ImageView iv_back;
    private ImageView iv_close;
    private TextView tv_branch;
    private View view_branch;
    private TextView tv_branchtype;
    private View view_branchtype;
    private RelativeLayout rl_branchtype;
    private RelativeLayout rl_device;
    private TextView tv_device;
    private View view_device;
    private RecyclerView rv_device;

    private List<String> listDevice = new ArrayList<>();

    public void init(Activity activity) {
        if(activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.view_popup_selectorg, null);
            RelativeLayout rl_body = popView.findViewById(R.id.rl_body);
            iv_back = popView.findViewById(R.id.iv_back);
            iv_close = popView.findViewById(R.id.iv_close);
            tv_branch = popView.findViewById(R.id.tv_branch);
            rl_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hidePop();
                }
            });
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hidePop();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hidePop();
                }
            });

            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,//添加一个布局
                    ViewGroup.LayoutParams.MATCH_PARENT);
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
