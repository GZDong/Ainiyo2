package com.huadi.android.ainiyo.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by zhidong on 2017/7/27.
 */

public class Friends extends DataSupport{

    private String user;

    private String name;

    private int picture;

    private int unreadMeg = 0;  //该好友的未读信息数

    private String newTime;  //最新聊天时间

    private String letters;  //首写字母

    private boolean showInChooseFragment;  //是否出现在聊天列表

    public Friends(){

    }

    //创建一个该用户的好友
    public Friends(String user,String name, int picture,int unread,String time,boolean show){
        this.user = user;
        this.name = name;
        this.picture = picture;
        this.unreadMeg = unread;
        this.newTime = time;
        this.showInChooseFragment = show;
    }

    public void setNewTime(String newTime) {
        this.newTime = newTime;
    }

    public String getNewTime() {
        return newTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public int getUnreadMeg() {
        return unreadMeg;
    }

    public void setUnreadMeg(int unreadMeg) {
        this.unreadMeg = unreadMeg;
    }

    public boolean isShowInChooseFragment() {
        return showInChooseFragment;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getLetters() {
        return letters;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}
