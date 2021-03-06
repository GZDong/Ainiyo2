package com.huadi.android.ainiyo.entity;

import java.io.Serializable;

/**
 * Created by fengsam on 17-8-13.
 */

public class ModeLocalData implements Serializable {

    private int Id;
    private int Userid;
    private ModeInfo mi;
    private String Date;
    private int CommentNum;

    public ModeLocalData(int id, int userid, ModeInfo mi, String date, int commentNum) {
        Id = id;
        Userid = userid;
        this.mi = mi;
        Date = date;
        CommentNum = commentNum;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public ModeInfo getMi() {
        return mi;
    }

    public void setMi(ModeInfo mi) {
        this.mi = mi;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getCommentNum() {
        return CommentNum;
    }

    public void setCommentNum(int commentNum) {
        CommentNum = commentNum;
    }
}
