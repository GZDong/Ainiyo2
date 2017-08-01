package com.huadi.android.ainiyo.entity;

import android.content.Context;

import com.huadi.android.ainiyo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhidong on 2017/7/28.
 */

public class FriendsLab {

    private static FriendsLab sFriendsLab;

    private Context mContext;

    private List<Friends> mFriendses;

    public static FriendsLab get(Context context){
        if (sFriendsLab == null){
         sFriendsLab = new FriendsLab(context);
        }
        return sFriendsLab;
    }

    private FriendsLab(Context context){
        mContext = context.getApplicationContext();
        initFriends();
    }

    private void initFriends(){
        mFriendses = new ArrayList<>();
        Friends friend1 = new Friends("shouji",R.drawable.examplepicture);
        Friends friend2 = new Friends("xiaoming", R.drawable.user2);
        Friends friend3 = new Friends("long",R.drawable.right_image);
        mFriendses.add(friend1);
        mFriendses.add(friend2);
        mFriendses.add(friend3);
        //到时候再这里修改成从服务器获取好友列表
    }

    public List<Friends> getFriendses() {
        return mFriendses;
    }

    public Friends getFriend(String name){
        for (Friends friends : mFriendses){
            if (friends.getName().equals(name)){
                return friends;
            }
        }
        return null;
    }
}
