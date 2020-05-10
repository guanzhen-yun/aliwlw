package com.ali.alisimulate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.DesignAdapter;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tools.AError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends Activity {
    private RecyclerView rv_design;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        rv_design = findViewById(R.id.rv_design);
        rv_design.setLayoutManager(new LinearLayoutManager(this));
        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();
        DesignAdapter adapter = new DesignAdapter(this, properties);
        adapter.setOnCheckedListener(new DesignAdapter.OnCheckedListener() {
            @Override
            public void onCheck(boolean isChecked, int position, Property property) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                reportData.put(property.getIdentifier(), new ValueWrapper.BooleanValueWrapper(isChecked ? 1 : 0));  // 参考示例，更多使用可参考demo
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
        rv_design.setAdapter(adapter);
    }
}
