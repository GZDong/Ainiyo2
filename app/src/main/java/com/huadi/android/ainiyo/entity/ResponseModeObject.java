package com.huadi.android.ainiyo.entity;

/**
 * Created by fengsam on 17-8-5.
 */

public class ResponseModeObject {

    private int Status;
    private String Msg;
    private ModeResult Result;
    private String Sessionid;


    public ResponseModeObject(int status, String msg, ModeResult result, String sessionid) {
        Status = status;
        Msg = msg;
        Result = result;
        Sessionid = sessionid;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public ModeResult getResult() {
        return Result;
    }

    public void setResult(ModeResult result) {
        Result = result;
    }

    public String getSessionid() {
        return Sessionid;
    }

    public void setSessionid(String sessionid) {
        Sessionid = sessionid;
    }
}
