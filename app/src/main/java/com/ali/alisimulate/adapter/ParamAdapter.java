package com.ali.alisimulate.adapter;

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
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

import java.util.ArrayList;
import java.util.List;

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
            ((ParamsHolder) viewHolder).tv_name.setText(data.getName());
            if (TmpConstant.TYPE_VALUE_ENUM.equals(data.getDataType().getType())) {
                ((ParamsHolder) viewHolder).et_prop.setEnabled(false);
                ValueWrapper propertyValue = LinkKit.getInstance().getDeviceThing().getPropertyValue(data.getIdentifier());
               if(propertyValue != null) {
                   Integer value = ((ValueWrapper.EnumValueWrapper) propertyValue).getValue();
                   ((ParamsHolder) viewHolder).et_prop.setText(value);
               }
            } else if (TmpConstant.TYPE_VALUE_FLOAT.equals(data.getDataType().getType())) {

            }else if (TmpConstant.TYPE_VALUE_DOUBLE.equals(data.getDataType().getType())) {

            }else if (TmpConstant.TYPE_VALUE_INTEGER.equals(data.getDataType().getType())) {

            }

            ((ParamsHolder) viewHolder).rl_form.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TmpConstant.TYPE_VALUE_ENUM.equals(data.getDataType().getType())) {
                        LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                                R.layout.pop_device, null, true);
                        PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        pw.setOutsideTouchable(true);
                        pw.showAsDropDown(((ParamsHolder) viewHolder).rl_form);
                        RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                        rv_device.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
                                ((ParamsHolder) viewHolder).tv_name.setText(list.get(pos));
                                pw.dismiss();
                            }
                        });
                    }
                }
            });
        }
    }

    class ParamsHolder extends BaseRecyclerAdapter.Holder {
        TextView tv_name;
        RelativeLayout rl_form;
        private EditText et_prop;
        private TextView tv_unit;
        private TextView tv_tip;
        private ImageView iv_select;

        public ParamsHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            rl_form = itemView.findViewById(R.id.rl_form);
            et_prop = itemView.findViewById(R.id.et_prop);
            tv_unit = itemView.findViewById(R.id.tv_unit);
            tv_tip = itemView.findViewById(R.id.tv_tip);
            iv_select = itemView.findViewById(R.id.iv_select);
        }
    }
}
