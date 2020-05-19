package com.ali.alisimulate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.BranchTypeEntity;
import com.bumptech.glide.Glide;

import java.util.List;

public class RightDeviceAdapter extends RecyclerView.Adapter {
    private final List<BranchTypeEntity> mData;
    private final Context mContext;
    private OnCheckedListener onCheckedListener;

    public RightDeviceAdapter(Context context, List<BranchTypeEntity> data) {
        mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rightdevice, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        BranchTypeEntity name = mData.get(position);
        viewHolder.tv_device.setText(name.name);
        if(name.isSelect) {
            viewHolder.iv.setBackgroundResource(R.mipmap.icon_sel);
        } else {
            viewHolder.iv.setBackgroundResource(R.mipmap.icon_unsel);
        }

        if(TextUtils.isEmpty(name.imageUrl)) {
            viewHolder.iv_pic.setImageResource(R.mipmap.shebei);
        } else {
            Glide.with(mContext).load(name.imageUrl).into(viewHolder.iv_pic);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        private ImageView iv;
        private ImageView iv_pic;
        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device = itemView.findViewById(R.id.tv_device);
            iv = itemView.findViewById(R.id.iv);
            iv_pic = itemView.findViewById(R.id.iv_pic);
        }
    }
}

