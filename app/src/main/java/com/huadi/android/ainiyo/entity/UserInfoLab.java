package com.huadi.android.ainiyo.entity;

import android.content.Context;

import com.huadi.android.ainiyo.R;

/**
 * Created by zhidong on 2017/8/1.
 */

public class UserInfoLab {

    private static UserInfoLab sUserInfoLab;
    private Context mContext;
    private UserInfo mUserInfo;

    public static UserInfoLab get(Context mContext){
        if (sUserInfoLab == null){
            sUserInfoLab = new UserInfoLab(mContext);
        }
        return sUserInfoLab;
    }


    private UserInfoLab(Context context){
        mContext = context.getApplicationContext();
        initUser();
    }

    private void initUser(){
        mUserInfo = new UserInfo();
        mUserInfo.setUsername("xuniji");
        mUserInfo.setPassword("123");
        mUserInfo.setPicture(R.drawable.left_image);

    }

    public UserInfo getUserInfo(){
        return mUserInfo;
    }
}
