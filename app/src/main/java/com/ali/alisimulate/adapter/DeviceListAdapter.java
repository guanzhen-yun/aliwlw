package com.ali.alisimulate.adapter;

import android.content.Context;
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

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.util.DisplayUtil;
import com.ali.alisimulate.util.ZXingUtils;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/13 18:31
 * Description:DeviceListAdapter
 **/
public class DeviceListAdapter extends RecyclerView.Adapter {
    private List<OrgDevice> mData;
    private Context context;

    public DeviceListAdapter(Context context, List<OrgDevice> data) {
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
        OrgDevice name = mData.get(position);
        viewHolder.rg_net.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == viewHolder.rb_net.getId()) {

                } else {

                }
            }
        });
        Bitmap bitmap = ZXingUtils.createQRImage(name.deviceName, DisplayUtil.dip2px(context, 24),  DisplayUtil.dip2px(context, 24));
        viewHolder.iv_code.setImageBitmap(bitmap);
        viewHolder.tv_devicename.setText(name.deviceName);
    }

    @Override
    public int getItemCount() {
        return mData.size();
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
}