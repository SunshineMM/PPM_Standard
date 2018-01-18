package com.example.npttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.npttest.R;
import com.example.npttest.tool.DateTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/20.
 */

public class CaroutDetailedInfo extends NoStatusbarActivity {


    @Bind(R.id.carout_record_return)
    ImageView caroutRecordReturn;
    @Bind(R.id.carout_record_carnum)
    TextView caroutRecordCarnum;
    @Bind(R.id.carout_record_cartype)
    TextView caroutRecordCartype;
    @Bind(R.id.carout_record_pztype)
    TextView caroutRecordPztype;
    @Bind(R.id.carout_record_ctime)
    TextView caroutRecordCtime;
    private String carnum, cartype, pztype;
    private long ctime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carout_detailed_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        cartype = intent.getStringExtra("cartype");
        pztype = intent.getStringExtra("pztype");
        ctime = intent.getLongExtra("ctime", 0);
        caroutRecordCarnum.setText(carnum);
        caroutRecordCartype.setText(cartype);
        caroutRecordPztype.setText(pztype);
        caroutRecordCtime.setText(DateTools.getDate(ctime * 1000));
    }

    @OnClick(R.id.carout_record_return)
    public void onViewClicked() {
        finish();
    }

}
