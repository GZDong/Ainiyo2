package com.huadi.android.ainiyo.entity;

/**
 * Created by zhidong on 2017/7/27.
 */

public class Friends {

    private String name;

    private int picture;

    private int unreadMeg = 0;

    private String newTime;

    public Friends(String name, int picture,int unread,String time){
        this.name = name;
        this.picture = picture;
        this.unreadMeg = unread;
        this.newTime = time;
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
}
