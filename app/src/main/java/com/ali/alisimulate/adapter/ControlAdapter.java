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
import com.ali.alisimulate.entity.DeviceControl;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ControlAdapter extends RecyclerView.Adapter {
    private final List<DeviceControl> mData;
    private final Context context;
    private OnCheckListener onCheckListener;

    public ControlAdapter(Context context, List<DeviceControl> data) {
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
        DeviceControl name = mData.get(position);
        viewHolder.tv_name.setText(name.getName());
        viewHolder.tv_status.setText(name.getStatus());
        if(name.getType() == 0) {//开关
            viewHolder.sw.setChecked(name.isOpen());
            viewHolder.sw.setVisibility(View.VISIBLE);
            viewHolder.tv_choose.setVisibility(View.GONE);
            viewHolder.iv_arrow.setVisibility(View.GONE);
            viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(onCheckListener != null) {
                        onCheckListener.onCheck(position, b);
                    }
                }
            });
        } else {//下拉选
            viewHolder.sw.setVisibility(View.GONE);
            viewHolder.tv_choose.setText(name.getLevel());
            viewHolder.tv_choose.setVisibility(View.VISIBLE);
            viewHolder.iv_arrow.setVisibility(View.VISIBLE);
            viewHolder.tv_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                            R.layout.pop_device, null, true);
                    PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    pw.setOutsideTouchable(true);
                    pw.showAsDropDown(viewHolder.tv_choose);
                    RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                    rv_device.setLayoutManager(new LinearLayoutManager(context));
                    List<String> list = new ArrayList<>();
                    list.add("1档");
                    list.add("2档");
                    list.add("3档");
                    list.add("4档");
                    PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                    rv_device.setAdapter(adapter);
                    adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                        @Override
                        public void onCheck(int pos) {
                            viewHolder.tv_choose.setText(list.get(pos));
                            pw.dismiss();
                            if(onCheckListener != null) {
                                onCheckListener.onSelect(position, list.get(pos));
                            }
                        }
                    });
                }
            });
        }
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

    private void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(int position, boolean isOpen);
        void onSelect(int position, String level);
    }
}

