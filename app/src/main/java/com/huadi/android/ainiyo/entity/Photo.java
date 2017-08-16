package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/14.
 */

public class Photo {
    private int Id;
    private int Userid;
    private String Picname;
    public void setUserid(int userid){Userid=userid;}
    public void setId(int id){Id=id;}
    public void setPicname(String picname){Picname=picname;}
    public int getId(){return Id;}
    public int getUserid(){return Userid;}
    public String getPicname(){return Picname;}
}
