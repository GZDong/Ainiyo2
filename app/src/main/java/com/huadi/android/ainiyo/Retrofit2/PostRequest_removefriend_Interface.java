package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForDeleteFri;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/22.
 */

public interface PostRequest_removefriend_Interface {

    @FormUrlEncoded
    @POST("removefriend")
    Observable<ResultForDeleteFri> getObservable(@Field("sessionid") String sessionid,@Field("friendid")String friendid);
}
