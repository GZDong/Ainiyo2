package com.huadi.android.ainiyo.entity;

/**
 * Created by rxvincent on 2017/8/16.
 */

public class MovementContentData {
    private int Id;
    private String Title;
    private String Date;
    private String ImageUrl;
    private String article;

    public MovementContentData(int id, String title, String date, String imageUrl, String article) {
        Id = id;
        Title = title;
        Date = date;
        ImageUrl = imageUrl;
        this.article = article;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
