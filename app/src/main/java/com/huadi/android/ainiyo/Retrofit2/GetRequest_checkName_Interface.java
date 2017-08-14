package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForCheckName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhidong on 2017/8/12.
 */

public interface GetRequest_checkName_Interface {
    @GET("checkName")
    Call<ResultForCheckName> getCall(@Query("sessionid") String sessionid,@Query("name") String name);
}
