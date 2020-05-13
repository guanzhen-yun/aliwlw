package com.ali.alisimulate.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.ParamAdapter;
import com.ali.alisimulate.util.SoftKeyBoardListener;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.view.DropDownPop;
import com.ali.alisimulate.view.TopViewCycle;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamFragment extends Fragment {
    private RecyclerView rv_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_param, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        DropDownPop dropDownPop = new DropDownPop();
        dropDownPop.init(getActivity());
        rv_list = v.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        ParamAdapter adapter = new ParamAdapter();

        List<Event> events = LinkKit.getInstance().getDeviceThing().getEvents();

        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();
        List<Property> paramList = new ArrayList<>();
        for (Property property : properties) {
            if(TmpConstant.TYPE_VALUE_ENUM.equals(property.getDataType().getType()) || TmpConstant.TYPE_VALUE_INTEGER.equals(property.getDataType().getType())) {
                paramList.add(property);
            }
        }

        adapter.addDatas(paramList);
        rv_list.setAdapter(adapter);

        adapter.setOnCheckedListener(new ParamAdapter.OnCheckedListener() {
            @Override
            public void onSelect(int position, String selectId) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = paramList.get(position);
                reportData.put(property.getIdentifier(), new ValueWrapper.EnumValueWrapper(Integer.parseInt(selectId)));  // 参考示例，更多使用可参考demo
                LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
                    @Override
                    public void onSuccess(String resID, Object o) {
                        // 属性上报成功 resID 设备属性对应的唯一标识
                        Log.e("ProductActivity", "属性上报成功");
                    }

                    @Override
                    public void onError(String resId, AError aError) {
                        // 属性上报失败
                        Log.e("ProductActivity", "属性上报失败");
                    }
                });
            }

            @Override
            public void onChange(int realPosition, String et) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = paramList.get(realPosition);
                reportData.put(property.getIdentifier(), new ValueWrapper.IntValueWrapper(Integer.parseInt(et)));  // 参考示例，更多使用可参考demo
                LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {
                    @Override
                    public void onSuccess(String resID, Object o) {
                        // 属性上报成功 resID 设备属性对应的唯一标识
                        Log.e("ProductActivity", "属性上报成功");
                    }

                    @Override
                    public void onError(String resId, AError aError) {
                        // 属性上报失败
                        Log.e("ProductActivity", "属性上报失败");
                    }
                });
            }
        });

        TopViewCycle topViewCycle = new TopViewCycle(getActivity());
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("顶部");
        list1.add("顶部");
        list1.add("顶部");
        list1.add("顶部");
        topViewCycle.loadData(list1);
        topViewCycle.setOnPageClickListener(new TopViewCycle.OnPageClickListener() {
            @Override
            public void onClick(int position) {
                ToastUtils.showToast(position + "");
                dropDownPop.showPop(rv_list);
            }
        });
        adapter.setHeadView(topViewCycle);
    }
}
