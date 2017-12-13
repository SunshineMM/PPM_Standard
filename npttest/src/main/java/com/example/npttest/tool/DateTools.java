package com.example.npttest.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DateTools {

    private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 返回日期格式
     */
    public static String getDate(long l){
        return sdf.format(new Date(l));
    }
}
