package com.huadi.android.ainiyo.entity;

/**
 * Created by zhidong on 2017/7/27.
 */

public class Friends {

    private String name;

    private int picture;

    public Friends(String name, int picture){
        this.name = name;
        this.picture = picture;
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
}
