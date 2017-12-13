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

public class MyLayoutIn extends LinearLayout {
    public MyLayoutIn(Context context) {
        super(context);
    }

    public MyLayoutIn(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mylinearlayout_in,this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
