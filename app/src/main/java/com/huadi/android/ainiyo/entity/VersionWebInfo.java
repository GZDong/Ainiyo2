package com.huadi.android.ainiyo.entity;

import android.provider.ContactsContract;

/**
 * Created by fengsam on 17-8-30.
 */

public class VersionWebInfo {
    private int Id;
    private int Editon;
    private String Title;
    private String Updatecontent;
    private String Url;
    private String Date;

    public VersionWebInfo(int id, int editon, String title, String updatecontent, String url, String date) {
        Id = id;
        Editon = editon;
        Title = title;
        Updatecontent = updatecontent;
        Url = url;
        Date = date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getEditon() {
        return Editon;
    }

    public void setEditon(int editon) {
        Editon = editon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUpdatecontent() {
        return Updatecontent;
    }

    public void setUpdatecontent(String updatecontent) {
        Updatecontent = updatecontent;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
