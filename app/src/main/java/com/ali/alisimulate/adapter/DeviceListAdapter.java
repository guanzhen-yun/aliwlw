package com.ali.alisimulate.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.R;
import com.ali.alisimulate.dialog.SecondWCodeDialog;
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
    private Handler handler = new Handler();

    public DeviceListAdapter(Context context, List<OrgDevice.DeviceList> data) {
        mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devicelist, parent, false);
        return new DesignViewHolder(inflate);
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(int position, boolean isCheck);
    }

    private OnCheckListener onCheckListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        OrgDevice.DeviceList name = mData.get(position);
        if(name.isCheck) {
            viewHolder.tv_net.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.icon_check), null, null, null);
            viewHolder.tv_unnet.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.icon_uncheck), null, null, null);
        } else {
            viewHolder.tv_net.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.icon_uncheck), null, null, null);
            viewHolder.tv_unnet.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.icon_check), null, null, null);
        }

        viewHolder.tv_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckListener.onCheck(viewHolder.getAdapterPosition(), true);
            }
        });

        viewHolder.tv_unnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckListener.onCheck(viewHolder.getAdapterPosition(), false);
            }
        });

        Bitmap bitmap = ZXingUtils.createQRImage(name.deviceName, DisplayUtil.dip2px(context, 24), DisplayUtil.dip2px(context, 24));
        viewHolder.iv_code.setImageBitmap(bitmap);
        viewHolder.tv_devicename.setText(name.deviceComment);
        viewHolder.tv_devicekey.setText("设备名称: " + name.deviceName);

        viewHolder.tv_alias.setText(name.brandName + " " + getModelStr(name.deviceModel) + " " + name.deviceComment);
        viewHolder.tv_status.setText(name.bindingStatus);

        if("1".equals(name.deviceModel)) {
            viewHolder.tv_net.setVisibility(View.INVISIBLE);
            viewHolder.tv_status.setVisibility(View.INVISIBLE);
            viewHolder.tv_unnet.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tv_net.setVisibility(View.VISIBLE);
            viewHolder.tv_unnet.setVisibility(View.VISIBLE);
            viewHolder.tv_status.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectListener != null) {
                    onSelectListener.onSelect(position);
                }
            }
        });

        viewHolder.iv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondWCodeDialog dialog = new SecondWCodeDialog(context);
                dialog.setDeviceName(name.deviceName);
                dialog.setDeviceDesc(name.brandName + " " + getModelStr(name.deviceModel) + " " + name.deviceComment);
                dialog.show();
            }
        });
    }

    private void setPreUninit(String predevice) {
        SharedPreferencesUtils.save(MyApp.getApp(),  Constants.KEY_CONNECT_STATUS, "");
        for (int i = 0; i < mData.size(); i++) {
            OrgDevice.DeviceList deviceList = mData.get(i);
            if(deviceList.deviceName.equals(predevice)) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public String getModelStr(String model) {
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
        private TextView tv_net;
        private TextView tv_unnet;
        private ImageView iv_code;
        private TextView tv_devicename;
        private TextView tv_devicekey;
        private TextView tv_alias;
        private TextView tv_status;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_net = itemView.findViewById(R.id.tv_net);
            tv_unnet = itemView.findViewById(R.id.tv_unnet);
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