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
        private String Autograph;
        private boolean Vip;
        private String Gentle;
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
        private String Phone;
        private String AreaName;

        public String getPhone() {
            return Phone;
        }

        public String getAvatar() {
            return Avatar;
        }

        public String getId() {
            return Id;
        }

        public String getUserid() {
            return Userid;
        }

        public String getGentle() {
            return Gentle;
        }

        public int getArea() {
            return Area;
        }
        public String getAutograph() {
            return Autograph;
        }

        public String getBirthday() {
            return Birthday;
        }

        public String getAreaName() {
            return AreaName;
        }

        public String getHobby() {
            return Hobby;
        }

    }

    public String getStatus() {
        return Status;
    }

    public ResultClass getResult() {
        return Result;
    }


}
