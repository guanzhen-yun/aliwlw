package com.ali.alisimulate.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.SelectOrgEntity;

import java.util.List;

/**
 * Author:关震
 * Date:2020/5/13 15:49
 * Description:DropSelectOrgAdapter
 **/
public class DropSelectOrgAdapter extends RecyclerView.Adapter {
    private List<SelectOrgEntity> mData;

    public DropSelectOrgAdapter(List<SelectOrgEntity> data) {
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drop_select, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        SelectOrgEntity name = mData.get(position);
        viewHolder.tv_name.setText(name.name);
        if (name.isSelect) {
            viewHolder.tv_name.setTextColor(Color.parseColor("#00ff00"));
        } else {
            viewHolder.tv_name.setTextColor(Color.parseColor("#000000"));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSelectListener != null) {
                    onSelectListener.onSelect(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnSelectListener(DropSelectOrgAdapter.onSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }

    private onSelectListener onSelectListener;

    public interface onSelectListener {
        void onSelect(int position);
    }
}
