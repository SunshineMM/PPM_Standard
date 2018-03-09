package com.example.npttest.tool;

import android.app.Activity;

import com.example.npttest.R;

/**
 * Created by liuji on 2017/9/27.
 */

public class TimeDifferTools {
    private Activity mcontext;

    public TimeDifferTools(Activity mContext){
        this.mcontext=mContext;
    }
    /*
    *计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
    * 根据差值返回多长之间前或多长时间后
    * */
    public String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff ;
        String flag;
        if(time1<time2) {
            diff = time2 - time1;
            flag="前";
        } else {
            diff = time1 - time2;
            flag="后";
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
        if(day!=0){
            if (sec>0){
                min=min+1;
            }
            return day+mcontext.getString(R.string.day_)+hour+mcontext.getString(R.string.hour_)+min+mcontext.getString(R.string.min_);
        }
        if(hour!=0){
            if (sec>0){
                min=min+1;
            }
            return hour+mcontext.getString(R.string.hour_)+min+mcontext.getString(R.string.min_);
        }
        if(min!=0) {
            if (sec>0){
                min=min+1;
            }
            return min + mcontext.getString(R.string.min_);
        }
        //if (sec!=0)return sec+"秒";
        return mcontext.getString(R.string.one_min);
    }
}
