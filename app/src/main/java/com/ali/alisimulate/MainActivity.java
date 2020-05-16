package com.ali.alisimulate;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.alisimulate.activity.DeviceDetailActivity;
import com.ali.alisimulate.activity.login.LoginActivity;
import com.ali.alisimulate.activity.orgmain.OrgMainActivity;
import com.ali.alisimulate.activity.regist.RegistActivity;
import com.ali.alisimulate.entity.KeyValue;
import com.ali.alisimulate.util.ToastUtils;
import com.ali.alisimulate.util.UserUtils;
import com.ali.alisimulate.view.MyWeelPop;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_login;
    private TextView tv_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<KeyValue> list_channel = new ArrayList<>();
        MyWeelPop mWindowChannel = new MyWeelPop(this, list_channel, "选择频道");
        mWindowChannel.setAnimationStyle(R.style.AnimationUP);
        mWindowChannel.showAtLocation(tv_login,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        tv_login = findViewById(R.id.tv_login);
        tv_regist = findViewById(R.id.tv_regist);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(UserUtils.getToken(this))) {
            tv_login.setVisibility(View.GONE);
            tv_regist.setVisibility(View.GONE);
        } else {
            tv_login.setVisibility(View.VISIBLE);
        }
    }

    public void jump(View view) {
        if (!checkReady()) {
            return;
        }
        if (LinkKit.getInstance().getDeviceThing() == null) {
            ToastUtils.showToast("物模型功能未启用");
            return;
        }
        startActivity(new Intent(MainActivity.this, DeviceDetailActivity.class));
    }

    private boolean checkReady() {
//        if (MyApp.userDevInfoError) {
//            ToastUtils.showToast("设备三元组信息res/raw/deviceinfo格式错误");
//            return false;
//        }
        if (!MyApp.isInitDone) {
            ToastUtils.showToast("初始化尚未成功，请稍后点击");
            return false;
        }
        return true;
    }

    public void tologin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void jumporg(View view) {
        if(TextUtils.isEmpty(UserUtils.getToken(this))) {
            ToastUtils.showToast("請先登錄");
        } else {
            startActivity(new Intent(MainActivity.this, OrgMainActivity.class));
        }
    }

    public void scan(View view) {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
         * 也可以不传这个参数
         * 不传的话  默认都为默认不震动  其他都为true
         * */

        //ZxingConfig config = new ZxingConfig();
        //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
        //config.setPlayBeep(true);//是否播放提示音
        //config.setShake(true);//是否震动
        //config.setShowAlbum(true);//是否显示相册
        //config.setShowFlashLight(true);//是否显示闪光灯
        //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == 11 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                ToastUtils.showToast("扫描结果为：" + content);
            }
        }
    }

    public void regist(View view) {
        startActivity(new Intent(MainActivity.this, RegistActivity.class));
    }
}
