package com.ali.alisimulate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.LeftTitleAdapter;
import com.ali.alisimulate.adapter.RightDeviceAdapter;
import com.ali.alisimulate.dialog.BottomTwoButtonDialog;
import com.ali.alisimulate.entity.LeftTitle;
import com.ali.alisimulate.entity.RightDevice;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceActivity extends Activity {
    private RecyclerView rv_left;
    private RecyclerView rv_right;
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddevice);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rv_left = findViewById(R.id.rv_left);
        rv_right = findViewById(R.id.rv_right);
        setRecy();
        setDevice();
    }

    private void setDevice() {
        List<RightDevice> list = new ArrayList<>();
        RightDevice device = new RightDevice();
        device.setDeviceName("V2空气净化器");
        list.add(device);
        device = new RightDevice();
        device.setDeviceName("V3空气净化器");
        list.add(device);
        RightDeviceAdapter adapter = new RightDeviceAdapter(list);
        adapter.setOnCheckedListener(new RightDeviceAdapter.OnCheckedListener() {
            @Override
            public void onCheck(int position) {
                BottomTwoButtonDialog dialog = new BottomTwoButtonDialog(AddDeviceActivity.this);
                dialog.setContent("是否需要创建" + list.get(position).getDeviceName() + "模拟设备?");
                dialog.setOnClickDialogListener(new BottomTwoButtonDialog.OnClickDialogListener() {
                    @Override
                    public void onClickLeft() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        dialog.dismiss();
                        //创建成功返回 刷新页面
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                dialog.show();
            }
        });
        rv_right.setAdapter(adapter);
    }

    private void setRecy() {
        rv_left.setLayoutManager(new LinearLayoutManager(this));
        List<LeftTitle> list = new ArrayList<>();
        LeftTitle title = new LeftTitle();
        title.setTitle("美的");
        list.add(title);
        title = new LeftTitle();
        title.setTitle("沁园");
        list.add(title);
        title = new LeftTitle();
        title.setTitle("海尔");
        list.add(title);
        title = new LeftTitle();
        title.setTitle("Blue air");
        list.add(title);
        title = new LeftTitle();
        title.setTitle("华为");
        list.add(title);
        LeftTitleAdapter adapter = new LeftTitleAdapter(list);
        adapter.setOnCheckedListener(new LeftTitleAdapter.OnCheckedListener() {
            @Override
            public void onCheck(int position) {
                String title = null;
                for (int i = 0; i < list.size(); i++) {
                    if (position == i) {
                        list.get(i).setSelect(true);
                        title = list.get(i).getTitle();
                    } else {
                        list.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        rv_left.setAdapter(adapter);
    }
}
