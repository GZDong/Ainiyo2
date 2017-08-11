package com.huadi.android.ainiyo.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhidong on 2017/8/10.
 */

public class FriendGot {

    public int Status;

    public String Msg;

    @SerializedName("Result")
    public List<ResultForFriend> friendList;

    public String Sessionid;
}
