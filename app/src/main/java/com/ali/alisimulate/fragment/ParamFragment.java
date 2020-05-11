package com.ali.alisimulate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.ParamAdapter;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.view.DropDownPop;
import com.ali.alisimulate.view.TopViewCycle;

import java.util.ArrayList;

public class ParamFragment extends Fragment {
    private RecyclerView rv_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_param, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        DropDownPop dropDownPop = new DropDownPop();
        dropDownPop.init(getActivity());
        rv_list = v.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        ParamAdapter adapter = new ParamAdapter();
        ArrayList<String> list = new ArrayList<>();
        list.add("sss");
        list.add("sss");
        list.add("sss");
        list.add("sss");
        adapter.addDatas(list);
        rv_list.setAdapter(adapter);

        TopViewCycle topViewCycle = new TopViewCycle(getActivity());
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("sss");
        list1.add("sss");
        list1.add("sss");
        list1.add("sss");
        topViewCycle.loadData(list1);
        topViewCycle.setOnPageClickListener(new TopViewCycle.OnPageClickListener() {
            @Override
            public void onClick(int position) {
                ToastUtils.showToast(position + "");
                dropDownPop.showPop(rv_list);
            }
        });
        adapter.setHeadView(topViewCycle);
    }
}
