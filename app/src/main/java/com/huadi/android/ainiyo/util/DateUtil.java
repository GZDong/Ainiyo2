package com.huadi.android.ainiyo.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhidong on 2017/8/3.
 */

public class DateUtil {

    public static String getNowDate(){
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String dateString = format.format(currentTime);
        return dateString;
    }
}
