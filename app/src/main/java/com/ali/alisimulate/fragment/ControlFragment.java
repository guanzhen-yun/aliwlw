package com.ali.alisimulate.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.ControlAdapter;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.ziroom.base.BaseFragment;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(layoutId = R.layout.fragment_control)
public class ControlFragment extends BaseFragment {

    @BindView(R.id.rv_control)
    RecyclerView mRvControl;
    @BindView(R.id.iv_close)
    ImageView mIvClose;

    @Override
    public void initViews(View mView) {
        mRvControl.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // 获取所有属性
        List<Property> properties = LinkKit.getInstance().getDeviceThing().getProperties();
        List<Property> controlList = new ArrayList<>();
        for (Property property : properties) {
            if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(property.getDataType().getType())) {
                controlList.add(property);
            }
        }

        ControlAdapter adapter = new ControlAdapter(getActivity(), controlList);
        adapter.setOnCheckListener(new ControlAdapter.OnCheckListener() {
            @Override
            public void onCheck(int position, boolean isOpen) {
                // 设备上报
                Map<String, ValueWrapper> reportData = new HashMap<>();
                // identifier 是云端定义的属性的唯一标识，valueWrapper是属性的值
                Property property = controlList.get(position);
                reportData.put(property.getIdentifier(), new ValueWrapper.BooleanValueWrapper(isOpen ? 1 : 0));  // 参考示例，更多使用可参考demo
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
            public void onSelect(int position, String level) {

            }
        });
        mRvControl.setAdapter(adapter);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        //跳轉定時開關機
    }
}
