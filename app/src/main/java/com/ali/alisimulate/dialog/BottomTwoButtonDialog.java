package com.ali.alisimulate.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ali.alisimulate.R;
import com.ali.alisimulate.util.DisplayUtil;

public class BottomTwoButtonDialog extends Dialog {
    private String mContent;//内容
    private String mTitle;//标题
    private int mLeftMargin;//左右间距
    private int mTopMargin;//上间距
    private boolean mIsSingle;//是否单个按钮
    private String mLeftButton;//左边按钮
    private String mRightButton;//右边按钮

    private OnClickDialogListener mOnClickDialogListener;
    private OnSingleClickDialogListener mOnSingleClickDialogListener;

    public BottomTwoButtonDialog(@NonNull Activity activity) {
        super(activity, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hire_dialog_bottom_twobutton);
        setView();
    }

    private void setView() {
        TextView tv_content = findViewById(R.id.tv_content);
        TextView tv_title = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mTitle)) {
            tv_title.setText(mTitle);
            tv_title.setVisibility(View.VISIBLE);
        }
        TextView tv_left = findViewById(R.id.tv_left);
        if (!TextUtils.isEmpty(mLeftButton)) {
            tv_left.setText(mLeftButton);
        }
        View view_line = findViewById(R.id.view_line);
        TextView tv_right = findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(mRightButton)) {
            tv_right.setText(mRightButton);
        }
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(mContent);
        }
        int left = (mLeftMargin == 0 ? DisplayUtil.dip2px(getContext(), 24) : DisplayUtil.dip2px(getContext(), mLeftMargin));
        int top = (mTopMargin == 0 ? DisplayUtil.dip2px(getContext(), 20) : DisplayUtil.dip2px(getContext(), mTopMargin));
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(left, top, left, 0);
        tv_content.setLayoutParams(layoutParam);

        if (mIsSingle) {
            view_line.setVisibility(View.GONE);
            tv_left.setVisibility(View.GONE);
        }

        tv_left.setOnClickListener(v -> {
            if (mOnClickDialogListener != null) {
                mOnClickDialogListener.onClickLeft();
            }
        });

        tv_right.setOnClickListener(v -> {
            if (mOnClickDialogListener != null) {
                mOnClickDialogListener.onClickRight();
            }
            if(mOnSingleClickDialogListener != null) {
                mOnSingleClickDialogListener.onClickRight();
            }
        });
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    /**
     * 设置左右间距
     */
    public void setLeftAndRightMargin(int leftMargin) {
        this.mLeftMargin = leftMargin;
    }

    /**
     * 设置上间距
     */
    public void setTopMargin(int topMargin) {
        this.mTopMargin = topMargin;
    }

    public void setSingleBottom(boolean isSingle) {
        this.mIsSingle = isSingle;
    }

    public void setOnClickDialogListener(OnClickDialogListener onClickDialogListener) {
        this.mOnClickDialogListener = onClickDialogListener;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setLeftButton(String leftButton) {
        this.mLeftButton = leftButton;
    }

    public void setRightButton(String rightButton) {
        this.mRightButton = rightButton;
    }

    public void setOnSingleClickDialogListener(OnSingleClickDialogListener onSingleClickDialogListener) {
        this.mOnSingleClickDialogListener = onSingleClickDialogListener;
    }

    public interface OnClickDialogListener {
        void onClickLeft();

        void onClickRight();
    }

    public interface OnSingleClickDialogListener {
        void onClickRight();
    }
}
