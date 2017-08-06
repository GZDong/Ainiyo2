package com.huadi.android.ainiyo.entity;

import android.provider.ContactsContract;

/**
 * Created by fengsam on 17-8-5.
 */

public class ModeWebData {
    private int Id;
    private int Userid;
    private String Content;
    private String Data;

    public ModeWebData(int id, int userid, String content, String data) {
        Id = id;
        Userid = userid;
        Content = content;
        Data = data;
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

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
