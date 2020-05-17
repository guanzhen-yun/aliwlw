package com.ali.alisimulate.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.BranchEntity;

import java.util.List;

public class LeftTitleAdapter extends RecyclerView.Adapter {
    private final List<BranchEntity> mData;
    private OnCheckedListener onCheckedListener;

    public LeftTitleAdapter(List<BranchEntity> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_title, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        BranchEntity name = mData.get(position);
        viewHolder.tv_title.setText(name.name);
        viewHolder.view_green.setVisibility(name.isSelect ? View.VISIBLE : View.INVISIBLE);
        if(name.isSelect) {
            viewHolder.tv_title.setTextColor(Color.parseColor("#10AB6C"));
        } else {
            viewHolder.tv_title.setTextColor(Color.parseColor("#6E6E6E"));
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

    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private View view_green;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            view_green = itemView.findViewById(R.id.view_green);
        }
    }
}

