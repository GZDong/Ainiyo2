package com.huadi.android.ainiyo.gson;

import java.util.List;

/**
 * Created by zhidong on 2017/9/1.
 */

public class ResultForFriAlbums {
    private String Status;
    private String Msg;
    private List<AlbumsName> Result;
    private String Sessionid;


    public String getMsg() {
        return Msg;
    }

    public List<AlbumsName> getResult() {
        return Result;
    }
}
