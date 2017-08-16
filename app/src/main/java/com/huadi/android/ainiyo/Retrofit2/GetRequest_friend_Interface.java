package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.FriendGot;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/10.
 */

public interface GetRequest_friend_Interface {

    @POST("getfriends")
    @FormUrlEncoded
    Observable<FriendGot> getCall(@Field("sessionid") String sessionid);
}
