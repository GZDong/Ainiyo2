package com.huadi.android.ainiyo.entity;

import android.content.Context;

import com.huadi.android.ainiyo.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

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
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation("shouji");
        Friends friend1;
        int test;
        if (conversation == null){
            test =0;
        }else{
            test = conversation.getUnreadMsgCount();
        }
        friend1 = new Friends("shouji",R.drawable.examplepicture,test);

        Friends friend2 = new Friends("xiaoming", R.drawable.user2,3);
        Friends friend3 = new Friends("long",R.drawable.right_image,7);
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

    private void getUnreadMsg(List<Friends> firList){
        for (Friends friends : firList){
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friends.getName());
            friends.setUnreadMeg(conversation.getUnreadMsgCount());
        }
    }

}
