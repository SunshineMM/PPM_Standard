package com.example.npttest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.example.npttest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/25.
 */

public class ProhibitPass extends Activity {

    @Bind(R.id.prohibit_carnum)
    TextView prohibitCarnum;
    @Bind(R.id.prohibit_yy)
    TextView prohibitYy;
    @Bind(R.id.prohibit_submit)
    Button prohibitSubmit;
    private String carnum, cartype, jfType, comfirmYy;
    private int ctype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prohibit_pass);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        comfirmYy = intent.getStringExtra("comfirmYy");
        prohibitCarnum.setText(carnum);
        prohibitYy.setText(comfirmYy);
    }

    @OnClick(R.id.prohibit_submit)
    public void onViewClicked() {
        //startActivity(new Intent(ProhibitPass.this, IndexActivity.class));
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
