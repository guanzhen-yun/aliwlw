package com.ali.alisimulate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;

public class ParamAdapter extends BaseRecyclerAdapter<String> {

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_param, parent, false);
        return new ParamsHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, String data) {
        if (viewHolder instanceof ParamsHolder) {
            ((ParamsHolder) viewHolder).text.setText(data);
        }
    }

    class ParamsHolder extends BaseRecyclerAdapter.Holder {
        TextView text;

        public ParamsHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
