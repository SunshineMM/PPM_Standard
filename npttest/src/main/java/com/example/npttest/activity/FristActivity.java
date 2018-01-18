package com.example.npttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.npttest.R;
import com.example.npttest.util.SPUtils;

/**
 * Created by liuji on 2017/10/17.
 */

public class FristActivity extends NoStatusbarActivity {
    private Boolean frist_in;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frist_activity);
        frist_in= (Boolean) SPUtils.get(FristActivity.this,"frist",true);
        if (frist_in){
            startActivity(new Intent(FristActivity.this,GuideActivity.class));
            finish();
        }else {
            startActivity(new Intent(FristActivity.this,SpalshActivity.class));
            finish();
        }
    }
}
