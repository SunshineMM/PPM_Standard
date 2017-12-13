package com.example.npttest.linearlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.example.npttest.R;

/**
 * Created by liuji on 2017/9/21.
 */

public class MyLayout extends LinearLayout {

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.my_linearlayout,this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
