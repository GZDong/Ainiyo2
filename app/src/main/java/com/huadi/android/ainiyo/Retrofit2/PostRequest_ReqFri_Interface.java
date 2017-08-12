package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForRequset;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zhidong on 2017/8/11.
 */

public interface PostRequest_ReqFri_Interface {

    @POST("addfriend")
    @FormUrlEncoded
    Call<ResultForRequset> getCall(@Field("sessionid") String sessionid,@Field("friendid") String friendid,@Field("attach") String attach);
}
