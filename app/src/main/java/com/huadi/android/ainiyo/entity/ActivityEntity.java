package com.huadi.android.ainiyo.entity;

/**
 * Created by xiaoxing on 2017/8/28.
 */

public class ActivityEntity {


    public String getTitle() {
        return Title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getDate() {
        return Date;
    }



    private String Title;
    private String ImageUrl;
    private String Date;

    public String getArticle() {
        return Article;
    }

    private String Article;

    public  ActivityEntity(String Title,String ImageUrl,String Date,String Article){
        this.Title=Title;
        this.ImageUrl=ImageUrl;
        this.Date=Date;
        this.Article=Article;
    }
}
