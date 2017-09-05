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
import com.huadi.android.ainiyo.util.CONST;
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
    private UserInfo userInfo;

    public static UserInfoLab get(Context mContext, String name,String password) {
        if (sUserInfoLab == null){
            sUserInfoLab = new UserInfoLab(mContext, name, password);
        }
        return sUserInfoLab;
    }
    public static UserInfoLab get(Context mContext,UserInfo userInfo) {
        if (sUserInfoLab == null){
            sUserInfoLab = new UserInfoLab(mContext, userInfo.getUsername(), userInfo.getPassword());
        }
        return sUserInfoLab;
    }

    public static UserInfoLab get(Context context) {
        //sUserInfoLab.mContext = context.getApplicationContext();
        return sUserInfoLab;
    }


    private UserInfoLab(Context context, String name, String password) {
        mContext = context.getApplicationContext();
        initUser(name, password);
    }


    private void initUser(final String name,final String password) {

        //找出已经保留在数据库里的用户
        mUserInfos = DataSupport.where("username = ?", name).find(UserInfo.class);
        Log.e("test","请求数据库完毕");
        if (mUserInfos!=null) {
            Log.e("test", "用户数据库数据不为空" + mUserInfos.toString());
            for (UserInfo userInfo : mUserInfos) {
                if (userInfo !=null) {
                    if (userInfo.getUsername().equals(name)) {
                        Log.e("test","用户的姓名是" + userInfo.getUsername());
                        mUserInfo = userInfo;
                        break;
                    }
                }
            }
          //  Log.e("test","用户的姓名是" +mUserInfo.getUsername());
        }
        //数据库里没有该用户,则储存到数据库里

        if (mUserInfo ==null) {
            mUserInfo = new UserInfo(name,password);
            mUserInfo.save();
            Log.e("test", "数据库没有该用户");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CONST.NEW_HOST)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostRequest_getuserinfo_Interface getuserinfoInterface = retrofit.create(PostRequest_getuserinfo_Interface.class);
            Log.e("test", "请求头像时的sessionid: " +  ((ECApplication)mContext.getApplicationContext()).sessionId);
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
                            Log.e("test", "onNext____获取用户id：" + resultForUserInfo.getResult().getUserid());
                            Log.e("test", "onNext____获取用户性别：" + resultForUserInfo.getResult().getGentle());
                            Log.e("test", "onNext____获取用户性别：" + resultForUserInfo.getResult().getBirthday());
                            Log.e("test", "onNext____获取用户签名：" + resultForUserInfo.getResult().getAutograph());
                            Log.e("test", "onNext____获取用户地区：" + resultForUserInfo.getResult().getArea());
                            Log.e("test", "onNext____获取用户电话：" + resultForUserInfo.getResult().getPhone());
                            Log.e("test", "onNext____获取用户地区名字：" + resultForUserInfo.getResult().getAreaName());

                            mUserInfo.setPicUrl(resultForUserInfo.getResult().getAvatar());
                            mUserInfo.setId(resultForUserInfo.getResult().getUserid());
                            mUserInfo.setSex(resultForUserInfo.getResult().getGentle());
                            mUserInfo.setBirthday(resultForUserInfo.getResult().getBirthday());
                            mUserInfo.setSign(resultForUserInfo.getResult().getAutograph());
                            mUserInfo.setArea(resultForUserInfo.getResult().getArea());
                            mUserInfo.setPhone(resultForUserInfo.getResult().getPhone());
                            mUserInfo.setAreaName(resultForUserInfo.getResult().getAreaName());
                            mUserInfo.save();
                            //重新到数据库里读取
                           initUser(name, password);
                        }
                    });
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

    public void clearUserInfo(){
        mUserInfo = null;
        sUserInfoLab = null;
        mUserInfos = null;
        userInfo = null;
    }

    public void refreshSessionid(String sessionid){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CONST.NEW_HOST)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PostRequest_getuserinfo_Interface getuserinfoInterface = retrofit.create(PostRequest_getuserinfo_Interface.class);
        Log.e("test", "请求头像时的sessionid: " +  sessionid);
        Observable<ResultForUserInfo> observable = getuserinfoInterface.getObservable(sessionid);
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
                        Log.e("test", "onNext____获取用户id：" + resultForUserInfo.getResult().getUserid());
                        Log.e("test", "onNext____获取用户性别：" + resultForUserInfo.getResult().getGentle());
                        Log.e("test", "onNext____获取用户性别：" + resultForUserInfo.getResult().getBirthday());
                        Log.e("test", "onNext____获取用户签名：" + resultForUserInfo.getResult().getAutograph());
                        Log.e("test", "onNext____获取用户地区：" + resultForUserInfo.getResult().getArea());
                        Log.e("test", "onNext____获取用户电话：" + resultForUserInfo.getResult().getPhone());
                        Log.e("test", "onNext____获取用户地区名字：" + resultForUserInfo.getResult().getAreaName());

                        mUserInfo.setPicUrl(resultForUserInfo.getResult().getAvatar());
                        mUserInfo.setId(resultForUserInfo.getResult().getUserid());
                        mUserInfo.setSex(resultForUserInfo.getResult().getGentle());
                        mUserInfo.setBirthday(resultForUserInfo.getResult().getBirthday());
                        mUserInfo.setSign(resultForUserInfo.getResult().getAutograph());
                        mUserInfo.setArea(resultForUserInfo.getResult().getArea());
                        mUserInfo.setPhone(resultForUserInfo.getResult().getPhone());
                        mUserInfo.setAreaName(resultForUserInfo.getResult().getAreaName());
                        mUserInfo.save();
                    }
                });
    }

    public boolean IsCurrentUser(String id){
        if (mUserInfo!=null){
            if (mUserInfo.getId().equals(id)){
                return true;
            }
        }
        return false;
    }
}
