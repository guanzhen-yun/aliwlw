package com.ali.alisimulate.activity.regist;

import android.widget.EditText;

import com.ali.alisimulate.R;
import com.ali.alisimulate.entity.RegistModel;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.util.Utils;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:RegistActivity
 **/
@ViewInject(layoutId = R.layout.activity_test)
public class RegistActivity extends BaseActivity<RegistPresenter> implements RegistContract.IView {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_orgname)
    EditText etOrgname;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_gender)
    EditText etGender;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_service)
    EditText etService;

    @OnClick(R.id.btn_regist)
    public void onViewClicked() {
        RegistModel model = new RegistModel();
        model.CompanyName = Utils.getStr(etCompany);
        model.EmailAddress = Utils.getStr(etEmail);
        model.Gender = (!"女".equals(Utils.getStr(etCompany)));
        model.Id = Utils.getStr(etId);
        model.Name = Utils.getStr(etName);
        model.OrgName = Utils.getStr(etOrgname);
        model.PSW = Utils.getStr(etPwd);
        model.PhoneNumber = Utils.getStr(etNumber);
        model.ServiceType = Utils.getStr(etService);
        mPresenter.sendMvpRequest(model);
    }

    @Override
    public RegistPresenter getPresenter() {
        return new RegistPresenter(this);
    }

    @Override
    public void getResult() {
        ToastUtils.showToast("注册成功,去登录吧");
        finish();
    }
}
