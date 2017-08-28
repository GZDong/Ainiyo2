package com.huadi.android.ainiyo.entity;

import java.util.ArrayList;

/**
 * Created by fengsam on 17-8-27.
 */

public class FindingResult {
    private int Page;
    private int Pagesize;
    private int Pagecount;
    private int Sum;
    private ArrayList<FindingInfo> Data = new ArrayList<FindingInfo>();

    public FindingResult(int page, int pagesize, int pagecount, int sum, ArrayList<FindingInfo> Data) {
        Page = page;
        Pagesize = pagesize;
        Pagecount = pagecount;
        Sum = sum;
        this.Data = Data;
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

    public ArrayList<FindingInfo> getData() {
        return Data;
    }

    public void setData(ArrayList<FindingInfo> Data) {
        this.Data = Data;
    }
}
