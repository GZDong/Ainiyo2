package com.huadi.android.ainiyo.entity;

/**
 * Created by rxvincent on 2017/8/16.
 */

public class MovementData {
    private int Id;
    private String Data;
    private String Content;
    private boolean Attended;

    public MovementData(int id, String data, String content) {
        Id = id;
        Data = data;
        Content = content;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }


    public String getDate() {
        return Data;
    }

    public void setDate(String date) {
        this.Data = date;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public boolean isAttended() {
        return Attended;
    }

    public void setAttended(boolean attended) {
        Attended = attended;
    }
}
