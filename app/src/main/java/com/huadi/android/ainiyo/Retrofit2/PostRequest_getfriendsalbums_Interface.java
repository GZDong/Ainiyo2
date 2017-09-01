package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForFriAlbums;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/9/1.
 */

public interface PostRequest_getfriendsalbums_Interface {
    @POST("getfriendsalbums")
    @FormUrlEncoded
    Observable<ResultForFriAlbums> getObservable(@Field("sessionid") String sessionid,@Field("friendid") String friendid);
}
