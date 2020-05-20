package com.ali.alisimulate.activity.orgmain;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.MyApp;
import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.DeviceDetailActivity;
import com.ali.alisimulate.activity.adddevice.AddDeviceActivity;
import com.ali.alisimulate.activity.login.LoginActivity;
import com.ali.alisimulate.adapter.DeviceListAdapter;
import com.ali.alisimulate.dialog.BottomTwoButtonDialog;
import com.ali.alisimulate.entity.BranchEntity;
import com.ali.alisimulate.entity.BranchTypeEntity;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.entity.SelectOrgEntity;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ali.alisimulate.util.LoadMoreOnScrollListener;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.view.DropDownOrgSelect;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.google.gson.Gson;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    private List<OrgDevice.DeviceList> orgDevices = new ArrayList<>();
    private DeviceListAdapter adapter;
    private int page = 0;
    private DropDownOrgSelect dropDownOrgSelect;
    private List<BranchEntity> mBranchList;

    private BranchEntity mSelectBranch;
    private List<SelectOrgEntity> listFirst;

    private int currentPosition = 0;
    private List<BranchTypeEntity> mBranchTypeList;

    private List<SelectOrgEntity> listSecond = new ArrayList<>();//1 配件  2.空气净化器 3净水器
    private Map<String, List<BranchTypeEntity>> listMap = new HashMap<>();

    private SelectOrgEntity mSelectBranchType;

    private List<BranchTypeEntity> branchTypeEntities;//产品列表

    private List<SelectOrgEntity> listThird = new ArrayList<>();

    private SelectOrgEntity mSelectProduct;

    private int mCheckPosition = -1;

    @Override
    public void initDatas() {
        mPresenter.getUserInfo();
        getDeviceList(true);//获取全部数据
        mPresenter.getBranchList();
    }

    @Override
    public void initViews() {
        SharedPreferencesUtils.save(MyApp.getApp(),  Constants.KEY_CONNECT_STATUS, "");
        dropDownOrgSelect = new DropDownOrgSelect();
        dropDownOrgSelect.init(this);
        dropDownOrgSelect.setOnSelectListener(new DropDownOrgSelect.onSelectListener() {
            @Override
            public void onSelect(int position, int current) {
                currentPosition = current;
                if (current == 0) {
                    mSelectBranch = mBranchList.get(position);
                    mPresenter.getBranchTypeList(mSelectBranch.id);
                    tvDevice.setText(mSelectBranch.name);
                    ivDelete.setVisibility(View.VISIBLE);
                } else if (current == 1) {
                    mSelectBranchType = listSecond.get(position);
                    tvDevice.setText(mSelectBranch.name + "/" + mSelectBranchType.name);
                    ivDelete.setVisibility(View.VISIBLE);
                    String name = mSelectBranchType.name;
                    if (name.equals("配件")) {
                        branchTypeEntities = listMap.get("1");
                    } else if (name.equals("空气净化器")) {
                        branchTypeEntities = listMap.get("2");
                    } else if (name.equals("净水器")) {
                        branchTypeEntities = listMap.get("3");
                    }

                    listThird.clear();
                    if (branchTypeEntities != null) {
                        for (BranchTypeEntity branchTypeEntity : branchTypeEntities) {
                            SelectOrgEntity entity = new SelectOrgEntity();
                            entity.name = branchTypeEntity.name;
                            entity.id = branchTypeEntity.id;
                            entity.isSelect = false;
                            listThird.add(entity);
                        }
                    }
                    currentPosition++;
                    dropDownOrgSelect.setList(listThird, currentPosition);
                    dropDownOrgSelect.showPop(rlDevice);
                } else if (current == 2) {
                    mSelectProduct = listThird.get(position);
                    tvDevice.setText(mSelectBranch.name + "/" + mSelectBranchType.name + "/" + mSelectProduct.name);
                    ivDelete.setVisibility(View.VISIBLE);
                    getDeviceList(true);
                }
            }

            @Override
            public void onChangeTo(int current) {
                currentPosition = current;
                if (current == 0) {
                    dropDownOrgSelect.setList(listFirst, currentPosition);
                    for (SelectOrgEntity entity : listFirst) {
                        if(entity.id.equals(mSelectBranch.id)) {
                            entity.isSelect = true;
                        } else {
                            entity.isSelect = false;
                        }
                    }
                    dropDownOrgSelect.showPop(rlDevice);
                    tvDevice.setText(mSelectBranch.name);
                    ivDelete.setVisibility(View.VISIBLE);
                } else if(current == 1) {
                    dropDownOrgSelect.setList(listSecond, currentPosition);
                    for (SelectOrgEntity entity : listSecond) {
                        if(entity.name.equals(mSelectBranchType.name)) {
                            entity.isSelect = true;
                        } else {
                            entity.isSelect = false;
                        }
                    }
                    dropDownOrgSelect.showPop(rlDevice);
                    tvDevice.setText(mSelectBranch.name + "/" + mSelectBranchType.name);
                    ivDelete.setVisibility(View.VISIBLE);
                } else if(current == 2) {
                    dropDownOrgSelect.setList(listThird, currentPosition);
                    for (SelectOrgEntity entity : listThird) {
                        if(entity.id.equals(mSelectProduct.id)) {
                            entity.isSelect = true;
                        } else {
                            entity.isSelect = false;
                        }
                    }
                    dropDownOrgSelect.showPop(rlDevice);
                    tvDevice.setText(mSelectBranch.name + "/" + mSelectBranchType.name + "/" + mSelectProduct.name);
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
        });
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

        adapter.setOnCheckListener(new DeviceListAdapter.OnCheckListener() {
            @Override
            public void onCheck(int position, boolean isCheck) {
                if(!isCheck) {
                    if(rvDevice.isComputingLayout()) {
                        rvDevice.post(new Runnable() {
                            @Override
                            public void run() {
                                orgDevices.get(position).isCheck = false;
                                adapter.notifyItemChanged(position);
                                mCheckPosition = -1;
                            }
                        });
                    } else {
                        orgDevices.get(position).isCheck = false;
                        adapter.notifyItemChanged(position);
                        mCheckPosition = -1;
                    }
                } else {
                        if(rvDevice.isComputingLayout()) {
                            rvDevice.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(mCheckPosition != -1) {
                                        if(mCheckPosition < orgDevices.size()) {
                                            orgDevices.get(mCheckPosition).isCheck = false;
                                        }
                                        adapter.notifyItemChanged(mCheckPosition);
                                    }
                                    orgDevices.get(position).isCheck = true;
                                    adapter.notifyItemChanged(position);
                                    mCheckPosition = position;
                                }
                            });
                        } else {
                            if(mCheckPosition != -1) {
                                if(mCheckPosition < orgDevices.size()) {
                                    orgDevices.get(mCheckPosition).isCheck = false;
                                }
                                adapter.notifyItemChanged(mCheckPosition);
                            }
                            orgDevices.get(position).isCheck = true;
                            adapter.notifyItemChanged(position);
                            mCheckPosition = position;
                    }
                }
            }
        });

        adapter.setOnSelectListener(new DeviceListAdapter.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                OrgDevice.DeviceList deviceList = orgDevices.get(position);
                if("配件".equals(adapter.getModelStr(deviceList.deviceModel))) {
                    return;
                }
                if(!deviceList.isCheck) {
                    ToastUtils.showToast("请先联网");
                    return;
                }
                if (LinkKit.getInstance().getDeviceThing() == null) {
//                    ToastUtils.showToast("物模型功能未启用");
//                    return;
                    MyApp.getApp().regist(deviceList.deviceName, deviceList.productKey, deviceList.deviceSecret, new MyApp.OnConnectListener() {
                        @Override
                        public void onConnect() {
                            Bundle bundle = new Bundle();
                            bundle.putString("productKey", deviceList.productKey);
                            bundle.putString("deviceName", deviceList.deviceName);
                            bundle.putString("deviceComment", deviceList.deviceComment);
                            bundle.putString("deviceSecret", deviceList.deviceSecret);
                            bundle.putString("deviceId", deviceList.deviceId);
                            bundle.putString("title", adapter.getModelStr(deviceList.deviceModel));
                            Intent intent = new Intent(OrgMainActivity.this, DeviceDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    if(!TextUtils.isEmpty(deviceList.deviceName) && !deviceList.deviceName.equals(SharedPreferencesUtils.getStr(MyApp.getApp(), Constants.KEY_CONNECT_STATUS))) {
                        MyApp.getApp().unregistConnectAli();
                        MyApp.getApp().regist(deviceList.deviceName, deviceList.productKey, deviceList.deviceSecret, new MyApp.OnConnectListener() {
                            @Override
                            public void onConnect() {
                                Bundle bundle = new Bundle();
                                bundle.putString("productKey", deviceList.productKey);
                                bundle.putString("deviceName", deviceList.deviceName);
                                bundle.putString("deviceComment", deviceList.deviceComment);
                                bundle.putString("deviceSecret", deviceList.deviceSecret);
                                bundle.putString("deviceId", deviceList.deviceId);
                                bundle.putString("title", adapter.getModelStr(deviceList.deviceModel));
                                Intent intent = new Intent(OrgMainActivity.this, DeviceDetailActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("productKey", deviceList.productKey);
                        bundle.putString("deviceName", deviceList.deviceName);
                        bundle.putString("deviceComment", deviceList.deviceComment);
                        bundle.putString("deviceSecret", deviceList.deviceSecret);
                        bundle.putString("deviceId", deviceList.deviceId);
                        bundle.putString("title", adapter.getModelStr(deviceList.deviceModel));
                        Intent intent = new Intent(OrgMainActivity.this, DeviceDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void getDeviceList(boolean isFirst) {
        if (isFirst) {
            page = 0;
        } else {
            page++;
        }
        String productKey = "";
        if (mSelectProduct != null) {
            productKey = mSelectProduct.id;
        }
        mPresenter.getDeviceList(page + 1, 10, productKey);
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
        startActivity(new Intent(OrgMainActivity.this, LoginActivity.class));
    }

    @Override
    public void getDeviceListSuccess(OrgDevice orgDevice) {
        if (orgDevice == null && page == 0) {
            llNone.setVisibility(View.VISIBLE);
            rvDevice.setVisibility(View.GONE);
        } else {
            llNone.setVisibility(View.GONE);
            rvDevice.setVisibility(View.VISIBLE);
            if (page == 0) {
                orgDevices.clear();
            }
            orgDevices.addAll(orgDevice.data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getBranchListResult(List<BranchEntity> list) {
        mBranchList = list;
        listFirst = new ArrayList<>();
        for (BranchEntity branchEntity : mBranchList) {
            SelectOrgEntity entity = new SelectOrgEntity();
            entity.name = branchEntity.name;
            entity.id = branchEntity.id;
            listFirst.add(entity);
        }
    }

    @Override
    public void getBranchTypeListResult(List<BranchTypeEntity> list) {
        listSecond.clear();
        listMap.clear();
        mBranchTypeList = list;

        for (BranchTypeEntity branchTypeEntity : mBranchTypeList) {
            String type = branchTypeEntity.type;
            if (listMap.containsKey(type)) {
                List<BranchTypeEntity> branchTypeEntities = listMap.get(type);
                branchTypeEntities.add(branchTypeEntity);
                listMap.put(type, branchTypeEntities);
            } else {
                List<BranchTypeEntity> branchTypeEntities = new ArrayList<>();
                branchTypeEntities.add(branchTypeEntity);
                listMap.put(type, branchTypeEntities);
            }
        }

        if (listMap.containsKey("1")) {
            SelectOrgEntity entity = new SelectOrgEntity();
            entity.name = "配件";
            listSecond.add(entity);
        }
        if (listMap.containsKey("2")) {
            SelectOrgEntity entity = new SelectOrgEntity();
            entity.name = "空气净化器";
            listSecond.add(entity);
        }
        if (listMap.containsKey("3")) {
            SelectOrgEntity entity = new SelectOrgEntity();
            entity.name = "净水器";
            listSecond.add(entity);
        }
        dropDownOrgSelect.setList(listSecond, currentPosition + 1);
        dropDownOrgSelect.showPop(rlDevice);
    }

    @OnClick({R.id.rl_device, R.id.iv_loginout, R.id.iv_delete})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                ivDelete.setVisibility(View.GONE);
                tvDevice.setText("");
                mSelectProduct = null;
                getDeviceList(true);//获取全部数据
                break;
            case R.id.rl_device:
                if (currentPosition == 0 && mBranchList != null) {
                    dropDownOrgSelect.setList(listFirst, currentPosition);
                }
                dropDownOrgSelect.showPop(rlDevice);
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
