package com.ali.alisimulate.service;

import com.ali.alisimulate.Constants;
import com.ali.alisimulate.entity.BranchEntity;
import com.ali.alisimulate.entity.BranchTypeEntity;
import com.ali.alisimulate.entity.FittingDetailEntity;
import com.ali.alisimulate.entity.FittingResetDetailEntity;
import com.ali.alisimulate.entity.LoginModel;
import com.ali.alisimulate.entity.LoginSuccess;
import com.ali.alisimulate.entity.OrgDevice;
import com.ali.alisimulate.entity.RegistDeviceRequest;
import com.ali.alisimulate.entity.RegistDeviceResult;
import com.ali.alisimulate.entity.RegistModel;
import com.ali.alisimulate.entity.UserInfoEntity;
import com.ziroom.net.bean.Result;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/M_User/{id}")
    Observable<Result<UserInfoEntity>> getUserInfo(@Path("id") String id);

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/logout")
    Observable<Result> logout();

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/brand/list")
    Observable<Result<List<BranchEntity>>> getBranchList();

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/brand/type_list")
    Observable<Result<List<BranchTypeEntity>>> getBranchTypeList(@Query("id") String id);

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @POST("/api/device/registerDevice")
    Observable<Result<RegistDeviceResult>> registerDevice (@Body RegistDeviceRequest request);

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/device/page_by_productkey")
    Observable<Result<OrgDevice>> getDeviceList (@QueryMap HashMap<String, Object> map);


    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/device/fixing_detail")
    Observable<Result<FittingDetailEntity>> getFitingInfo (@Query("device_name") String device_name);

    @Headers({Constants.DOMAIN_ALI_HEADR})
    @GET("/api/device/sync_state")
    Observable<Result<FittingResetDetailEntity>> reset (@Query("device_name") String device_name);
}
