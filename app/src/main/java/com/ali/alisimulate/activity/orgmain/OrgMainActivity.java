package com.ali.alisimulate.activity.orgmain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.adddevice.AddDeviceActivity;
import com.ali.alisimulate.activity.DeviceDetailActivity;
import com.ali.alisimulate.adapter.DeviceListAdapter;
import com.ali.alisimulate.adapter.PopDeviceListAdapter;
import com.ali.alisimulate.dialog.BottomTwoButtonDialog;
import com.ali.alisimulate.dialog.SecondWCodeDialog;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ali.alisimulate.util.LoadMoreOnScrollListener;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(layoutId = R.layout.activity_orgmain)
public class OrgMainActivity extends BaseActivity<OrgMainPresenter> implements OrgMainContract.IView {

    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_companyname)
    TextView tvCompanyname;
    @BindView(R.id.iv_loginout)
    ImageView ivLoginout;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.tv_device)
    TextView tvDevice;
    @BindView(R.id.rl_device)
    RelativeLayout rlDevice;
    @BindView(R.id.rv_device)
    RecyclerView rvDevice;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.iv_addDevice)
    ImageView ivAddDevice;

    private List<OrgDevice.DeviceList> orgDevices = new ArrayList<>();
    private DeviceListAdapter adapter;
    private int page = 0;
    @Override
    public void initDatas() {
        mPresenter.getUserInfo();
//        mPresenter.getDevice("a1yz4fe0qG1");
        getDeviceList(true);//获取全部数据
    }

    @Override
    public void initViews() {
        String strLoginInfo = SharedPreferencesUtils.getStr(this, Constants.KEY_LOGIN_INFO);
        LoginSuccess loginInfo = new Gson().fromJson(strLoginInfo, LoginSuccess.class);
        if (loginInfo != null) {
            tvUsername.setText(loginInfo.userDetail.username);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvDevice.setLayoutManager(linearLayoutManager);
        adapter = new DeviceListAdapter(this, orgDevices);
        rvDevice.setAdapter(adapter);
        rvDevice.addOnScrollListener(new LoadMoreOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage > page) {
                    getDeviceList(false);
                }
            }
        });
    }

    private void getDeviceList(boolean isFirst) {
        if(isFirst) {
            page = 0;
        } else {
            page++;
        }
        mPresenter.getDeviceList(page + 1, 10, "");
    }

    public void adddevice(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("Companyname", tvCompanyname.getText().toString());
        Intent intent = new Intent(OrgMainActivity.this, AddDeviceActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            //刷新设备列表
            getDeviceList(true);
        }
    }

    @Override
    public OrgMainPresenter getPresenter() {
        return new OrgMainPresenter(this);
    }

    @Override
    public void getUserInfoSuccess(UserInfoEntity entity) {
        if (entity != null) {
            tvCompanyname.setText(entity.CompanyName);
        }
    }

    @Override
    public void logoutSuccess() {
        finish();
    }

    @Override
    public void getDeviceListSuccess(OrgDevice orgDevice) {
        if (orgDevices == null && page == 0) {
            llNone.setVisibility(View.VISIBLE);
            rvDevice.setVisibility(View.GONE);
            tvDevice.setText("无");
        } else {
            llNone.setVisibility(View.GONE);
            rvDevice.setVisibility(View.VISIBLE);
            if(page == 0) {
                orgDevices.clear();
            }
            orgDevices.addAll(orgDevice.data);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.rl_device, R.id.iv_loginout})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.rl_device:
                LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup menuView = (ViewGroup) mLayoutInflater.inflate(
                        R.layout.pop_device, null, true);
                PopupWindow pw = new PopupWindow(menuView, RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                pw.setOutsideTouchable(true);
                pw.showAsDropDown(rlDevice);
                RecyclerView rv_device = menuView.findViewById(R.id.rv_device);
                rv_device.setLayoutManager(new LinearLayoutManager(OrgMainActivity.this));
                List<String> list = new ArrayList<>();
                list.add("小米/净水机/WaterPurifier1");
                list.add("小米/净水机/WaterPurifier2");
                list.add("小米/净水机/WaterPurifier3");
                list.add("小米/净水机/WaterPurifier4");
                PopDeviceListAdapter adapter = new PopDeviceListAdapter(list);
                rv_device.setAdapter(adapter);
                adapter.setOnCheckedListener(new PopDeviceListAdapter.OnCheckedListener() {
                    @Override
                    public void onCheck(int position) {
                        tvDevice.setText(list.get(position));
                        pw.dismiss();
                    }
                });
                break;
            case R.id.iv_loginout:
                BottomTwoButtonDialog dialog = new BottomTwoButtonDialog(OrgMainActivity.this);
                dialog.setOnClickDialogListener(new BottomTwoButtonDialog.OnClickDialogListener() {
                    @Override
                    public void onClickLeft() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        mPresenter.logout();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }
}
