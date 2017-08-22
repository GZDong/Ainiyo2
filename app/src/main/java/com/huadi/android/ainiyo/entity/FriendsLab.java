package com.huadi.android.ainiyo.entity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.huadi.android.ainiyo.Retrofit2.GetRequest_friend_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_getFriAvatar_Interface;
import com.huadi.android.ainiyo.Retrofit2.PostRequest_removefriend_Interface;
import com.huadi.android.ainiyo.application.ECApplication;
import com.huadi.android.ainiyo.gson.FriImg;
import com.huadi.android.ainiyo.gson.FriendGot;
import com.huadi.android.ainiyo.gson.ResultForDeleteFri;
import com.huadi.android.ainiyo.gson.ResultForFriend;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhidong on 2017/7/28.
 */

public class FriendsLab {

    private static FriendsLab sFriendsLab;

    private Context mContext;

    private List<Friends> mFriendses;

    private UserInfo mUserInfo;
    private List<Friends> mmFriendses;

    private List<String> usernames;

    public static FriendsLab get(Context context, UserInfo userInfo) {
        if (sFriendsLab == null){
            sFriendsLab = new FriendsLab(context, userInfo);
        }
        return sFriendsLab;
    }
    public static FriendsLab get(Context context) {
        return sFriendsLab;
    }

    private FriendsLab(Context context, UserInfo userInfo) {
        mContext = context.getApplicationContext();
        String name = userInfo.getUsername();
        String passwd = userInfo.getPassword();
        int picture = userInfo.getPicture();
        mUserInfo = new UserInfo(name,passwd,picture);
       // initFriends(userInfo);
    }

