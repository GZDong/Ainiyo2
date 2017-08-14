package com.huadi.android.ainiyo.entity;

import android.provider.ContactsContract;

/**
 * Created by fengsam on 17-8-5.
 */

public class ModeWebData {
    private int Id;
    private int Userid;
    private String Content;
    private String Date;

    public ModeWebData(int id, int userid, String content, String date) {
        Id = id;
        Userid = userid;
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

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String data) {
        Date = data;
    }
}
