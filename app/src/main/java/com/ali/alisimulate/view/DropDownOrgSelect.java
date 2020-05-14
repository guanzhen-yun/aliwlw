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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.DropSelectOrgAdapter;
import com.ali.alisimulate.entity.SelectOrgEntity;

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
    private RelativeLayout rl_branch;
    private TextView tv_branch;
    private View view_branch;
    private TextView tv_branchtype;
    private View view_branchtype;
    private RelativeLayout rl_branchtype;
    private RelativeLayout rl_device;
    private TextView tv_device;
    private View view_device;
    private RecyclerView rv_device;

    private int currentPosition = 0;

    private List<SelectOrgEntity> listDevice = new ArrayList<>();
    private DropSelectOrgAdapter dropSelectOrgAdapter;

    public void init(Activity activity) {
        if(activity != null) {
            @SuppressLint("InflateParams")
            View popView = LayoutInflater.from(activity).inflate(R.layout.view_popup_selectorg, null);
            RelativeLayout rl_body = popView.findViewById(R.id.rl_body);
            iv_back = popView.findViewById(R.id.iv_back);
            iv_close = popView.findViewById(R.id.iv_close);
            rl_branch = popView.findViewById(R.id.rl_branch);
            tv_branch = popView.findViewById(R.id.tv_branch);
            view_branch = popView.findViewById(R.id.view_branch);
            tv_branchtype  = popView.findViewById(R.id.tv_branchtype);
            view_branchtype = popView.findViewById(R.id.view_branchtype);
            rl_branchtype = popView.findViewById(R.id.rl_branchtype);
            rl_device = popView.findViewById(R.id.rl_device);
            tv_device  = popView.findViewById(R.id.tv_device);
            view_device = popView.findViewById(R.id.view_device);
            view_device.setVisibility(View.INVISIBLE);
            view_branchtype.setVisibility(View.INVISIBLE);
            rv_device = popView.findViewById(R.id.rv_device);
            rv_device.setLayoutManager(new LinearLayoutManager(activity));
            dropSelectOrgAdapter = new DropSelectOrgAdapter(listDevice);
            rv_device.setAdapter(dropSelectOrgAdapter);
            dropSelectOrgAdapter.setOnSelectListener(new DropSelectOrgAdapter.onSelectListener() {
                @Override
                public void onSelect(int position) {
                    for (int i = 0; i < listDevice.size(); i++) {
                        listDevice.get(i).isSelect = (i == position);
                    }
                    dropSelectOrgAdapter.notifyDataSetChanged();
                    if(onSelectListener != null) {
                        if(currentPosition == 0) {
                            tv_branch.setText(listDevice.get(position).name);
                            rl_branchtype.setVisibility(View.VISIBLE);
                            view_branchtype.setVisibility(View.VISIBLE);
                            view_branch.setVisibility(View.INVISIBLE);
                            onSelectListener.onSelect(position, currentPosition);
                        } else if(currentPosition == 1) {
                            tv_branchtype.setText(listDevice.get(position).name);
                            rl_device.setVisibility(View.VISIBLE);
                            view_device.setVisibility(View.VISIBLE);
                            view_branch.setVisibility(View.INVISIBLE);
                            view_branchtype.setVisibility(View.INVISIBLE);
                            onSelectListener.onSelect(position, currentPosition);
                        }else if(currentPosition == 2) {
                            tv_device.setText(listDevice.get(position).name);
                            onSelectListener.onSelect(position, currentPosition);
                            hidePop();
                        }
                    }
                }
            });
            rl_branchtype.setVisibility(View.GONE);
            rl_device.setVisibility(View.GONE);

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

            rl_branch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition = 0;
                    rl_branch.setVisibility(View.VISIBLE);
                    view_branch.setVisibility(View.VISIBLE);
                    rl_branchtype.setVisibility(View.GONE);
                    view_branchtype.setVisibility(View.INVISIBLE);
                    rl_device.setVisibility(View.GONE);
                    view_device.setVisibility(View.INVISIBLE);
                    if(onSelectListener != null) {
                        onSelectListener.onChangeTo(currentPosition);
                    }
                }
            });

            rl_branchtype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition = 1;
                    view_branch.setVisibility(View.INVISIBLE);
                    rl_device.setVisibility(View.GONE);
                    view_device.setVisibility(View.INVISIBLE);
                    rl_branchtype.setVisibility(View.VISIBLE);
                    view_branchtype.setVisibility(View.VISIBLE);
                    if(onSelectListener != null) {
                        onSelectListener.onChangeTo(currentPosition);
                    }
                }
            });
            rl_device.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition = 2;
                    if(onSelectListener != null) {
                        onSelectListener.onChangeTo(currentPosition);
                    }
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

    public void setList(List<SelectOrgEntity> listFirst, int currentPosition) {
        this.currentPosition = currentPosition;
        listDevice.clear();
        listDevice.addAll(listFirst);
        dropSelectOrgAdapter.notifyDataSetChanged();
    }

    private onSelectListener onSelectListener;

    public interface onSelectListener {
        void onSelect(int position, int current);
        void onChangeTo(int current);
    }

    public void setOnSelectListener(onSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }
}
