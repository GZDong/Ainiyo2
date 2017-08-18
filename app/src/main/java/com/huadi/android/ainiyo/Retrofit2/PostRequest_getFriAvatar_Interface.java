package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.FriImg;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/16.
 */

public interface PostRequest_getFriAvatar_Interface {
    @POST("getavatar")
    @FormUrlEncoded
    Observable<FriImg> getObservable(@Field("sessionid") String sessionid);
}
