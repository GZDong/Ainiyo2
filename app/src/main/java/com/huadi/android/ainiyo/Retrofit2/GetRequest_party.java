package com.huadi.android.ainiyo.Retrofit2;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by rxvincent on 2017/8/14.
 */

public interface GetRequest_party {
    @GET("party/{id}")
    Call<ResponseBody> getPartyInformation(@Path("id") String id);
}
