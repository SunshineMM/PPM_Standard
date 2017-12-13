package com.example.npttest.activity;

import android.app.Activity;
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

public class CarinDetailedInfo extends Activity {

    @Bind(R.id.carin_record_return)
    ImageView carinRecordReturn;
    @Bind(R.id.carin_record_carnum)
    TextView carinRecordCarnum;
    @Bind(R.id.carin_record_cartype)
    TextView carinRecordCartype;
    @Bind(R.id.carin_record_pztype)
    TextView carinRecordPztype;
    @Bind(R.id.carin_record_itime)
    TextView carinRecordItime;
    private String carnum, cartype, pztype;
    private long itime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carin_detailed_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        cartype = intent.getStringExtra("cartype");
        pztype = intent.getStringExtra("pztype");
        itime = intent.getLongExtra("itime", 0);
        carinRecordCarnum.setText(carnum);
        carinRecordCartype.setText(cartype);
        carinRecordPztype.setText(pztype);
        carinRecordItime.setText(DateTools.getDate(itime * 1000));
    }

    @OnClick(R.id.carin_record_return)
    public void onViewClicked() {
        finish();
    }
}
