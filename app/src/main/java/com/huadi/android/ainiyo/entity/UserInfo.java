package com.huadi.android.ainiyo.entity;

import com.huadi.android.ainiyo.R;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhidong on 2017/8/1.
 */

public class UserInfo extends DataSupport implements Serializable {

    private String username;
    private String password;
    private int picture;
    private String picUrl;
    private String Id;

    private String sign;
    private String sex;
    private int area;
    private String phone;
    private String birthday;
    private String areaName;

    public UserInfo(String username, String password) {
        this.username = username;
        this.password = password;
        this.picture = R.drawable.left_image;
        this.sign = null;
        this.sex = null;
        this.area = 0;
        this.phone = null;
        this.birthday = null;
        this.areaName = null;
    }
    public UserInfo(String username, String password,String url,String Id) {
        this.username = username;
        this.password = password;
        this.picture = R.drawable.left_image;
        this.picUrl = url;
        this.Id = Id;
        this.sign = null;
        this.sex = null;
        this.area = 0;
        this.phone = null;
        this.birthday = null;
        this.areaName = null;
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

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getArea() {
        return area;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }
}
