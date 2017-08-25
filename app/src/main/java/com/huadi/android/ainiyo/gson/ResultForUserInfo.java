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
        private String Birthday;
        private int Area;
        private String Job;
        private double Salary;
        private boolean HaveKids;
        private boolean Parentsalive;
        private String Maritallstatus;
        private String Emotion;
        private String Hobby;
        private String Requir;
        private String Avatar;
        private String Userid;

        public String getAvatar() {
            return Avatar;
        }
    }

    public String getStatus() {
        return Status;
    }

    public ResultClass getResult() {
        return Result;
    }
}
