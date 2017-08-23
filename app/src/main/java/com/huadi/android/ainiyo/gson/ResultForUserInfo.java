package com.huadi.android.ainiyo.gson;

import java.util.List;

/**
 * Created by zhidong on 2017/8/23.
 */

public class ResultForUserInfo {
    public String Status;
    public String Msg;
    public ResultClass Result;
    public String Sessionid;

    public class ResultClass{
        private String Id;
        private boolean Vip;
        private String birthday;
        private int Area;
        private String Job;
        private double Salary;
        private boolean HaveKids;
        private boolean parentsalive;
        private String maritallstatus;
        private String Emotion;
        private String Hobby;
        private String Requir;
        private String avatar;
        private String Userid;

        public String getAvatar() {
            return avatar;
        }
    }

    public String getStatus() {
        return Status;
    }

    public ResultClass getResult() {
        return Result;
    }
}
