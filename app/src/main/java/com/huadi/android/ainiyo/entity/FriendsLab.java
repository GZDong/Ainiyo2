package com.huadi.android.ainiyo.entity;

import android.content.Context;
import android.widget.Toast;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.DateUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.litepal.crud.DataSupport;

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

    private List<Friends> mFriendses;

    public static FriendsLab get(Context context,UserInfo userInfo){
        if (sFriendsLab == null){
         sFriendsLab = new FriendsLab(context,userInfo);
        }
        return sFriendsLab;
    }

    private FriendsLab(Context context,UserInfo userInfo){
        mContext = context.getApplicationContext();
        initFriends(userInfo);
    }

    //根据用户来初始化好友列表
    private void initFriends(UserInfo userInfo){
        mFriendses = new ArrayList<>();

        //从数据库中加载用户列的值为指定用户的好友名单
        mFriendses = DataSupport.where("user = ?",userInfo.getUsername()).find(Friends.class);

        //加载出来之后，如果名单多于一个，就根据最新消息时间排一下序号
        if (mFriendses.size() > 1){
            reSort();
        }
        //如果数据库里没有好友，就到服务器请求好友列表
        if (mFriendses.size() == 0){
            //根据userInfo发起网络请求
            //获得数据
            //模拟：假如mmFriendses就是返回的数据
            List<Friends> mmFriendses = new ArrayList<>();
            //测试：
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation("shouji");
            int unread;
            if (conversation == null){
                unread =0;
            }else{
                unread = conversation.getUnreadMsgCount();
            }
            //
            Friends friend1 = new Friends(userInfo.getUsername() ,"shouji",R.drawable.examplepicture,unread, DateUtil.getNowDate(),true);
            Friends friend2 = new Friends(userInfo.getUsername() ,"xiaoming",R.drawable.user2,3, DateUtil.getNowDate(),true);
            Friends friend3 = new Friends(userInfo.getUsername() ,"aong",R.drawable.right_image,7,"2017-08-03 16:40:59",true);
            Friends friend4 = new Friends(userInfo.getUsername() ,"bong",R.drawable.right_image,2,"2017-08-02 16:40:59",true);
            Friends friend5 = new Friends(userInfo.getUsername() ,"cong",R.drawable.right_image,1,"2017-08-03 16:41:59",false);
            Friends friend6 = new Friends(userInfo.getUsername() ,"dong",R.drawable.right_image,3,"2017-08-03 16:40:59",true);
            Friends friend7 = new Friends(userInfo.getUsername() ,"eong",R.drawable.right_image,0,"2017-08-03 16:43:59",false);
            Friends friend8 = new Friends(userInfo.getUsername() ,"fong",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
            Friends friend9 = new Friends(userInfo.getUsername() ,"gong",R.drawable.right_image,0,"2017-08-03 16:45:59",false);
            Friends friend10 = new Friends(userInfo.getUsername() ,"hong",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
            Friends friend11 = new Friends(userInfo.getUsername() ,"冯庆星",R.drawable.right_image,0,"2017-08-03 16:46:59",true);
            Friends friend12 = new Friends(userInfo.getUsername() ,"陈华琳",R.drawable.right_image,0,"2017-08-03 16:40:48",true);
            Friends friend13 = new Friends(userInfo.getUsername() ,"杨小兴",R.drawable.right_image,0,"2017-08-05 16:13:00",true);
            Friends friend14 = new Friends(userInfo.getUsername() ,"黄嘉豪",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
            Friends friend15 = new Friends(userInfo.getUsername() ,"黄铭熙",R.drawable.right_image,0,"2017-08-03 16:40:59",false);
            Friends friend16 = new Friends(userInfo.getUsername() ,"郑文辉",R.drawable.right_image,0,"2017-08-03 16:40:59",true);

            mmFriendses.add(friend3);
            mmFriendses.add(friend2);
            mmFriendses.add(friend4);
            mmFriendses.add(friend5);
            mmFriendses.add(friend6);
            mmFriendses.add(friend7);
            mmFriendses.add(friend8);
            mmFriendses.add(friend9);
            mmFriendses.add(friend10);
            mmFriendses.add(friend11);
            mmFriendses.add(friend12);
            mmFriendses.add(friend13);
            mmFriendses.add(friend14);
            mmFriendses.add(friend15);
            mmFriendses.add(friend16);
            mmFriendses.add(friend1);

            //获得数据后先获得每个好友的未读信息，后存入数据库
            //这里记得初始化一些服务器上没有的数据
            /* for (Friends friends : mmFriendses){
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friends.getName());
                int unread;
                if (conversation == null){
                    unread =0;
                }else{
                    unread = conversation.getUnreadMsgCount();
                }
                friends.setUnreadMeg(unread);

            }*/
            if (mmFriendses.size()>0) {
                for (Friends friends :mmFriendses){
                    friends.save();
                }
                //如果来自网络的好友列表不为空，重新根据用户初始化聊天列表
                initFriends(userInfo);
            }
        }
    }
    //这个方法是返回聊天列表的
    public List<Friends> getFriendses() {
        List<Friends> reList = new ArrayList<>();

        //在好友列表挑出需要显示在聊天列表的好友
        for (Friends friends : mFriendses){
            if (friends.isShowInChooseFragment() == true){
                reList.add(friends);
            }
        }
        return reList;

    }

    //这个方法返回所有好友列表
    private List<Friends> getFriendsesAll(){
        return mFriendses;
    }

    //根据好友的名称在好友列表里返回改好友的实例
    public Friends getFriend(String name){
        for (Friends friends : mFriendses){
            if (friends.getName().equals(name)){
                return friends;
            }
        }
        return null;
    }

    //这个排序是对单例里的好友列表数据排序
    public void reSort(){
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
    }

    //返回好友列表的姓名集
    public String[] getNames(){
        int count = mFriendses.size();
        String[] nameA = new String[count];
        for (int i =0; i < count;i++ ){
            nameA[i] = mFriendses.get(i).getName();
        }
        return nameA;
    }


    public void addFriend(Friends friends){

        int flag = 0;
        for (Friends friends1 : mFriendses){
            if (friends1.getName().equals(friends.getName())){
                Toast.makeText(mContext,"已经是好友",Toast.LENGTH_LONG).show();
                flag =1;
            }
        }
        if (flag == 0 ){
            //请求网路
            //判断有没有这个用户，如果有就向服务器申请添加该好友
            //有的话提示用户已经发送请求了
            Toast.makeText(mContext,"好友请求已经发送",Toast.LENGTH_LONG).show();
        }
    }

}
