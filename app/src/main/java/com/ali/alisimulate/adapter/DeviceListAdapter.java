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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/13 18:31
 * Description:DeviceListAdapter
 **/
public class DeviceListAdapter extends BaseQuickAdapter<OrgDevice.DeviceList, BaseViewHolder> {

    public DeviceListAdapter(List<OrgDevice.DeviceList> data) {
        super(R.layout.item_devicelist, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, OrgDevice.DeviceList name) {
        TextView tv_net = helper.getView(R.id.tv_net);
        TextView tv_unnet = helper.getView(R.id.tv_unnet);
        if(name.isCheck) {
            tv_net.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.icon_check), null, null, null);
            tv_unnet.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.icon_uncheck), null, null, null);
        } else {
            tv_net.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.icon_uncheck), null, null, null);
            tv_unnet.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.icon_check), null, null, null);
        }

        tv_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckListener.onCheck(helper.getAdapterPosition(), true);
            }
        });

        tv_unnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckListener.onCheck(helper.getAdapterPosition(), false);
            }
        });

        Bitmap bitmap = ZXingUtils.createQRImage(name.deviceName, DisplayUtil.dip2px(mContext, 24), DisplayUtil.dip2px(mContext, 24));
        ImageView iv_code = helper.getView(R.id.iv_code);

        iv_code.setImageBitmap(bitmap);
        helper.setText(R.id.tv_devicename, name.deviceComment);
        helper.setText(R.id.tv_devicekey, "设备名称: " + name.deviceName);
        helper.setText(R.id.tv_alias, name.brandName + " " + getModelStr(name.deviceModel) + " " + name.deviceComment);
        helper.setText(R.id.tv_status, name.bindingStatus);
        TextView tv_status = helper.getView(R.id.tv_status);
        if("1".equals(name.deviceModel)) {
            tv_net.setVisibility(View.INVISIBLE);
            tv_status.setVisibility(View.INVISIBLE);
            tv_unnet.setVisibility(View.INVISIBLE);
        } else {
            tv_net.setVisibility(View.VISIBLE);
            tv_unnet.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.VISIBLE);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectListener != null) {
                    onSelectListener.onSelect(helper.getAdapterPosition());
                }
            }
        });

        iv_code.setOnClickListener(view -> {
            SecondWCodeDialog dialog = new SecondWCodeDialog(mContext);
            dialog.setDeviceName(name.deviceName);
            dialog.setDeviceDesc(name.brandName + " " + getModelStr(name.deviceModel) + " " + name.deviceComment);
            dialog.show();
        });
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(int position, boolean isCheck);
    }

    private OnCheckListener onCheckListener;

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

    private OnSelectListener onSelectListener;

    public interface OnSelectListener {
        public void onSelect(int position);
    }
}