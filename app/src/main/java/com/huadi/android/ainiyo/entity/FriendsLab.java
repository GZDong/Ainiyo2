package com.huadi.android.ainiyo.entity;

import android.content.Context;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.DateUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhidong on 2017/7/28.
 */

public class FriendsLab {

    private static FriendsLab sFriendsLab;

    private Context mContext;

    private static List<Friends> mFriendses;



    public static FriendsLab get(Context context){
        if (sFriendsLab == null){
         sFriendsLab = new FriendsLab(context);
        }
        return sFriendsLab;
    }

    private FriendsLab(Context context){
        mContext = context.getApplicationContext();
        initFriends();
        reSort();
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
        friend1 = new Friends("shouji",R.drawable.examplepicture,test, DateUtil.getNowDate(),true);

        Friends friend2 = new Friends("xiaoming", R.drawable.user2,3, DateUtil.getNowDate(),true);
        Friends friend3 = new Friends("aong",R.drawable.right_image,7,"2017-08-03 16:40:59",true);
        Friends friend4 = new Friends("bong",R.drawable.right_image,2,"2017-08-02 16:40:59",true);
        Friends friend5 = new Friends("cong",R.drawable.right_image,1,"2017-08-03 16:41:59",false);
        Friends friend6 = new Friends("dong",R.drawable.right_image,3,"2017-08-03 16:40:59",true);
        Friends friend7 = new Friends("eong",R.drawable.right_image,0,"2017-08-03 16:43:59",false);
        Friends friend8 = new Friends("fong",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
        Friends friend9 = new Friends("gong",R.drawable.right_image,0,"2017-08-03 16:45:59",false);
        Friends friend10 = new Friends("hong",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
        Friends friend11 = new Friends("冯庆星",R.drawable.right_image,0,"2017-08-03 16:46:59",true);
        Friends friend12 = new Friends("陈华琳",R.drawable.right_image,0,"2017-08-03 16:40:48",true);
        Friends friend13 = new Friends("杨小兴",R.drawable.right_image,0,"2017-08-05 16:13:00",true);
        Friends friend14 = new Friends("黄嘉豪",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
        Friends friend15 = new Friends("黄铭熙",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
        Friends friend16 = new Friends("郑文辉",R.drawable.right_image,0,"2017-08-03 16:40:59",true);
        //if showInChooseFragment == true 才添加
        mFriendses.add(friend3);
        mFriendses.add(friend2);
        mFriendses.add(friend4);
        mFriendses.add(friend5);
        mFriendses.add(friend6);
        mFriendses.add(friend7);
        mFriendses.add(friend8);
        mFriendses.add(friend9);
        mFriendses.add(friend10);
        mFriendses.add(friend11);
        mFriendses.add(friend12);
        mFriendses.add(friend13);
        mFriendses.add(friend14);
        mFriendses.add(friend15);
        mFriendses.add(friend16);
        mFriendses.add(friend1);



        //到时候再这里修改成从服务器获取好友列表
    }

    public List<Friends> getFriendses() {
        List<Friends> reList = new ArrayList<>();
        for (Friends friends : mFriendses){
            if (friends.isShowInChooseFragment() == true){
                reList.add(friends);
            }
        }
        return reList;

    }

    public List<Friends> getFriendsesAll(){
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

    public void getUnreadMsg(List<Friends> firList){
        for (Friends friends : firList){
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friends.getName());
            friends.setUnreadMeg(conversation.getUnreadMsgCount());
        }
    }

    public static void reSort(){
        Collections.sort(mFriendses, new Comparator<Friends>() {
            @Override
            public int compare(Friends friends, Friends t1) {
                if (friends.getNewTime().compareTo(t1.getNewTime())>0){
                    return -1;
                }
                if (friends.getNewTime().compareTo(t1.getNewTime())==0){
                    return 0;
                }
                return 1;
            }
        });
        System.out.println("排序后："+ mFriendses);
    }

    public String[] getNames(){
        int count = mFriendses.size();
        String[] nameA = new String[count];
        for (int i =0; i < count;i++ ){
            nameA[i] = mFriendses.get(i).getName();
        }
        return nameA;
    }

}
