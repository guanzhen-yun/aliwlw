package com.ali.alisimulate.service;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.entity.RegistModel;
import com.ziroom.net.bean.Result;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Description:AppService
 **/
public interface AppService {
    @Headers({Constants.DOMAIN_ALI_HEADR})
    @POST("/api/M_User")
    Observable<Result> regist(@Body RegistModel regist);

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @POST("/api/login")
    Observable<Result<LoginSuccess>> login(@Body LoginModel loginModel);

    @Headers({Constants.DOMAIN_ALI2_HEADR})
    @GET("/api/device/registerDevice ")
    Observable<Result> getDevices(@Query("productKey") String productKey);
}
