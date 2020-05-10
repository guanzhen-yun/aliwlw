package com.ali.alisimulate.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.ali.alisimulate.R;

/**
 * 二维码
 */
public class SecondWCodeDialog extends Dialog {
    public SecondWCodeDialog(@NonNull Activity activity) {
        super(activity, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_erw);
    }
}
