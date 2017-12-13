package com.example.npttest.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liuji on 2017/8/15.
 */

public class MyIndexviewpager extends ViewPager {
    private boolean isCanScroll = false;
    public MyIndexviewpager(Context context) {
        super(context);
    }

    public MyIndexviewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {

        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {

        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {

        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {

        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }
}
