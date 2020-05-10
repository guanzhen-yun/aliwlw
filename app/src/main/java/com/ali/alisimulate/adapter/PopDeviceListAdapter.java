package com.ali.alisimulate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;

import java.util.List;

public class PopDeviceListAdapter extends RecyclerView.Adapter {
    private final List<String> mData;
    private OnCheckedListener onCheckedListener;

    public PopDeviceListAdapter(List<String> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popdevice, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        String name = mData.get(position);
        viewHolder.tv_device.setText(name);
        viewHolder.tv_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCheckedListener != null) {
                    onCheckedListener.onCheck(position);
                }
            }
        });
    }

    public interface OnCheckedListener {
        void onCheck(int position);
    }

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //缓存View 内存友好设计
    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_device;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device = itemView.findViewById(R.id.tv_device);
        }
    }
}

