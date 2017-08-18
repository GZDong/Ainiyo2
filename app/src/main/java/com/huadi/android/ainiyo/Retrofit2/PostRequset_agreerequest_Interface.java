package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForAgree;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/17.
 */

public interface PostRequset_agreerequest_Interface {
    @FormUrlEncoded
    @POST("agreerequest")
    Observable<ResultForAgree> getObservable(@Field("sessionid")String sessionid,@Field("friendid") String friendid,@Field("agree")String agree);
}
