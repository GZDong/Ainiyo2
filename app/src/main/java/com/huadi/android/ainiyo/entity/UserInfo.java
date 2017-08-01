package com.huadi.android.ainiyo.entity;

/**
 * Created by zhidong on 2017/8/1.
 */

public class UserInfo {

    private String username;
    private String password;
    private int picture;

    public int getPicture() {
        return picture;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
