package com.huadi.android.ainiyo.entity;

import java.io.Serializable;

/**
 * Created by fengsam on 17-7-26.
 */

public class ModeInfo implements Serializable{
    private String id;
    private String name;
    private String content;
    private String imgUrlforHead;
    private String imgUrlforContent;

    public ModeInfo(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrlforHead() {
        return imgUrlforHead;
    }

    public void setImgUrlforHead(String imgUrlforHead) {
        this.imgUrlforHead = imgUrlforHead;
    }

    public String getImgUrlforContent() {
        return imgUrlforContent;
    }

    public void setImgUrlforContent(String imgUrlforContent) {
        this.imgUrlforContent = imgUrlforContent;
    }
}
