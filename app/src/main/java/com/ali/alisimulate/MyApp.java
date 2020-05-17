package com.ali.alisimulate;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.ali.alisimulate.config.ConfigManager;
import com.ali.alisimulate.config.LogUtils;
import com.ali.alisimulate.config.ParamsInterceptor;
import com.ali.alisimulate.config.SSLSocketClient;
import com.ali.alisimulate.util.IDemoCallback;
import com.ali.alisimulate.util.InitManager;
import com.ali.alisimulate.util.SharedPreferencesUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.dm.model.ResponseModel;
import com.aliyun.alink.h2.api.HLog;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.channel.core.persistent.PersistentNet;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.id2.Id2ItlsSdk;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.ziroom.net.RetrofitManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApp extends Application {
    private static final String TAG = "MyApp";
    private static MyApp app;
    public static Context mAppContext = null;

    /**
     * 判断是否初始化完成
     * 未初始化完成，所有和云端的长链通信都不通
     */
    public static boolean isInitDone = false;
    public String productKey = null, deviceName = null, deviceSecret = null, productSecret = null;
    private InitManager initManager;
    public HashMap<String, Boolean> mapInit = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        MultiDex.install(this);
        initAli();
        RetrofitManager.initClient(getOkhttpClient(), ConfigManager.getInstance().getHost());
        initNetInfo();
        initOkhttpUtils();
    }

    public static MyApp getApp() {
        return app;
    }

    /**
     * 创建OkHttpClient
     */
    private static OkHttpClient getOkhttpClient() {
        HttpLoggingInterceptor.Logger logger =
                (String message) -> LogUtils.d("OkHttp_Log", message);
        OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(
                        new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)))
                .addInterceptor(new ParamsInterceptor());

        if (BuildConfig.DEBUG) {
            builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory());
            builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
        }
        return builder.build();
    }

    private void initOkhttpUtils() {
        //okhttp
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager).build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 初始化域名信息
     */
    public static void initNetInfo() {
        RetrofitUrlManager.getInstance().putDomain(Constants.DOMAIN_ALI_KEY, ConfigManager.getInstance().getHost());
        RetrofitUrlManager.getInstance().putDomain(Constants.DOMAIN_ALI2_KEY, ConfigManager.getInstance().getHost2());
    }

    public void regist(String mDeviceName, String mproductKey, String mdeviceSecret) {
        deviceName = mDeviceName;
        deviceSecret = mdeviceSecret;
        productKey = mproductKey;
        ALog.i(TAG, "sdk version = " + LinkKit.getInstance().getSDKVersion());
        if (TextUtils.isEmpty(deviceSecret) && !TextUtils.isEmpty(productSecret)) {
            initManager.registerDevice(this, productKey, deviceName, productSecret, new IConnectSendListener() {
                @Override
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    Log.d(TAG, "registerDevice onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? "null" : aResponse.data) + "]");
                    if (aResponse != null && aResponse.data != null) {
                        // 解析云端返回的数据
                        ResponseModel<Map<String, String>> response = JSONObject.parseObject(aResponse.data.toString(),
                                new TypeReference<ResponseModel<Map<String, String>>>() {
                                }.getType());
                        if ("200".equals(response.code) && response.data != null && response.data.containsKey("deviceSecret") &&
                                !TextUtils.isEmpty(response.data.get("deviceSecret"))) {
                            /**
                             * ds必须保存在非应用目录，确保卸载之后ds仍然可以读取到。
                             * 以下代码仅供测试验证使用
                             */
                            deviceSecret = response.data.get("deviceSecret");
                            // getDeviceSecret success, to build connection.
                            // 持久化 deviceSecret 初始化建联的时候需要
                            // 用户需要按照实际场景持久化设备的三元组信息，用于后续的连接
                            SharedPreferences preferences = getSharedPreferences("deviceAuthInfo", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("deviceId", productKey+deviceName);
                            editor.putString("deviceSecret", deviceSecret);
                            //提交当前数据
                            editor.apply();
                            connect();
                        }
                    }
                }

                @Override
                public void onFailure(ARequest aRequest, AError aError) {
                    Log.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + aError + "]");
                }
            });
        } else if (!TextUtils.isEmpty(deviceSecret)){
            connect();
        }
    }

    private void initAli() {
        initManager = InitManager.getInstance();
        MqttConfigure.itlsLogLevel = Id2ItlsSdk.DEBUGLEVEL_NODEBUG;
        HLog.setLogLevel(Log.ERROR);
        PersistentNet.getInstance().openLog(true);
        ALog.setLevel(ALog.LEVEL_DEBUG);
        // 设置心跳时间，默认65秒
        MqttConfigure.setKeepAliveInterval(65);

        mAppContext = getApplicationContext();
    }

    /**
     * 初始化建联
     * 如果初始化建联失败，需要用户重试去完成初始化，并确保初始化成功。如应用启动的时候无网络，导致失败，可以在网络可以的时候再次执行初始化，成功之后不需要再次执行。
     * 初始化成功之后，如果因为网络原因连接断开了，用户不需要执行初始化建联操作，SDK会处理建联。
     *
     * onError 初始化失败
     * onInitDone 初始化成功
     *
     * SDK 支持以userName+password+clientId 的方式登录（不推荐，建议使用三元组建联）
     * 设置如下参数，InitManager.init的时候 deviceSecret, productSecret 可以不填
     * MqttConfigure.mqttUserName = username;
     * MqttConfigure.mqttPassWord = password;
     * MqttConfigure.mqttClientId = clientId;
     *
     */
    public void connect() {
        Log.d(TAG, "connect() called");
        // SDK初始化

        initManager.init(this, productKey, deviceName, deviceSecret, productSecret, new IDemoCallback() {

            @Override
            public void onError(AError aError) {
                Log.d(TAG, "onError() called with: aError = [" + aError + "]");
                Log.d(TAG,Log.getStackTraceString(new Throwable()));
                // 初始化失败，初始化失败之后需要用户负责重新初始化
                // 如一开始网络不通导致初始化失败，后续网络回复之后需要重新初始化
//                showToast("初始化失败");
                SharedPreferencesUtils.save(MyApp.getApp(),  Constants.KEY_CONNECT_STATUS, "");
            }

            @Override
            public void onInitDone(Object data) {
                Log.d(TAG, "onInitDone() called with: data = [" + data + "]");
//                showToast("初始化成功");
                isInitDone = true;
                mapInit.put(deviceName, true);
                SharedPreferencesUtils.save(MyApp.getApp(),  Constants.KEY_CONNECT_STATUS, deviceName);
            }
        });

    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void unregistConnectAli() {
        initManager.unregistInit();
    }
}
