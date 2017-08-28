package com.huadi.android.ainiyo.entity;

import com.huadi.android.ainiyo.R;
import com.huadi.android.ainiyo.util.DateUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by zhidong on 2017/7/27.
 */

public class Friends extends DataSupport {

    private String user;   //拥有该好友的用户姓名

    private String name;

    private int picture;    //在没有上传图片情况下的默认图片资源

    private String picUrl;   //服务器上的Url

    private int unreadMeg = 0;  //该好友的未读信息数

    private String newTime;  //最新聊天时间

    private String letters;  //首写字母

    private boolean showInChooseFragment;//是否出现在聊天列表

    private String friId;  //好友在服务器上的id，不同于姓名

    private int flag_delete = 0;   //是否被删除的标记


    public Friends(String user, String name) {
        this.user = user;
        this.name = name;
        this.picture = R.drawable.gagaki;
        this.unreadMeg = 0;
        this.newTime = DateUtil.getNowDate();
        this.showInChooseFragment = true;
        this.picUrl = null;
        this.friId = null;
    }

    public Friends(String user, String name, String picUrl,String friId) {
          this.user = user;
        this.name = name;
        this.picture = R.drawable.gagaki;
        this.unreadMeg = 0;
        this.newTime = DateUtil.getNowDate();
        this.showInChooseFragment = true;
        this.picUrl = picUrl;
        this.friId = friId;
    }

    public Friends(String user, String name, int picture, int unread, String time, boolean show) {
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

    public void setShowInChooseFragment(boolean result) {
        this.showInChooseFragment = result;
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

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getFriId() {
        return friId;
    }

    public void setFriId(String friId) {
        this.friId = friId;
    }

    public void setFlag_delete(int flag_delete) {
        this.flag_delete = flag_delete;
    }

    public int getFlag_delete() {
        return flag_delete;
    }
}
