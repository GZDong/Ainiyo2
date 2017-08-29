package com.huadi.android.ainiyo.util;

import android.util.Log;

import java.util.Date;

/**
 * Created by zhidong on 2017/8/29.
 */

public class TimeUtil {
    private final static long minute = 60 * 1000;
    private final static long hour = 60 * minute;
    private final static long day = hour * 24;
    private final static long month = 31 * day;
    private final static long year = 12 * month;

    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }

        long diff = new Date().getTime() - date.getTime();
        /*Log.e("test", "getTimeFormatText:  now Time :" + new Date().getTime());
        Log.e("test", "getTimeFormatText:  old Time :" + date.getTime());
        Log.e("test", "getTimeFormatText:  diff :" + diff);
        Log.e("test", "getTimeFormatText:  diff :" + diff / year);
        Log.e("test", "getTimeFormatText:  diff :" + diff / month);
        Log.e("test", "getTimeFormatText:  diff :" + diff / day );
        Log.e("test", "getTimeFormatText:  diff :" + diff / hour);
        Log.e("test", "getTimeFormatText:  diff :" + diff / minute);*/
        long r = 0;

        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }

        if (diff > month) {
            r = (diff / month);
            return r + "月前";
        }

        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }

        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }

        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }

        return "刚刚";
    }
}
