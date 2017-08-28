package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/27.
 */

public class ActivityData {
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public ActivityContent getContent() {
        return Content;
    }

    public void setContent(ActivityContent content) {
        Content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private ActivityContent Content;
    private String Date;
}
