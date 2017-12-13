package com.example.npttest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.npttest.R;
import com.example.npttest.tool.DateTools;
import com.example.npttest.tool.TimeDifferTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/30.
 */

public class ChargeDetailedInfo extends Activity {
    @Bind(R.id.charge_record_return)
    ImageView chargeRecordReturn;
    @Bind(R.id.charge_record_title)
    LinearLayout chargeRecordTitle;
    @Bind(R.id.charge_record_carnum)
    TextView chargeRecordCarnum;
    @Bind(R.id.charge_record_owner)
    TextView chargeRecordOwner;
    @Bind(R.id.charge_record_cartype)
    TextView chargeRecordCartype;
    @Bind(R.id.charge_record_pztype)
    TextView chargeRecordPztype;
    @Bind(R.id.charge_record_nmon)
    TextView chargeRecordNmon;
    @Bind(R.id.charge_record_smon)
    TextView chargeRecordSmon;
    @Bind(R.id.charge_record_rmon)
    TextView chargeRecordRmon;
    @Bind(R.id.charge_record_itime)
    TextView chargeRecordItime;
    @Bind(R.id.charge_record_ctime)
    TextView chargeRecordCtime;
    @Bind(R.id.charge_record_ptime)
    TextView chargeRecordPtime;
    @Bind(R.id.charge_record_ptype)
    TextView chargeRecordPtype;
    private String carnum, cartype, pztype, srmon, snmon, ssmon;
    private String ptype;
    private double nmom, rmon, smon;
    private long itime, ctime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge_detailed_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        cartype = intent.getStringExtra("cartype");
        pztype = intent.getStringExtra("pztype");
        nmom = intent.getDoubleExtra("nmon", 0);
        rmon = intent.getDoubleExtra("rmon", 0);
        smon = intent.getDoubleExtra("smon", 0);
        ptype = intent.getStringExtra("ptype");
        srmon = String.format("%.2f", rmon / 100);
        snmon = String.format("%.2f", nmom / 100);
        ssmon = String.format("%.2f", smon / 100);
        itime = intent.getLongExtra("itime", 0);
        ctime = intent.getLongExtra("ctime", 0);
        Log.e("TAG", nmom + "oooo" + rmon + "oooo" + smon + "");
        chargeRecordPtype.setText(ptype);
        chargeRecordCarnum.setText(carnum);
        chargeRecordCartype.setText(cartype);
        chargeRecordPztype.setText(pztype);
        chargeRecordNmon.setText(snmon + "元");
        chargeRecordSmon.setText(ssmon + "元");
        chargeRecordRmon.setText(srmon + "元");
        chargeRecordItime.setText(DateTools.getDate(itime * 1000));
        chargeRecordCtime.setText(DateTools.getDate(ctime * 1000));
        chargeRecordPtime.setText(TimeDifferTools.getDistanceTime(itime * 1000, ctime * 1000));
    }

    @OnClick(R.id.charge_record_return)
    public void onViewClicked() {
        finish();
    }
}
