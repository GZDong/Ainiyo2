package com.huadi.android.ainiyo.gson;

import java.util.List;

/**
 * Created by zhidong on 2017/8/17.
 */

public class ResultForRqstList {
    private String Status;
    private String Msg;
    private List<Resultitem> Result;
    private String Sessionid;

    public class Resultitem {
        public String Id;
        public String Userid;
        public String Requestedid;
        public String Attach;
        public String Agree;

        public String getUserid() {
            return Userid;
        }
    }

    public String getStatus() {
        return Status;
    }

    public List<Resultitem> getResult() {
        return Result;
    }
}
