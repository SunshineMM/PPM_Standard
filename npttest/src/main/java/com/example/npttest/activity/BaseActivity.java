package com.example.npttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.npttest.manager.ActivityManager;

import es.dmoral.toasty.Toasty;


/**
 * Created by Administrator on 2017/5/5.
 */
//基类
public class BaseActivity extends FragmentActivity {
    //记录第一次按下返回键的时间
    private long firsttime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);//将activity添加到容器
    }

    //按键按下的监听事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            long secondTime=System.currentTimeMillis();//第二次按下的时间
            if (secondTime-firsttime>2000){
                Toasty.info(BaseActivity.this, "再按一次退出", Toast.LENGTH_SHORT, true).show();
                firsttime=System.currentTimeMillis();//记录当前按下的时间
            }else {
                //finish();
                ActivityManager.getInstance().exit();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);//移除activity
    }
    /**
     * Toast操作
     */

    public void T(String mess){
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    /**
     * activity跳转
     * @param aClass
     */
    public void startToActivity(Class aClass){
        startActivity(new Intent(this,aClass));
    }
}
