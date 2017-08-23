package com.huadi.android.ainiyo.entity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getuserinfo_Interface;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.gson.ResultForUserInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.huadi.android.ainiyo.application.ECApplication.sessionId;

/**
 * Created by zhidong on 2017/8/1.
 */

public class UserInfoLab {

    private static UserInfoLab sUserInfoLab;

    private Context mContext;
    private UserInfo mUserInfo;  //指定的用户
    private List<UserInfo> mUserInfos;  //名字相同的用户，没有实际意义，只是满足语法

    public static UserInfoLab get(Context mContext, UserInfo userInfo) {
        if (sUserInfoLab == null){
            sUserInfoLab = new UserInfoLab(mContext, userInfo.getUsername(), userInfo.getPassword());
        }
        return sUserInfoLab;
    }

    public static UserInfoLab get(Context context) {
        sUserInfoLab.mContext = context.getApplicationContext();

        return sUserInfoLab;
    }


    private UserInfoLab(Context context, String name, String password) {
        mContext = context.getApplicationContext();
        initUser(name, password);
    }

    private void initUser(String name, String password) {

        //找出已经保留在数据库里的用户
        mUserInfos = DataSupport.where("username = ?", name).find(UserInfo.class);
        for (UserInfo userInfo : mUserInfos) {
            if (userInfo.getUsername().equals(name)) {
                mUserInfo = userInfo;
                break;
            }
        }
        //数据库里没有该用户,则储存到数据库里
        if (mUserInfo == null) {
            final UserInfo userInfo = new UserInfo(name, password);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.24.168.102:8080/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostRequest_getuserinfo_Interface getuserinfoInterface = retrofit.create(PostRequest_getuserinfo_Interface.class);
            Observable<ResultForUserInfo> observable = getuserinfoInterface.getObservable(((ECApplication)mContext.getApplicationContext()).sessionId);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResultForUserInfo>() {
                        @Override
                        public void onCompleted() {
                            Log.e("test", "onCompleted: "+" 请求用户头像结束" );
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("test", "onError: "+" 请求用户头像异常" );
                        }

                        @Override
                        public void onNext(ResultForUserInfo resultForUserInfo) {
                            Log.e("test","onNext____获取用户头像URL: " + resultForUserInfo.getResult().getAvatar());
                            userInfo.setPicUrl(resultForUserInfo.getResult().getAvatar());
                        }
                    });
            userInfo.save();
            //重新到数据库里读取
            initUser(name, password);
        }
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public UserInfo getUserInfo(){
        return mUserInfo;
    }

    public void updateUserUrl(String url){
        mUserInfo.setPicUrl(url);
        mUserInfo.save();
    }
}
