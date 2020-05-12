package com.ali.alisimulate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;

import java.util.ArrayList;
import java.util.List;

public class ControlAdapter extends RecyclerView.Adapter {
    private final List<Property> mData;
    private final Context context;
    private OnCheckListener onCheckListener;

    public ControlAdapter(Context context, List<Property> data) {
        mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_control, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        Property name = mData.get(position);
        viewHolder.tv_name.setText(name.getName());
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(name.getIdentifier());
        if(propertyValue != null) {
            Integer value = ((ValueWrapper.BooleanValueWrapper) propertyValue).getValue();
            viewHolder.sw.setVisibility(View.VISIBLE);
            if(value != null && value == 1) {
                viewHolder.sw.setChecked(true);
                viewHolder.tv_status.setText("已开启");
            } else {
                viewHolder.sw.setChecked(false);
                viewHolder.tv_status.setText("未开启");
            }
        } else {
            viewHolder.sw.setChecked(false);
            viewHolder.tv_status.setText("未开启");
        }
        viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    viewHolder.tv_status.setText("已开启");
                } else {
                    viewHolder.tv_status.setText("未开启");
                }
                if(onCheckListener != null) {
                    onCheckListener.onCheck(position, b);
                }
            }
        });
        viewHolder.tv_choose.setVisibility(View.GONE);
        viewHolder.iv_arrow.setVisibility(View.GONE);

//        if(name.getType() == 0) {//开关
//        } else {//下拉选
//            viewHolder.sw.setVisibility(View.GONE);
//            viewHolder.tv_choose.setText(name.getLevel());
//            viewHolder.tv_choose.setVisibility(View.VISIBLE);
//            viewHolder.iv_arrow.setVisibility(View.VISIBLE);
//            viewHolder.tv_choose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//                    ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
//                            R.layout.pop_device, null, true);
//                    PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
//                            RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    pw.setOutsideTouchable(true);
//                    pw.showAsDropDown(viewHolder.tv_choose);
//                    RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
//                    rv_device.setLayoutManager(new LinearLayoutManager(context));
//                    List<String> list = new ArrayList<>();
//                    list.add("1档");
//                    list.add("2档");
//                    list.add("3档");
//                    list.add("4档");
//                    PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
//                    rv_device.setAdapter(adapter);
//                    adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
//                        @Override
//                        public void onCheck(int pos) {
//                            viewHolder.tv_choose.setText(list.get(pos));
//                            pw.dismiss();
//                            if(onCheckListener != null) {
//                                onCheckListener.onSelect(position, list.get(pos));
//                            }
//                        }
//                    });
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_status;
        private TextView tv_choose;
        private ImageView iv_arrow;
        private Switch sw;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            sw = itemView.findViewById(R.id.sw);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_choose = itemView.findViewById(R.id.tv_choose);
            iv_arrow = itemView.findViewById(R.id.iv_arrow);
        }
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(int position, boolean isOpen);
        void onSelect(int position, String level);
    }
}

