package com.example.listenercollection.retrofit;

import com.example.listenercollection.retrofit.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyService {

    @POST("APP/APPHandler.ashx")
    @FormUrlEncoded
    Observable<BaseResponse> bindDevices(@Field("action") String action, @Field("userid") String userid, @Field("serialno") String serialno);

    @POST("APP/APPHandler.ashx")
    @FormUrlEncoded
    Observable<BaseResponse> unbindDevices(@Field("action") String action, @Field("userid") String userid, @Field("serialno") String serialno);

    @POST("APP/APPHandler.ashx")
    @FormUrlEncoded
    Observable<BaseResponse> uploadIncome(@Field("action") String action,
                                          @Field("userid") String userid,
                                          @Field("paymodel") String paymodel,
                                          @Field("number") String number,
                                          @Field("time") String time);
}
