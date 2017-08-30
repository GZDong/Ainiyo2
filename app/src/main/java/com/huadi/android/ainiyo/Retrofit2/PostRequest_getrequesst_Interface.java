package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForRqstList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/17.
 */

public interface PostRequest_getrequesst_Interface {

    @POST("getrequest")
    @FormUrlEncoded
    Observable<ResultForRqstList> getObservable(@Field("sessionid") String sessionid,@Field("page") String page,@Field("pagesize") String pagesize);
}
