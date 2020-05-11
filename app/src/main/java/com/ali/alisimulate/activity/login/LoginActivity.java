package com.ali.alisimulate.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.orgmain.OrgMainActivity;
import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.util.Utils;
import com.google.gson.Gson;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
@ViewInject(layoutId = R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.IView {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    public void loginResult(LoginSuccess loginSuccess) {
        SharedPreferences sharedPreferences = getSharedPreferences("aliyun", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("loginInfo", new Gson().toJson(loginSuccess));
        edit.apply();
        ToastUtils.showToast("登录成功");
        startActivity(new Intent(LoginActivity.this, OrgMainActivity.class));
    }

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter(this);
    }

    @OnClick({R.id.tv_login, R.id.iv_back})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                LoginModel loginModel = new LoginModel();
                loginModel.username = Utils.getStr(etName);
                loginModel.password = Utils.getStr(etPassword);
                mPresenter.login(loginModel);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
