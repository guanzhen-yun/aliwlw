package com.ali.alisimulate.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.PopDeviceListAdapter;
import com.ali.alisimulate.dialog.BottomTwoButtonDialog;
import com.ali.alisimulate.dialog.SecondWCodeDialog;

import java.util.ArrayList;
import java.util.List;

public class OrgMainActivity extends Activity {
    private ImageView iv_code;
    private RelativeLayout rl_device;
    private TextView tv_device;
    private ImageView iv_loginout;
    private RelativeLayout rl_body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgmain);
        rl_body = findViewById(R.id.rl_body);
        rl_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrgMainActivity.this, DeviceDetailActivity.class));
            }
        });
        iv_code = findViewById(R.id.iv_code);
        iv_loginout = findViewById(R.id.iv_loginout);
        tv_device = findViewById(R.id.tv_device);
        iv_loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomTwoButtonDialog dialog = new BottomTwoButtonDialog(OrgMainActivity.this);
                dialog.setOnClickDialogListener(new BottomTwoButtonDialog.OnClickDialogListener() {
                    @Override
                    public void onClickLeft() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        //TODO 退出
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        iv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondWCodeDialog dialog = new SecondWCodeDialog(OrgMainActivity.this);
                dialog.show();
            }
        });

        rl_device = findViewById(R.id.rl_device);
        rl_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                        R.layout.pop_device, null, true);
                PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                pw.setOutsideTouchable(true);
                pw.showAsDropDown(rl_device);
                RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                rv_device.setLayoutManager(new LinearLayoutManager(OrgMainActivity.this));
                List<String> list = new ArrayList<>();
                list.add("小米/净水机/WaterPurifier1");
                list.add("小米/净水机/WaterPurifier2");
                list.add("小米/净水机/WaterPurifier3");
                list.add("小米/净水机/WaterPurifier4");
                PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                rv_device.setAdapter(adapter);
                adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                    @Override
                    public void onCheck(int position) {
                        tv_device.setText(list.get(position));
                        pw.dismiss();
                    }
                });
            }
        });
    }

    public void adddevice(View view) {
        startActivityForResult(new Intent(OrgMainActivity.this, AddDeviceActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100) {
            //刷新设备列表
        }
    }
}
