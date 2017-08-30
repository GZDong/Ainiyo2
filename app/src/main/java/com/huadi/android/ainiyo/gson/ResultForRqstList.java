package com.huadi.android.ainiyo.gson;

import java.util.List;

/**
 * Created by zhidong on 2017/8/17.
 */

public class ResultForRqstList {
    private String Status;
    private String Msg;
    private Result_class Result;
    private String Sessionid;

    public class Result_class{
        private String Page;
        private String Pagesize;
        private String Pagecount;
        private String Sum;
        private List<Dataitem> Data;

        public List<Dataitem> getData() {
            return Data;
        }
    }

    public class Dataitem {
        public String Id;
        public String Userid;
        public String Requestedid;
        public String Attach;
        public String Agree;
        public String Date;

        public String getUserid() {
            return Userid;
        }
    }

    public String getStatus() {
        return Status;
    }

    public Result_class getResult() {
        return Result;
    }



    public int getSize(){
        return Result.getData().size();
    }
}
