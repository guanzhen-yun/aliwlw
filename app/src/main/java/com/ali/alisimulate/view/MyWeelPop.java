package com.ali.alisimulate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.KeyValue;
import com.ali.alisimulate.view.wheelview.WheelView;

import java.util.List;

/**
 * 自定义下拉pop
 */
public class MyWeelPop extends PopupWindow {
    public MyWeelPop(Context context, List<KeyValue> listContent, String title) {
        super(context);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        //设置布局与相关属性
        View mContentView = LayoutInflater.from(context).inflate(R.layout.layout_wheel_pop, null);
        setContentView(mContentView);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        mContentView.setOnClickListener(v -> dismiss());
        WheelView wvContent = mContentView.findViewById(R.id.wv_content);
//        TextView tvTitle = mContentView.findViewById(R.id.tv_title);
//        tvTitle.setText(title);
        wvContent.setItems(listContent, 0);
//        TextView tvCancel = mContentView.findViewById(R.id.tv_cancel);
//        TextView tvOk = mContentView.findViewById(R.id.tv_ok);
//        tvCancel.setOnClickListener(v -> dismiss());
//        tvOk.setOnClickListener(v -> {
//            if (onWeelSelectlistener != null) {
//                onWeelSelectlistener.onSelect(wvContent.getSelectedItem());
//            }
//            dismiss();
//        });
        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_mypop1));
    }

    public void setOnWeelSelectlistener(OnWeelSelectlistener onWeelSelectlistener) {
        this.onWeelSelectlistener = onWeelSelectlistener;
    }

    public interface OnWeelSelectlistener {
        public void onSelect(KeyValue item);
    }

    private OnWeelSelectlistener onWeelSelectlistener;

}
