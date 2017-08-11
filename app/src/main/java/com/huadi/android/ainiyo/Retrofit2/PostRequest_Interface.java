package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.FriendGot;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zhidong on 2017/8/10.
 */

public interface PostRequest_Interface {

    @POST("getfriends")
    @FormUrlEncoded
    Call<FriendGot> getCall(@Field("sessionid") String sessionid);
}
