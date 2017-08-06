package com.huadi.android.ainiyo.entity;

import android.content.Context;

import com.huadi.android.ainiyo.R;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

/**
 * Created by zhidong on 2017/8/1.
 */

public class UserInfoLab {

    private static UserInfoLab sUserInfoLab;
    private Context mContext;
    private  UserInfo mUserInfo;
    private List<UserInfo> mUserInfos;

    public static UserInfoLab get(Context mContext){
        if (sUserInfoLab == null){
            sUserInfoLab = new UserInfoLab(mContext);
        }
        return sUserInfoLab;
    }


    private UserInfoLab(Context context){
        mContext = context.getApplicationContext();
        //initUser();
    }

    public void initUser(String name,String password,int picture){
        mUserInfos = DataSupport.where("username = ?",name).find(UserInfo.class);
        for (UserInfo userInfo: mUserInfos){
            if (userInfo.getUsername().equals(name)){
                mUserInfo = userInfo;
                break;
            }
        }
        if (mUserInfo == null){
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(name);
            userInfo.setPassword(password);
            userInfo.setPicture(picture);
            userInfo.save();
            mUserInfo=userInfo;
        }
        /*mUserInfo.setUsername("xuniji");
        mUserInfo.setPassword("123");
        mUserInfo.setPicture(R.drawable.left_image);*/
    }

    public UserInfo getUserInfo(){
        return mUserInfo;
    }
}
