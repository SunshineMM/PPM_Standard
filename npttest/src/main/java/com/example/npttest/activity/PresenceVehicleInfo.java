package com.example.npttest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.npttest.R;
import com.example.npttest.tool.DateTools;
import com.example.npttest.tool.TimeDifferTools;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/8.
 */

public class PresenceVehicleInfo extends Activity {

    @Bind(R.id.carinfo_return)
    ImageView carinfoReturn;
    @Bind(R.id.carinfo_carnum)
    TextView carinfoCarnum;
    @Bind(R.id.carinfo_cartype)
    TextView carinfoCartype;
    @Bind(R.id.carinfo_pztype)
    TextView carinfoPztype;
    @Bind(R.id.carinfo_ptime)
    TextView carinfoPtime;
    @Bind(R.id.carinfo_itime)
    TextView carinfoItime;
    private String carnum, cartype, pztype, ptime,sid;
    private int intcartype, intpztype;
    private long itime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presence_vehicle_info);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        intcartype = intent.getIntExtra("cartype", 0);
        intpztype = intent.getIntExtra("pztype", 0);
        itime = intent.getLongExtra("itime", 0);
        sid=intent.getStringExtra("sid");
        ptime = TimeDifferTools.getDistanceTime(itime * 1000, gettime());
        jfjudge();
        carinfoCarnum.setText(carnum);
        carinfoCartype.setText(cartype);
        carinfoPztype.setText(pztype);
        carinfoItime.setText(DateTools.getDate(itime * 1000));
        carinfoPtime.setText(ptime);
    }

    @OnClick(R.id.carinfo_return)
    public void onViewClicked() {
        finish();
    }

    private void jfjudge() {
        switch (intcartype) {
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

        switch (intpztype) {
            case 1:
                pztype = "贵宾车";
                break;
            case 2:
                pztype = "月票车";
                break;
            case 3:
                pztype = "储值车";
                break;
            case 4:
                pztype = "临时车";
                break;
            case 5:
                pztype = "免费车";
                break;
            case 6:
                pztype = "车位池车";
                break;
            case 7:
                pztype = "时租车";
                break;
        }
    }

    //获取当前时间
    private long gettime() {
        Date date = new Date();
        long time = (date.getTime());
        //Log.e("TAG",date.getTime()/1000+"");
        return time;
    }
}
