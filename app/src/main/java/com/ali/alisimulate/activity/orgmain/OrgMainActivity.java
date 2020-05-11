package com.ali.alisimulate.activity.orgmain;

import android.content.Intent;
import android.os.Handler;
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

import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.AddDeviceActivity;
import com.ali.alisimulate.activity.DeviceDetailActivity;
import com.ali.alisimulate.adapter.PopDeviceListAdapter;
import com.ali.alisimulate.dialog.BottomTwoButtonDialog;
import com.ali.alisimulate.dialog.SecondWCodeDialog;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.rb_net)
    RadioButton rbNet;
    @BindView(R.id.rb_unnet)
    RadioButton rbUnnet;
    @BindView(R.id.rg_net)
    RadioGroup rgNet;
    @BindView(R.id.tv_devicename)
    TextView tvDevicename;
    @BindView(R.id.tv_devicekey)
    TextView tvDevicekey;
    @BindView(R.id.tv_alias)
    TextView tvAlias;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.rl_body)
    RelativeLayout rlBody;
    @BindView(R.id.iv_addDevice)
    ImageView ivAddDevice;

    @Override
    public void initDatas() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getDevice("a1DyFgDz1iX");
            }
        });
    }

    @Override
    public void initViews() {
        rlBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrgMainActivity.this, DeviceDetailActivity.class));
            }
        });
        ivLoginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomTwoButtonDialog dialog = new BottomTwoButtonDialog(OrgMainActivity.this);
                dialog.setOnClickDialogListener(new BottomTwoButtonDialog.OnClickDialogListener() {
                    @Override
                    public void onClickLeft() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickRight() {
                        //TODO 退出
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondWCodeDialog dialog = new SecondWCodeDialog(OrgMainActivity.this);
                dialog.show();
            }
        });

        rlDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
    }

    public void adddevice(View view) {
        startActivityForResult(new Intent(OrgMainActivity.this, AddDeviceActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            //刷新设备列表
        }
    }

    @Override
    public OrgMainPresenter getPresenter() {
        return new OrgMainPresenter(this);
    }

    @Override
    public void getDeviceResult() {

    }
}
