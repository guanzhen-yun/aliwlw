package com.ali.alisimulate.activity.adddevice;

import android.content.Intent;
import android.text.TextUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.adapter.LeftTitleAdapter;
import com.ali.alisimulate.adapter.RightDeviceAdapter;
import com.ali.alisimulate.dialog.BottomTwoButtonDialog;
import com.ali.alisimulate.entity.BranchEntity;
import com.ali.alisimulate.entity.BranchTypeEntity;
import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.entity.RegistDeviceRequest;
import com.ali.alisimulate.entity.RegistDeviceResult;
import com.ali.alisimulate.util.AppUtils;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.ali.alisimulate.util.Utils;
import com.google.gson.Gson;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ViewInject(layoutId = R.layout.activity_adddevice)
public class AddDeviceActivity extends BaseActivity<AddDevicePresenter> implements AddDeviceContract.IView {
    @BindView(R.id.rv_left)
    RecyclerView mRvLeft;
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    private BottomTwoButtonDialog dialog;

    private String Companyname;

    private int isCanReturn = 0;//0 未上传 1 上传中  2 上传成功  3 上传失败
    private RegistDeviceResult mResult;

    @Override
    public void fetchIntents() {
        Companyname = getIntent().getStringExtra("Companyname");
    }

    @Override
    public void initDatas() {
        mPresenter.getBranchList();
    }

    private void setDevice(List<BranchTypeEntity> list) {
        RightDeviceAdapter adapter = new RightDeviceAdapter(this, list);
        adapter.setOnCheckedListener(position -> {
            dialog = new BottomTwoButtonDialog(AddDeviceActivity.this);
            dialog.setContent("是否需要创建" + list.get(position).name + "模拟设备?");
            dialog.setOnClickDialogListener(new BottomTwoButtonDialog.OnClickDialogListener() {
                @Override
                public void onClickLeft() {
                    dialog.dismiss();
                }

                @Override
                public void onClickRight() {
                    if(isCanReturn == 0) {
                        registDevice(list.get(position));
                    } else if(isCanReturn == 3){
                        dialog.dismiss();
                    } else if(isCanReturn == 2) {
                        setResult(RESULT_OK, new Intent().putExtra("result", mResult));
                        finish();
                    }
                }
            });
            dialog.show();
        });
        mRvRight.setAdapter(adapter);
    }

    private void registDevice(BranchTypeEntity branchTypeEntity) {
        RegistDeviceRequest registDeviceRequest = new RegistDeviceRequest();
        String str = SharedPreferencesUtils.getStr(this, Constants.KEY_LOGIN_INFO);
        LoginSuccess loginSuccess = new Gson().fromJson(str, LoginSuccess.class);

        if (!TextUtils.isEmpty(Utils.getMac(this))) {
            registDeviceRequest.macAddress = Utils.getMac(this);
        }
        registDeviceRequest.productCompany = Companyname;
        registDeviceRequest.producter = loginSuccess.userDetail.username;
            registDeviceRequest.romVersion = String.valueOf(AppUtils.getVersionCode(AddDeviceActivity.this));
        registDeviceRequest.productLine = "产品线1";
        registDeviceRequest.productKey = branchTypeEntity.id;
//        registDeviceRequest.fittingIds = list;
        registDeviceRequest.deviceComment = branchTypeEntity.name;
        mPresenter.registDevice(registDeviceRequest);
        dialog.setContent("正在创建");
        isCanReturn = 1;
    }

    private void setRecy(List<BranchEntity> list) {
        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        list.get(0).isSelect = true;
        LeftTitleAdapter adapter = new LeftTitleAdapter(list);
        adapter.setOnCheckedListener(new LeftTitleAdapter.OnCheckedListener() {
            @Override
            public void onCheck(int position) {
                String id = null;
                for (int i = 0; i < list.size(); i++) {
                    if (position == i) {
                        list.get(i).isSelect = true;
                        id = list.get(i).id;
                    } else {
                        list.get(i).isSelect = false;
                    }
                }
                adapter.notifyDataSetChanged();
                getBranchTypeList(id);
            }
        });
        mRvLeft.setAdapter(adapter);
        getBranchTypeList(list.get(0).id);
    }

    private void getBranchTypeList(String branchId) {
        mPresenter.getBranchTypeList(branchId);
    }

    @Override
    public AddDevicePresenter getPresenter() {
        return new AddDevicePresenter(this);
    }

    @Override
    public void getBranchListResult(List<BranchEntity> list) {
        setRecy(list);
    }

    @Override
    public void getBranchTypeListResult(List<BranchTypeEntity> list) {
        setDevice(list);
    }

    @Override
    public void registDeviceSuccess(RegistDeviceResult registDeviceResult) {
        if(registDeviceResult != null) {
            mResult = registDeviceResult;
            dialog.setContent("创建成功！");
            isCanReturn = 2;
        } else {
            isCanReturn = 3;
            dialog.setContent("创建失败");
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
