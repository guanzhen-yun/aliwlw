package com.ali.alisimulate.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.DingShiActivity;
import com.ali.alisimulate.util.DisplayUtil;
import com.ali.alisimulate.util.SaveAndUploadAliUtil;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ControlAdapter extends RecyclerView.Adapter {
    private final List<List<Property>> mData;
    private OnCheckListener onCheckListener;

    public ControlAdapter(List<List<Property>> data) {
        mData = data;
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
        Property name = mData.get(position).get(0);
        viewHolder.tv_name.setText(name.getName());
        ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(name.getIdentifier());

        if (TmpConstant.TYPE_VALUE_ENUM.equals(name.getDataType().getType())) {
            viewHolder.tv_other_status.setVisibility(View.GONE);
            viewHolder.tv_other_per.setVisibility(View.GONE);
            EnumSpec specs = (EnumSpec) name.getDataType().getSpecs();

            Set<String> strings = specs.keySet();

            List<String> list = new ArrayList<>();

            List<String> listKey = new ArrayList<>();

            for (String string : strings) {
                list.add(specs.get(string));
                listKey.add(string);
            }
            if (propertyValue != null) {
                if (propertyValue instanceof ValueWrapper.EnumValueWrapper) {
                    Integer value = ((ValueWrapper.EnumValueWrapper) propertyValue).getValue();
                    viewHolder.tv_choose.setText(value + "(" + specs.get(String.valueOf(value)) + ")");
                } else if (propertyValue instanceof ValueWrapper.IntValueWrapper) {
                    Integer value = ((ValueWrapper.IntValueWrapper) propertyValue).getValue();
                    if(value != null) {
                        viewHolder.tv_choose.setText(value + "(" + specs.get(String.valueOf(value)) + ")");
                    }
                }
            } else {
                viewHolder.tv_choose.setText("请选择");
            }

            if (mDatas != null && mDatas instanceof Integer && mPosition == position) {
                int dd = (int) mDatas;
                viewHolder.tv_choose.setText(dd + "(" + specs.get(String.valueOf(dd)) + ")");
                if (onCheckListener != null) {
                    onCheckListener.onSelect(position, listKey.get(dd));
                }
                mDatas = null;
            }

            viewHolder.tv_choose.setVisibility(View.VISIBLE);
            viewHolder.sw.setVisibility(View.GONE);
            viewHolder.tv_status.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TmpConstant.TYPE_VALUE_ENUM.equals(name.getDataType().getType())) {
                        LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                R.layout.pop_device, null, true);
                        PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        pw.setOutsideTouchable(true);
                        pw.showAsDropDown(viewHolder.tv_choose);
                        RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                        rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rv_device.getLayoutParams();
                        if ((position + 1) % 2 != 1) {
                            layoutParams.setMargins(DisplayUtil.getScreenWight((Activity) rv_device.getContext()) / 2, 30, 0, 0);
                        } else {
                            layoutParams.setMargins(0, 30, DisplayUtil.getScreenWight((Activity) rv_device.getContext()) / 2, 0);
                        }

                        rv_device.setLayoutParams(layoutParams);
                        PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                        rv_device.setAdapter(adapter);
                        adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                            @Override
                            public void onCheck(int pos) {
                                pw.dismiss();
                                viewHolder.tv_choose.setText(list.get(pos));
                                if (onCheckListener != null) {
                                    onCheckListener.onSelect(viewHolder.getAdapterPosition(), listKey.get(pos));
                                }
                            }
                        });
                    }
                }
            });
        } else if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(name.getDataType().getType())) {
            if (propertyValue != null) {
                if(propertyValue instanceof ValueWrapper.IntValueWrapper) {
                    Integer value = ((ValueWrapper.IntValueWrapper) propertyValue).getValue();
                    if (value != null && value == 1) {
                        viewHolder.sw.setChecked(true);
                        viewHolder.tv_status.setText("已开启");
                    } else {
                        viewHolder.sw.setChecked(false);
                        viewHolder.tv_status.setText("未开启");
                    }
                } else if(propertyValue instanceof ValueWrapper.BooleanValueWrapper) {
                    Integer value = ((ValueWrapper.BooleanValueWrapper) propertyValue).getValue();
                    if (value != null && value == 1) {
                        viewHolder.sw.setChecked(true);
                        viewHolder.tv_status.setText("已开启");
                    } else {
                        viewHolder.sw.setChecked(false);
                        viewHolder.tv_status.setText("未开启");
                    }
                }
            } else {
                viewHolder.sw.setChecked(false);
                viewHolder.tv_status.setText("未开启");
            }

            if (mDatas != null && mDatas instanceof Integer && mPosition == position) {
                int dd = (int) mDatas;
                if (dd == 1) {
                    viewHolder.sw.setChecked(true);
                    viewHolder.tv_status.setText("已开启");
                } else {
                    viewHolder.sw.setChecked(false);
                    viewHolder.tv_status.setText("未开启");
                }
                if (onCheckListener != null) {
                    onCheckListener.onCheck(position, dd == 1);
                }
                mDatas = null;
            } else if(mDatas != null && mDatas instanceof Double && mPosition == position) {
                double dd = (double) mDatas;
                if (dd == 1) {
                    viewHolder.sw.setChecked(true);
                    viewHolder.tv_status.setText("已开启");
                } else {
                    viewHolder.sw.setChecked(false);
                    viewHolder.tv_status.setText("未开启");
                }
                if (onCheckListener != null) {
                    onCheckListener.onCheck(position, dd == 1);
                }
                mDatas = null;
            }

            viewHolder.sw.setVisibility(View.VISIBLE);
            viewHolder.tv_status.setVisibility(View.VISIBLE);
            viewHolder.sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        viewHolder.tv_status.setText("已开启");
                    } else {
                        viewHolder.tv_status.setText("未开启");
                    }
                    if (onCheckListener != null) {
                        onCheckListener.onCheck(viewHolder.getAdapterPosition(), b);
                    }
                }
            });
            viewHolder.tv_choose.setVisibility(View.GONE);

            if (mData.get(position).size() == 3) {
                viewHolder.tv_other_status.setVisibility(View.VISIBLE);
                viewHolder.tv_other_per.setVisibility(View.VISIBLE);
                Property propertyStatus = mData.get(position).get(1);

                Integer value = SaveAndUploadAliUtil.getEnumVal(propertyStatus.getIdentifier());
                if (value == null) {
                    if (propertyStatus.getIdentifier().equals("WashingState")) {
                        viewHolder.tv_other_status.setText("正常");
                    } else if (propertyStatus.getIdentifier().equals("PureState")) {
                        viewHolder.tv_other_status.setText("待机");
                    }
                } else {
                    String enumValue = SaveAndUploadAliUtil.getEnumValue(propertyStatus, value);
                    viewHolder.tv_other_status.setText(enumValue);
                }
                Property propertyPer = mData.get(position).get(2);
                Integer pervalue = SaveAndUploadAliUtil.getIntVal(propertyPer.getIdentifier());
                if (pervalue == null) {
                    viewHolder.tv_other_per.setText("0%");
                } else {
                    viewHolder.tv_other_per.setText(pervalue + "%");
                }
            } else {
                viewHolder.tv_other_status.setVisibility(View.GONE);
                viewHolder.tv_other_per.setVisibility(View.GONE);
            }
        } else if (TmpConstant.TYPE_VALUE_ARRAY.equals(name.getDataType().getType()) && name.getIdentifier().equals("LocalTimer")) {
            viewHolder.tv_other_status.setVisibility(View.GONE);
            viewHolder.tv_other_per.setVisibility(View.GONE);
            if (propertyValue != null) {
                viewHolder.sw.setVisibility(View.VISIBLE);
                viewHolder.tv_status.setVisibility(View.VISIBLE);
                viewHolder.sw.setChecked(false);
                viewHolder.tv_status.setText("未开启");
                List<ValueWrapper> value = ((ValueWrapper.ArrayValueWrapper) propertyValue).getValue();
                if (value != null && value.size() > 0) {
                    for (ValueWrapper valueWrapper : value) {
                        Map<String, ValueWrapper> reportData = (Map<String, ValueWrapper>) valueWrapper.getValue();
                        if (reportData != null && reportData.size() > 0) {
                            ValueWrapper.BooleanValueWrapper enable = (ValueWrapper.BooleanValueWrapper) reportData.get("Enable");
                            if(enable != null && enable.getValue() != null && enable.getValue() == 1) {
                                viewHolder.sw.setChecked(true);
                                viewHolder.tv_status.setText("已开启");
                                break;
                            }
                        }
                    }
                }
            } else {
                viewHolder.sw.setChecked(false);
                viewHolder.tv_status.setText("未开启");
            }
            viewHolder.sw.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    viewHolder.tv_status.setText("已开启");
                } else {
                    viewHolder.tv_status.setText("未开启");
                }
                if (onCheckListener != null && !b) {
                    onCheckListener.onCheck(position, b);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), DingShiActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setLocalOpenOrFalse() {
        for (int i = 0; i < mData.size(); i++) {
            List<Property> properties = mData.get(i);
            if (properties.size() == 1 && TmpConstant.TYPE_VALUE_ARRAY.equals(properties.get(0).getDataType().getType())) {
                notifyItemChanged(i);
                break;
            }
        }
    }

    public class DesignViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_status;
        private TextView tv_choose;
        private Switch sw;
        private TextView tv_other_status;
        private TextView tv_other_per;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            sw = itemView.findViewById(R.id.sw);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_choose = itemView.findViewById(R.id.tv_choose);
            tv_other_status = itemView.findViewById(R.id.tv_other_status);
            tv_other_per = itemView.findViewById(R.id.tv_other_per);
        }
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener {
        void onCheck(int position, boolean isOpen);

        void onSelect(int position, String level);
    }

    public void setDataByPos(int position, Object data) {
        mDatas = data;
        mPosition = position;
        notifyItemChanged(position);
    }

    private Object mDatas;
    private int mPosition;
}

