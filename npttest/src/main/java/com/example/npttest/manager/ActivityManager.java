package com.example.npttest.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**activity管理
 * Created by Administrator on 2017/5/8.
 */

public class ActivityManager {
    //装载activity的容器
    private List<Activity> activities=new ArrayList<>();
    private static ActivityManager activityManager=null;
    private ActivityManager(){};//私有的构造方法，外部类是不能实例化
    /**
     *供外部使用Activity的单例模式
     * @return
     */

    public static ActivityManager getInstance(){
        if (activityManager==null){
            activityManager=new ActivityManager();
        }
         return activityManager;
    }

    /**
     * 添加activity
     * @param activity
     */
    public void addActivity (Activity activity){
        activities.add(activity);
    }

    /**
     * 移除activity
     */
    public void removeActivity(Activity activity){
        //检查list中是否包含
        if (activities.contains(activity)){
            activity.finish();
            activities.remove(activity);
        }

    }


    /**
     * 退出
     */

    public void exit(){
        for (Activity activity:activities){
            if (activity!=null)
            activity.finish();
        }
    }
}
