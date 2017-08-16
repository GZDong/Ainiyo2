package com.huadi.android.ainiyo.Retrofit2;

import com.huadi.android.ainiyo.gson.ResultForLogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zhidong on 2017/8/12.
 */

public interface PostRequest_login_Interface {

    @FormUrlEncoded
    @POST("login")
    Observable<ResultForLogin> getCall(@Field("name") String name, @Field("pwd") String pwd);
}