    //根据用户来初始化好友列表
    public void initFriends() {
        mFriendses = new ArrayList<>();
        Log.e("test", "第一次初始化的时候的sessionid: " + ((ECApplication) mContext.getApplicationContext()).sessionId);

        //从数据库中加载用户列的值为指定用户的好友名单
        Log.e("test", "请求数据库时的用户id为：" + mUserInfo.getUsername());
        mFriendses = DataSupport.where("user = ?", mUserInfo.getUsername()).find(Friends.class);

        //加载出来之后，如果名单多于一个，就根据最新消息时间排一下序号
        if (mFriendses.size() > 1) {
            reSort();
        }
        if (mFriendses.size() > 0) {
            Intent intent = new Intent("com.huadi.android.ainiyo.refresh");
            mContext.sendBroadcast(intent);
        }
        //如果数据库里没有好友，就到服务器请求好友列表
        if (mFriendses.size() == 0) {
            //根据userInfo发起网络请求
            //获得数据
            //模拟：假如mmFriendses就是返回的数据
           mmFriendses = new ArrayList<>();

            //**********RxJava + Retrofit*******
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://120.24.168.102:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            GetRequest_friend_Interface request = retrofit.create(GetRequest_friend_Interface.class);
            Log.e("test","在请求好友列表时的sessionid: "+((ECApplication)mContext.getApplicationContext()).sessionId);
            Observable<FriendGot> call = request.getCall(((ECApplication)mContext.getApplicationContext()).sessionId);
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<FriendGot>() {
                        @Override
                        public void onCompleted() {
                            Log.e("test", "onCompleted____请求好友列表完成");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("test", "onError_____请求好友列表异常");
                        }

                        @Override
                        public void onNext(FriendGot friendGot) {
                            Log.e("test", "onNext____请求好友列表");
                            if (friendGot.getStatus().equals("300") ){
                                Log.e("test", "状态码为300 " + friendGot.getMsg());
                                    Toast.makeText(mContext,"请求好友列表成功",Toast.LENGTH_LONG).show();
                                Log.e("test", "返回的Result ：" + friendGot.getFriendList().toString());
                                    if (friendGot.friendList != null && friendGot.friendList.size() > 0){
                                        for (ResultForFriend resultForFriend : friendGot.friendList){
                                            Log.e("test", "好友的id：" + resultForFriend.getName());
                                            Log.e("test", "好友的Url为：" + resultForFriend.getAvatar());
                                            Log.e("test", "此时的用户id为：" + mUserInfo.getUsername());
                                            Friends friends = new Friends(mUserInfo.getUsername(), resultForFriend.getName(), resultForFriend.getAvatar(),resultForFriend.getId());
                                            mmFriendses.add(friends);
                                        }
                                        //获得数据后先获得每个好友的未读信息，后存入数据库
                                        //这里记得初始化一些服务器上没有的数据
                                        Log.e("test", "执行未读数更新");
                                        if (mmFriendses.size() > 0) {
                                            for (Friends friends : mmFriendses) {
                                                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(friends.getName());
                                                int unread;
                                                if (conversation == null) {
                                                    unread = 0;
                                                } else {
                                                    unread = conversation.getUnreadMsgCount();
                                                }
                                                friends.setUnreadMeg(unread);

                                            }

                                            Log.e("test", "执行把数据放进数据库里");
                                            for (Friends friends : mmFriendses) {
                                                friends.save();
                                            }
                                            //如果来自网络的好友列表不为空，重新根据用户初始化聊天列表
                                            initFriends();
                                        } else if (mmFriendses.size() == 0) {
                                            //没有好友
                                            Toast.makeText(mContext, "没有好友", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            }else {
                                Log.e("test", "状态码显示失败，获取好友列表失败");
                                Log.e("test", "Msg 是：" + friendGot.getMsg() + " " + friendGot.getStatus());
                            }
                        }

                    });





        }
    }
    //这个方法是返回聊天列表的
    public List<Friends> getFriendses() {
        List<Friends> reList = new ArrayList<>();

        //在好友列表挑出需要显示在聊天列表的好友
        if (mFriendses!=null) {
            for ( Friends friends : mFriendses){
                if (friends.isShowInChooseFragment() == true){
                    reList.add(friends);
                }
            }
        }
        return reList;

    }

    //这个方法返回所有好友列表
    public List<Friends> getFriendsesAll() {
        return mFriendses;
    }

    //根据好友的名称在好友列表里返回改好友的实例
    public Friends getFriend(String name){
        if (mFriendses.size()>0) {
            for (Friends friends : mFriendses){
                if (friends.getName().equals(name)){
                    return friends;
                }
            }
        }
        return null;
    }

    //这个排序是对单例里的好友列表数据排序
    public void reSort() {
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
        for (Friends friends1 : mFriendses) {
            if (friends1 != null) {
                Log.e("test", "mFriendses 排序后：" + friends1.getName() + "/n");
            }
        }
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


    public void addFriend(Friends friends) {
        //这里还需要向服务器添加

        String user = friends.getUser();
        String name = friends.getName();
        Friends friends1 = new Friends(user,name);
        friends1.save();
        initFriends();
    }

    //设置是否在聊天列表出现
    public void setShow(Friends friends) {
        if (friends.isShowInChooseFragment() == false) {
            for (Friends friends1 : mFriendses) {
                if (friends1.getName().equals(friends.getName())) {
                    friends1.setShowInChooseFragment(true);
                }
            }
        }
    }

    public void setFriListNull(){
        sFriendsLab = null;
        mFriendses = null;
        usernames = null;
        mmFriendses = null;
        mUserInfo = null;
        mContext = null;
    }

    //更新最新显示消息和未读数
    public void updateTimeAndUnread(String ID,String newTime,int unRead){
       for (Friends friends: mFriendses){
           if (friends != null && friends.getName().equals(ID)){
               friends.setNewTime(newTime);
               friends.setUnreadMeg(unRead);
               friends.save();
           }
       }
    }
    //用于点击后消除未读数
    public void clearUnread(String ID){
        for (Friends friends : mFriendses){
            if (friends != null && friends.getName().equals(ID)){
                friends.setUnreadMeg(0);
                friends.save();
            }
        }
    }

    public void addNewDialog(String Id) {
        for (Friends friends : mFriendses) {
            if (friends != null && friends.getName().equals(Id)) {
                friends.setShowInChooseFragment(true);
                friends.save();
            }
        }
    }

    public String findNameById(String id){
        for (Friends friends : mFriendses){
            if (friends.getFriId().equals(id)){
                return friends.getName();
            }
        }
        return null;
    }
    public String findUrlById(String id){
        for (Friends friends : mFriendses){
            if (friends.getFriId().equals(id)){
                return friends.getPicUrl();
            }
        }
        return null;
    }
    //删除会话
    public void deleteDialog(String name){
        for (Friends friends: mFriendses){
            if (friends.getName().equals(name)){
                friends.setShowInChooseFragment(false);
                friends.save();
            }
        }
    }
    //删除好友
    public void deleteFriends(String name){
        //数据库删除数据
        for (Friends friends: mFriendses){
            if (friends.getName().equals(name)){
                Log.e("test","被删除好友的id：" + friends.getFriId());
                DataSupport.deleteAll(Friends.class,"name = ?",name);
                mFriendses.remove(friends);  //单例移除

                //服务器删除数据
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://120.24.168.102:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                PostRequest_removefriend_Interface removefriend_interface = retrofit.create(PostRequest_removefriend_Interface.class);
                Observable<ResultForDeleteFri> observable = removefriend_interface.getObservable(((ECApplication) mContext.getApplicationContext()).sessionId,friends.getFriId());
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResultForDeleteFri>() {
                            @Override
                            public void onCompleted() {
                                Log.e("test","onCompleted___删除好友");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("test","onError___删除好友异常");
                            }

                            @Override
                            public void onNext(ResultForDeleteFri resultForDeleteFri) {
                                Log.e("test","onNext___删除好友 " + resultForDeleteFri.getMsg());
                                if (resultForDeleteFri.getStatus().equals("320")){
                                    Toast.makeText(mContext,"删除好友成功！",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }

    }
}
