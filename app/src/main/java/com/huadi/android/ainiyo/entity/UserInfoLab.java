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
    private UserInfo mUserInfo;  //指定的用户
    private List<UserInfo> mUserInfos;  //名字相同的用户，没有实际意义，只是满足语法

    public static UserInfoLab get(Context mContext, UserInfo userInfo) {
        if (sUserInfoLab == null){
            sUserInfoLab = new UserInfoLab(mContext, userInfo.getUsername(), userInfo.getPassword(), userInfo.getPicture());
        }
        return sUserInfoLab;
    }


    private UserInfoLab(Context context, String name, String password, int picture) {
        mContext = context.getApplicationContext();
        initUser(name, password, picture);
    }

    private void initUser(String name, String password, int picture) {

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
            UserInfo userInfo = new UserInfo(name, password, picture);
            userInfo.save();
            //重新到数据库里读取
            initUser(name, password, picture);
        }
    }

    public UserInfo getUserInfo(){
        return mUserInfo;
    }
}
