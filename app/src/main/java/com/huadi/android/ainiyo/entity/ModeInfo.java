package com.huadi.android.ainiyo.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fengsam on 17-7-26.
 */

public class ModeInfo implements Serializable{
    private String id;
    private String name;
    private String content;
    private String imgUrlforHead;
    private ArrayList<String> imgUrlforContent;

    public ModeInfo(String name, String content,String imgUrlforHead,ArrayList<String> imgUrlforContent) {
        this.name = name;
        this.content = content;
        this.imgUrlforContent=imgUrlforContent;
        this.imgUrlforHead=imgUrlforHead;
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

    public ArrayList<String> getImgUrlforContent() {
        return imgUrlforContent;
    }

    public void setImgUrlforContent(ArrayList<String> imgUrlforContent) {
        this.imgUrlforContent = imgUrlforContent;
    }
}
