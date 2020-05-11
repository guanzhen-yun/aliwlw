package com.ali.alisimulate.service;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.entity.Regist;
import com.ziroom.net.RetrofitManager;
import com.ziroom.net.bean.Result;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Description:AppService
 **/
public interface AppService {
    @Headers({Constants.DOMAIN_ALI_HEADR})
    @POST("/api/M_User")
    Observable<Result> regist(@Body Regist regist);
}
