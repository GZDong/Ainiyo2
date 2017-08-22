package com.huadi.android.ainiyo.entity;

import java.io.Serializable;

/**
 * Created by fengsam on 17-8-21.
 */

public class FindingAphorism implements Serializable {
    private int Id;
    private String Content;
    private String Author;

    public FindingAphorism(int id, String content, String author) {
        Id = id;
        Content = content;
        Author = author;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }
}
