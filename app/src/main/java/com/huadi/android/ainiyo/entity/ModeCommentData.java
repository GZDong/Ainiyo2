package com.huadi.android.ainiyo.entity;

/**
 * Created by fengsam on 17-8-15.
 */

public class ModeCommentData {
    private int Id;
    private int Userid;
    private Boolean Type;
    private int Targetid;
    private String Content;
    private String Date;

    public ModeCommentData(int id, int userid, Boolean type, int targetid, String content, String date) {
        Id = id;
        Userid = userid;
        Type = type;
        Targetid = targetid;
        Content = content;
        Date = date;
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

    public Boolean getType() {
        return Type;
    }

    public void setType(Boolean type) {
        Type = type;
    }

    public int getTargetid() {
        return Targetid;
    }

    public void setTargetid(int targetid) {
        Targetid = targetid;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
