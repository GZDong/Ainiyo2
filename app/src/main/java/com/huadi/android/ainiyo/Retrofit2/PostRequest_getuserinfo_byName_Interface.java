package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForUserInfo;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/30.
 */

public interface PostRequest_getuserinfo_byName_Interface {
    @FormUrlEncoded
    @POST("getuserinfo")
    Observable<ResultForUserInfo> getObservable(@Field("sessionid") String sessionid,@Field("name") String name);
}
