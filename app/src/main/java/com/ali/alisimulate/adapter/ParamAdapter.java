package com.ali.alisimulate.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.util.DisplayUtil;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.EnumSpec;
import com.aliyun.alink.linksdk.tmp.devicemodel.specs.MetaSpec;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ParamAdapter extends BaseRecyclerAdapter<Property> {

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_param, parent, false);
        return new ParamsHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, Property data) {
        if (viewHolder instanceof ParamsHolder) {
            if(realPosition == getItemCount()-1) {
                ((ParamsHolder) viewHolder).view_bottom.setVisibility(View.VISIBLE);
            } else {
                ((ParamsHolder) viewHolder).view_bottom.setVisibility(View.GONE);
            }
            ((ParamsHolder) viewHolder).tv_name.setText(data.getName());
            if (TmpConstant.TYPE_VALUE_ENUM.equals(data.getDataType().getType())) {
                ((ParamsHolder) viewHolder).et_prop.setVisibility(View.GONE);
                ((ParamsHolder) viewHolder).tv_prop.setVisibility(View.VISIBLE);

                EnumSpec specs = (EnumSpec) data.getDataType().getSpecs();

                Set<String> strings = specs.keySet();

                List<String> list = new ArrayList<>();

                List<String> listKey = new ArrayList<>();

                for (String string : strings) {
                    list.add(specs.get(string));
                    listKey.add(string);
                }
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(data.getIdentifier());
                if (propertyValue != null) {
                    Integer value = ((ValueWrapper.EnumValueWrapper) propertyValue).getValue();
                    ((ParamsHolder) viewHolder).tv_prop.setText(value + "(" + specs.get(String.valueOf(value)) + ")");
                }
                ((ParamsHolder) viewHolder).iv_select.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).tv_unit.setVisibility(View.GONE);

                if(mData != null && mData instanceof Integer && mPosition == realPosition) {
                    int dd = (int) mData;
                    ((ParamsHolder) viewHolder).tv_prop.setText(listKey.get(dd) + "(" + list.get(dd) + ")");
                    if (onCheckedListener != null) {
                        onCheckedListener.onSelect(realPosition, listKey.get(dd));
                    }
                    mData = null;
                }

                ((ParamsHolder) viewHolder).rl_form.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TmpConstant.TYPE_VALUE_ENUM.equals(data.getDataType().getType())) {
                            LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                    R.layout.pop_device, null, true);
                            PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            pw.setOutsideTouchable(true);
                            pw.showAsDropDown(((ParamsHolder) viewHolder).rl_form);
                            RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                            rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rv_device.getLayoutParams();
                            layoutParams.setMargins(DisplayUtil.dip2px(rv_device.getContext(), 109),10,0,0);
                            rv_device.setLayoutParams(layoutParams);
                            PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                            rv_device.setAdapter(adapter);
                            adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                                @Override
                                public void onCheck(int pos) {
                                    ((ParamsHolder) viewHolder).tv_prop.setText(listKey.get(pos) + "(" + list.get(pos) + ")");
                                    if (onCheckedListener != null) {
                                        onCheckedListener.onSelect(realPosition, listKey.get(pos));
                                    }
                                    pw.dismiss();
                                }
                            });
                        }
                    }
                });
                ((ParamsHolder) viewHolder).tv_tip.setVisibility(View.GONE);
            } else if (TmpConstant.TYPE_VALUE_FLOAT.equals(data.getDataType().getType())) {
                ((ParamsHolder) viewHolder).iv_select.setVisibility(View.GONE);
                ((ParamsHolder) viewHolder).tv_unit.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).tv_tip.setVisibility(View.VISIBLE);
            } else if (TmpConstant.TYPE_VALUE_DOUBLE.equals(data.getDataType().getType())) {
                ((ParamsHolder) viewHolder).iv_select.setVisibility(View.GONE);
                ((ParamsHolder) viewHolder).tv_unit.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).tv_tip.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).et_prop.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).tv_prop.setVisibility(View.GONE);
                if(data.getDataType().getSpecs() != null) {
                    MetaSpec specs = (MetaSpec) data.getDataType().getSpecs();
                    ((ParamsHolder) viewHolder).tv_unit.setVisibility(View.VISIBLE);
                    ((ParamsHolder) viewHolder).tv_tip.setText("double型, 范围: " + specs.getMin() + "～" + specs.getMax() + ",步长" + specs.getStep());
                    ((ParamsHolder) viewHolder).tv_unit.setText(specs.getUnit());
                } else {
                    ((ParamsHolder) viewHolder).tv_tip.setText("double型");
                    ((ParamsHolder) viewHolder).tv_unit.setVisibility(View.GONE);
                }

                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(data.getIdentifier());
                if (propertyValue != null) {
                    Double value = ((ValueWrapper.DoubleValueWrapper) propertyValue).getValue();
                    if (value != null) {
                        ((ParamsHolder) viewHolder).et_prop.setText(String.valueOf(value));
                    }
                }
                if (((ParamsHolder) viewHolder).et_prop.getTag() instanceof TextWatcher) {
                    ((ParamsHolder) viewHolder).et_prop.removeTextChangedListener((TextWatcher) (((ParamsHolder) viewHolder).et_prop.getTag()));
                }
                if(mData != null && mData instanceof Double && mPosition == realPosition) {
                    double dd = (double) mData;
                    ((ParamsHolder) viewHolder).et_prop.setText(String.valueOf(dd));
                    if (onCheckedListener != null) {
                        onCheckedListener.onChange(realPosition, dd + "");
                    }
                    mData = null;
                }
                ((ParamsHolder) viewHolder).et_prop.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                final TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (((ParamsHolder) viewHolder).et_prop.hasFocus()) {
                            String et = ((ParamsHolder) viewHolder).et_prop.getText().toString();
                            if(data.getDataType().getSpecs() != null) {
                                MetaSpec specs = (MetaSpec) data.getDataType().getSpecs();
                                if(!TextUtils.isEmpty(et) && (Double.parseDouble(et) > Double.parseDouble(specs.getMax()) || Double.parseDouble(et) < Double.parseDouble(specs.getMin()))) {
                                    ((ParamsHolder) viewHolder).tv_tip.setTextColor(Color.parseColor("#ff0000"));
                                } else {
                                    ((ParamsHolder) viewHolder).tv_tip.setTextColor(Color.parseColor("#ADADAD"));
                                }
                                if(!TextUtils.isEmpty(et) && Double.parseDouble(et) >= Double.parseDouble(specs.getMin()) && Double.parseDouble(et) <= Double.parseDouble(specs.getMax()) && onCheckedListener != null) {
                                    onCheckedListener.onChange(realPosition, et);
                                }
                            } else {
                                if(!TextUtils.isEmpty(et) && onCheckedListener != null) {
                                    onCheckedListener.onChange(realPosition, et);
                                }
                            }
                        }
                    }
                };

                ((ParamsHolder) viewHolder).et_prop.setTag(watcher);
                ((ParamsHolder) viewHolder).et_prop.addTextChangedListener(watcher);
                ((ParamsHolder) viewHolder).et_prop.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        ((ParamsHolder) viewHolder).et_prop.addTextChangedListener(watcher);
                    } else {
                        ((ParamsHolder) viewHolder).et_prop.removeTextChangedListener(watcher);
                    }
                });

            } else if (TmpConstant.TYPE_VALUE_INTEGER.equals(data.getDataType().getType())) {
                ((ParamsHolder) viewHolder).et_prop.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).tv_prop.setVisibility(View.GONE);
                ((ParamsHolder) viewHolder).iv_select.setVisibility(View.GONE);
                ((ParamsHolder) viewHolder).tv_unit.setVisibility(View.VISIBLE);
                ((ParamsHolder) viewHolder).tv_tip.setVisibility(View.VISIBLE);
                if(data.getDataType().getSpecs() != null) {
                    MetaSpec specs = (MetaSpec) data.getDataType().getSpecs();
                    ((ParamsHolder) viewHolder).tv_tip.setText("int32型, 范围: " + specs.getMin() + "～" + specs.getMax() + ",步长" + specs.getStep());
                    ((ParamsHolder) viewHolder).tv_unit.setText(specs.getUnit());
                } else {
                    ((ParamsHolder) viewHolder).tv_tip.setText("int32型");
                }

                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(data.getIdentifier());
                if (propertyValue != null) {
                    Integer value = ((ValueWrapper.IntValueWrapper) propertyValue).getValue();
                    if (value != null) {
                        ((ParamsHolder) viewHolder).et_prop.setText(String.valueOf(value));
                    }
                }
                if (((ParamsHolder) viewHolder).et_prop.getTag() instanceof TextWatcher) {
                    ((ParamsHolder) viewHolder).et_prop.removeTextChangedListener((TextWatcher) (((ParamsHolder) viewHolder).et_prop.getTag()));
                }
                if(mData != null && mData instanceof Integer && mPosition == realPosition) {
                    int dd = (int) mData;
                    ((ParamsHolder) viewHolder).et_prop.setText(String.valueOf(dd));
                    if (onCheckedListener != null) {
                        onCheckedListener.onChange(realPosition, dd + "");
                    }
                    mData = null;
                }
                ((ParamsHolder) viewHolder).et_prop.setInputType(InputType.TYPE_CLASS_NUMBER);
                final TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (((ParamsHolder) viewHolder).et_prop.hasFocus()) {
                            String et = ((ParamsHolder) viewHolder).et_prop.getText().toString();
                            if(data.getDataType().getSpecs() != null) {
                                MetaSpec specs = (MetaSpec) data.getDataType().getSpecs();
                                if(!TextUtils.isEmpty(et) && (Integer.parseInt(et) > Integer.parseInt(specs.getMax()) || Integer.parseInt(et) < Integer.parseInt(specs.getMin()))) {
                                    ((ParamsHolder) viewHolder).tv_tip.setTextColor(Color.parseColor("#ff0000"));
                                } else {
                                    ((ParamsHolder) viewHolder).tv_tip.setTextColor(Color.parseColor("#ADADAD"));
                                }
                                if(!TextUtils.isEmpty(et) && Integer.parseInt(et) >= Integer.parseInt(specs.getMin()) && Integer.parseInt(et) <= Integer.parseInt(specs.getMax()) && onCheckedListener != null) {
                                    onCheckedListener.onChange(realPosition, et);
                                }
                            } else if(!TextUtils.isEmpty(et) && onCheckedListener != null) {
                                    onCheckedListener.onChange(realPosition, et);
                            }
                        }
                    }
                };

                ((ParamsHolder) viewHolder).et_prop.setTag(watcher);
                ((ParamsHolder) viewHolder).et_prop.addTextChangedListener(watcher);
                ((ParamsHolder) viewHolder).et_prop.setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus) {
                        ((ParamsHolder) viewHolder).et_prop.addTextChangedListener(watcher);
                    } else {
                        ((ParamsHolder) viewHolder).et_prop.removeTextChangedListener(watcher);
                    }
                });
            }
        }
    }

    private OnCheckedListener onCheckedListener;

    public void setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
    }

    public interface OnCheckedListener {
        void onSelect(int position, String selectId);

        void onChange(int realPosition, String et);
    }

    class ParamsHolder extends BaseRecyclerAdapter.Holder {
        TextView tv_name;
        RelativeLayout rl_form;
        private EditText et_prop;
        private TextView tv_unit;
        private TextView tv_tip;
        private ImageView iv_select;
        private TextView tv_prop;
        private View view_bottom;
        public ParamsHolder(View itemView) {
            super(itemView);
            tv_prop = itemView.findViewById(R.id.tv_prop);
            tv_name = itemView.findViewById(R.id.tv_name);
            rl_form = itemView.findViewById(R.id.rl_form);
            et_prop = itemView.findViewById(R.id.et_prop);
            tv_unit = itemView.findViewById(R.id.tv_unit);
            tv_tip = itemView.findViewById(R.id.tv_tip);
            iv_select = itemView.findViewById(R.id.iv_select);
            view_bottom = itemView.findViewById(R.id.view_bottom);
        }
    }

    public void setDataByPos(int position, Object data) {
        mData = data;
        mPosition = position;
        notifyItemChanged(position);
    }

    private Object mData;
    private int mPosition;
}
