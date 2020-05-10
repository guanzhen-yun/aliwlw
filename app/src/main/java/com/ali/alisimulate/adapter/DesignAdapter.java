package com.ali.alisimulate.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

import java.util.List;

public class DesignAdapter extends RecyclerView.Adapter {
    private final List<Property> mData;
    private final Activity mContext;
    private OnCheckedListener onCheckedListener;

    public DesignAdapter(Activity context, List<Property> data) {
        mData = data;
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        DesignViewHolder viewHolder = new DesignViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        Property property = mData.get(position);
        viewHolder.mTv1.setText(property.getName());
        viewHolder.mTv2.setText(property.getIdentifier());
        viewHolder.mTv3.setText(property.getDataType().getType());
        if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(property.getDataType().getType())) {
            viewHolder.sh.setVisibility(View.VISIBLE);
            ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(property.getIdentifier());
            if(propertyValue != null) {
                Integer value = ((ValueWrapper.BooleanValueWrapper) propertyValue).getValue();
                if(value != null && value == 1) {
                    viewHolder.sh.setChecked(true);
                } else {
                    viewHolder.sh.setChecked(false);
                }
            }
            viewHolder.sh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (onCheckedListener != null) {
                        onCheckedListener.onCheck(isChecked, position, property);
                    }
                }
            });
        } else {
            viewHolder.sh.setVisibility(View.GONE);
        }
    }

    public interface OnCheckedListener {
        void onCheck(boolean isChecked, int position, Property property);
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
        private TextView mTv1;
        private TextView mTv2;
        private TextView mTv3;
        private Switch sh;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv1 = itemView.findViewById(R.id.tv1);
            mTv2 = itemView.findViewById(R.id.tv2);
            mTv3 = itemView.findViewById(R.id.tv3);
            sh = itemView.findViewById(R.id.sh);
        }
    }
}
