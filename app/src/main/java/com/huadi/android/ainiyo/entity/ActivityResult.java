package com.huadi.android.ainiyo.entity;

import java.util.List;

/**
 * Created by xiaoxing on 2017/8/27.
 */

public class ActivityResult {
    private int Page;
    private int Pagesize;
    private int Pagecount;
    private int Sum;

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getPagesize() {
        return Pagesize;
    }

    public void setPagesize(int pagesize) {
        Pagesize = pagesize;
    }

    public int getPagecount() {
        return Pagecount;
    }

    public void setPagecount(int pagecount) {
        Pagecount = pagecount;
    }

    public int getSum() {
        return Sum;
    }

    public void setSum(int sum) {
        Sum = sum;
    }

    public List<ActivityData> getData() {
        return Data;
    }

    public void setData(List<ActivityData> data) {
        Data = data;
    }

    public String getSessionid() {
        return Sessionid;
    }

    public void setSessionid(String sessionid) {
        Sessionid = sessionid;
    }

    private List<ActivityData> Data;
    private String Sessionid;
}
