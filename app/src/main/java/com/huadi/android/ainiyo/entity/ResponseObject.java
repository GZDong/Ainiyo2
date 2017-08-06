package com.huadi.android.ainiyo.entity;

public class ResponseObject<T> {

    private String Msg;
    private int Status;
    private T Result;// 存放我真正需要解析的数据
    private String Sessionid;

    public ResponseObject(String msg, int status, T result, String sessionid) {
        Msg = msg;
        Status = status;
        Result = result;
        Sessionid = sessionid;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }

    public String getSessionid() {
        return Sessionid;
    }

    public void setSessionid(String sessionid) {
        Sessionid = sessionid;
    }
}
