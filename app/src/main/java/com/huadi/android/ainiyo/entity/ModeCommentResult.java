package com.huadi.android.ainiyo.entity;

/**
 * Created by fengsam on 17-8-15.
 */

public class ModeCommentResult {
    private int Page;
    private int Pagesize;
    private int Pagecount;
    private int Sum;
    private ModeCommentData[] Data;

    public ModeCommentResult(int page, int pagesize, int pagecount, int sum, ModeCommentData[] data) {
        Page = page;
        Pagesize = pagesize;
        Pagecount = pagecount;
        Sum = sum;
        Data = data;
    }

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

    public ModeCommentData[] getData() {
        return Data;
    }

    public void setData(ModeCommentData[] data) {
        Data = data;
    }
}
