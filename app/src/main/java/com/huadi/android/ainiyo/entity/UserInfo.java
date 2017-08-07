package com.huadi.android.ainiyo.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhidong on 2017/8/1.
 */

public class UserInfo extends DataSupport implements Serializable {

    private String username;
    private String password;
    private int picture;


    public UserInfo(String username, String password, int picture) {
        this.username = username;
        this.password = password;
        this.picture = picture;
    }

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
