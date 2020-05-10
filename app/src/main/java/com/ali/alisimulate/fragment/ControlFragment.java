package com.ali.alisimulate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.ControlAdapter;
import com.ali.alisimulate.entity.DeviceControl;

import java.util.ArrayList;
import java.util.List;

public class ControlFragment extends Fragment {
    private RecyclerView rv_control;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        rv_control = v.findViewById(R.id.rv_control);
        rv_control.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        List<DeviceControl> list = new ArrayList<>();
        DeviceControl deviceControl = new DeviceControl();
        deviceControl.setName("自动模式");
        deviceControl.setType(0);
        deviceControl.setOpen(true);
        deviceControl.setStatus("已开启");
        list.add(deviceControl);

        deviceControl = new DeviceControl();
        deviceControl.setName("风速");
        deviceControl.setType(1);
        deviceControl.setStatus("静音档");
        deviceControl.setLevel("1档");
        list.add(deviceControl);

        deviceControl = new DeviceControl();
        deviceControl.setName("睡眠模式");
        deviceControl.setType(0);
        deviceControl.setOpen(false);
        deviceControl.setStatus("未启用");
        list.add(deviceControl);

        deviceControl = new DeviceControl();
        deviceControl.setName("定时");
        deviceControl.setType(0);
        deviceControl.setOpen(false);
        deviceControl.setStatus("未启用");
        list.add(deviceControl);

        deviceControl = new DeviceControl();
        deviceControl.setName("童锁开关");
        deviceControl.setType(0);
        deviceControl.setOpen(false);
        deviceControl.setStatus("未开启");
        list.add(deviceControl);

        deviceControl = new DeviceControl();
        deviceControl.setName("加湿开关");
        deviceControl.setType(0);
        deviceControl.setOpen(false);
        deviceControl.setStatus("未开启");
        list.add(deviceControl);

        deviceControl = new DeviceControl();
        deviceControl.setName("离子团开关");
        deviceControl.setType(0);
        deviceControl.setOpen(true);
        deviceControl.setStatus("已开启");
        list.add(deviceControl);
        ControlAdapter adapter = new ControlAdapter(getActivity(), list);
        rv_control.setAdapter(adapter);
    }
}
