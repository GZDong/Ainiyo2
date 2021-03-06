package com.huadi.android.ainiyo.entity;

import java.io.Serializable;

/**
 * Created by wgyscsf on 2016/5/26.
 * 邮箱：wgyscsf@163.com
 * 博客：http://blog.csdn.net/wgyscsf
 */
public class ModeComment implements Serializable {
    private String id;
    private String userid;
    private String imgUrl;
    private String content;
    private String time;
    private String replyed;//回复了谁

    public ModeComment(String id, String userid, String imgUrl, String content, String time, String replyed) {
        this.id = id;
        this.userid = userid;
        this.imgUrl = imgUrl;
        this.content = content;
        this.time = time;
        this.replyed = replyed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReplyed() {
        return replyed;
    }

    public void setReplyed(String replyed) {
        this.replyed = replyed;
    }
}
