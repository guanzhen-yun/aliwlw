package com.ali.alisimulate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ali.alisimulate.activity.login.LoginActivity;
import com.ali.alisimulate.activity.orgmain.OrgMainActivity;
import com.ali.alisimulate.util.UserUtils;
import com.ziroom.base.BaseActivity;
import com.ziroom.base.ViewInject;

/**
 * Author:关震
 * Date:2020/5/17 7:07
 * Description:SplashActivity
 **/
@ViewInject(layoutId = R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    @Override
    public void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int check = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int check2 = checkSelfPermission(Manifest.permission.CAMERA);
            if (check == PackageManager.PERMISSION_GRANTED && check2 == PackageManager.PERMISSION_GRANTED) {
                jump();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
            }
        }
    }

    private void jump() {
        if (!TextUtils.isEmpty(UserUtils.getToken(this))) {
            startActivity(new Intent(SplashActivity.this, OrgMainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isValid = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                isValid = false;
                Toast.makeText(this, "请开启相应权限", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if(isValid) {
            jump();
        }
    }
}
