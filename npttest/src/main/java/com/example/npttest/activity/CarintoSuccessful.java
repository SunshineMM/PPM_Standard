package com.example.npttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.manager.ActivityManager;
import com.example.npttest.tool.DateTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/8/31.
 */

public class CarintoSuccessful extends NoStatusbarActivity {


    @Bind(R.id.carin_suful_carnum)
    TextView carinSufulCarnum;
    @Bind(R.id.carin_suful_cartype)
    TextView carinSufulCartype;
    @Bind(R.id.carin_suful_pztype)
    TextView carinSufulPztype;
    @Bind(R.id.carin_suful_intime)
    TextView carinSufulIntime;
    @Bind(R.id.carin_suful_submit)
    Button carinSufulSubmit;
    private String carnum, cartype, jfType;
    private int ctype;
    private long itime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carintosuccessful);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        ctype = intent.getIntExtra("ctype", 0);
        jfType = intent.getStringExtra("jfType");
        itime = intent.getLongExtra("itime", 0);
        judge();
        carinSufulCarnum.setText(carnum);
        carinSufulCartype.setText(cartype);
        carinSufulPztype.setText(jfType);
        carinSufulIntime.setText(DateTools.getDate(itime * 1000) + "");
        ActivityManager.getInstance().addActivity(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.carin_suful_submit)
    public void onViewClicked() {
        startActivity(new Intent(CarintoSuccessful.this, IndexActivity.class));
        App.goRefresh = false;
        finish();
    }


    private void judge() {
        switch (ctype) {
            case 1:
                cartype = "摩托车";
                break;
            case 2:
                cartype = "小型车";
                break;
            case 3:
                cartype = "中型车";
                break;
            case 4:
                cartype = "大型车";
                break;
            case 5:
                cartype = "运输车";
                break;
            case 6:
                cartype = "备用车";
                break;
        }
    }
}
