package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForCheckName;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/12.
 */

public interface GetRequest_checkName_Interface {
    @POST("checkName")
    @FormUrlEncoded
    Observable<ResultForCheckName> getCall(@Field("sessionid") String sessionid, @Field("name") String name);
}
