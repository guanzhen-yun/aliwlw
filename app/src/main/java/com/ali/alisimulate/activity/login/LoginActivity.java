package com.ali.alisimulate.activity.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.R;
import com.ali.alisimulate.activity.orgmain.OrgMainActivity;
import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.util.SharedPreferencesUtils;
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
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_tip)
    TextView tvTip;


    @Override
    public void initViews() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isCanLogin()) {
                    tvLogin.setBackgroundResource(R.drawable.bg_login_buton_canenable);
                } else {
                    tvLogin.setBackgroundResource(R.drawable.bg_login_buton_notenable);
                }
                String name = etName.getText().toString();
                if(!TextUtils.isEmpty(name) && name.length() < 2) {
                    tvTip.setVisibility(View.VISIBLE);
                } else {
                    tvTip.setVisibility(View.GONE);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isCanLogin()) {
                    tvLogin.setBackgroundResource(R.drawable.bg_login_buton_canenable);
                } else {
                    tvLogin.setBackgroundResource(R.drawable.bg_login_buton_notenable);
                }
            }
        });
    }

    private boolean isCanLogin() {
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password);
    }

    @Override
    public void loginResult(LoginSuccess loginSuccess) {
        SharedPreferencesUtils.save(this, Constants.KEY_LOGIN_INFO, new Gson().toJson(loginSuccess));
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
//                loginModel.username = "cuichen";
//                loginModel.password = "123456a";
                if (isCanLogin()) {
                    LoginModel loginModel = new LoginModel();
                    loginModel.username = Utils.getStr(etName);
                    loginModel.password = Utils.getStr(etPassword);
                    mPresenter.login(loginModel);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
