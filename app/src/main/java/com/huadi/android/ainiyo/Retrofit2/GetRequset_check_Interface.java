package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForCheck;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by zhidong on 2017/8/11.
 */

public interface GetRequset_check_Interface {

    @GET("checklogin")
    Call<ResultForCheck> getCall(@Query("sessionid") String sessionid);
}
