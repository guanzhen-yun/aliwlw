package com.ali.alisimulate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ParamAdapter extends BaseRecyclerAdapter<String> {

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_param, parent, false);
        return new ParamsHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, String data) {
        if (viewHolder instanceof ParamsHolder) {
            ((ParamsHolder) viewHolder).tv_name.setText(data);
            ((ParamsHolder) viewHolder).rl_form.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater mLayoutInflater = (LayoutInflater) view.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                            R.layout.pop_device, null, true);
                    PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    pw.setOutsideTouchable(true);
                    pw.showAsDropDown(((ParamsHolder) viewHolder).rl_form);
                    RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                    rv_device.setLayoutManager(new LinearLayoutManager( view.getContext()));
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
            });
        }
    }

    class ParamsHolder extends BaseRecyclerAdapter.Holder {
        TextView tv_name;
        RelativeLayout rl_form;

        public ParamsHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            rl_form = itemView.findViewById(R.id.rl_form);
        }
    }
}
