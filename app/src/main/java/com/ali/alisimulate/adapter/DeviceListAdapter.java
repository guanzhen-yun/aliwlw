package com.ali.alisimulate.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.util.DisplayUtil;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.util.ZXingUtils;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/13 18:31
 * Description:DeviceListAdapter
 **/
public class DeviceListAdapter extends RecyclerView.Adapter {
    private List<OrgDevice.DeviceList> mData;
    private Context context;

    public DeviceListAdapter(Context context, List<OrgDevice.DeviceList> data) {
        mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devicelist, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        OrgDevice.DeviceList name = mData.get(position);
        if (name.deviceName.equals(SharedPreferencesUtils.getStr(context, Constants.KEY_CONNECT_STATUS))) {
            viewHolder.rb_net.setChecked(true);
        } else {
            viewHolder.rb_unnet.setChecked(true);
        }

        viewHolder.rg_net.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == viewHolder.rb_net.getId()) {
                    String deviceName = name.deviceName;
                    String productKey = name.productKey;
                    String deviceSecret = name.deviceSecret;
                    MyApp.getApp().regist(deviceName, productKey, deviceSecret);
                } else {
                    SharedPreferencesUtils.save(MyApp.getApp(),  Constants.KEY_CONNECT_STATUS, "");
                }
            }
        });
        Bitmap bitmap = ZXingUtils.createQRImage(name.deviceName, DisplayUtil.dip2px(context, 24), DisplayUtil.dip2px(context, 24));
        viewHolder.iv_code.setImageBitmap(bitmap);
        viewHolder.tv_devicename.setText(name.deviceComment);
        viewHolder.tv_devicekey.setText("设备名称: " + name.deviceName);

        viewHolder.tv_alias.setText(name.brandName + " " + getModelStr(name.deviceModel) + " " + name.deviceComment);
        viewHolder.tv_status.setText(name.bindingStatus);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectListener != null) {
                    onSelectListener.onSelect(position);
                }
            }
        });
    }

    private String getModelStr(String model) {
        switch (model) {
            case "1":
                return "配件";
            case "2":
                return "空气净化器";
            case "3":
                return "净水器";
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private RadioGroup rg_net;
        private RadioButton rb_net;
        private RadioButton rb_unnet;
        private ImageView iv_code;
        private TextView tv_devicename;
        private TextView tv_devicekey;
        private TextView tv_alias;
        private TextView tv_status;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            rg_net = itemView.findViewById(R.id.rg_net);
            rb_net = itemView.findViewById(R.id.rb_net);
            rb_unnet = itemView.findViewById(R.id.rb_unnet);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_alias = itemView.findViewById(R.id.tv_alias);
            tv_devicekey = itemView.findViewById(R.id.tv_devicekey);
            tv_devicename = itemView.findViewById(R.id.tv_devicename);
            iv_code = itemView.findViewById(R.id.iv_code);
        }
    }

    private OnSelectListener onSelectListener;

    public interface OnSelectListener {
        public void onSelect(int position);
    }
}