package com.hlxyedu.mhk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtils {
    /*
        * 将时间戳转换为时间
        */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String getChatTime(long time) {
        return getMinTime(time);
    }

    public static String getMinTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(time));
    }
}
