package com.huadi.android.ainiyo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengsam on 17-8-20.
 */

public class FindingLikeList implements Serializable {
    public ArrayList<FindingInfo> mList;

    public FindingLikeList() {
        mList = new ArrayList<FindingInfo>();
    }
}
