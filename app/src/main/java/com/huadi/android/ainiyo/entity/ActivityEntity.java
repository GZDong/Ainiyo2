package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/28.
 */

public class ActivityEntity {
    private String Title;

    public String getTitle() {
        return Title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getDate() {
        return Date;
    }

    private String ImageUrl;
    private String Date;

    public void ActivityEntity(String Title,String ImageUrl,String Date){
        this.Title=Title;
        this.ImageUrl=ImageUrl;
        this.Date=Date;
    }
}
