package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForFriAlbums;
import com.huadi.android.ainiyo.gson.ResultForFriUrl;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/9/1.
 */

public interface PostRequest_getfriendsalbums_next_Interface {
    @POST("getfriendalbum")
    @FormUrlEncoded
    Observable<ResultForFriUrl> getObservable(@Field("sessionid") String sessionid, @Field("alumb") String alumb, @Field("friendid") String friendid);
}
