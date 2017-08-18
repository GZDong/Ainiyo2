package com.huadi.android.ainiyo.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhidong on 2017/8/10.
 */

public class FriendGot {

    public String Status;

    public String Msg;

    @SerializedName("Result")
    public List<ResultForFriend> friendList;

    public String Sessionid;

    public String getStatus() {
        return Status;
    }

    public String getMsg() {
        return Msg;
    }

    public List<ResultForFriend> getFriendList() {
        return friendList;
    }
}
