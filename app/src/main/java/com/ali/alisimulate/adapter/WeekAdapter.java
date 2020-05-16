package com.ali.alisimulate.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.WeekEntity;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/16 9:03
 * Description:WeekAdapter
 **/
public class WeekAdapter extends RecyclerView.Adapter {
    private final List<WeekEntity> mData;

    public WeekAdapter(List<WeekEntity> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false);
        WeekHolder viewHolder = new WeekHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WeekHolder viewHolder = (WeekHolder) holder;
        WeekEntity name = mData.get(position);
        viewHolder.tv_week.setText(name.week);
        if(name.isSelect) {
            viewHolder.tv_week.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.tv_week.setBackgroundColor(Color.parseColor("#10AB6C"));
        } else {
            viewHolder.tv_week.setTextColor(Color.parseColor("#585858"));
            viewHolder.tv_week.setBackgroundColor(Color.parseColor("#EEF8F4"));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class WeekHolder extends RecyclerView.ViewHolder {
        private TextView tv_week;

        public WeekHolder(@NonNull View itemView) {
            super(itemView);
            tv_week = itemView.findViewById(R.id.tv_week);
        }
    }
}
