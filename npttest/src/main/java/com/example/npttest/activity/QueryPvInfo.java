package com.example.npttest.activity;

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
 * Created by liuji on 2017/10/10.
 */

public class QueryPvInfo extends NoStatusbarActivity {


    @Bind(R.id.query_pv_info_return)
    ImageView queryPvInfoReturn;
    @Bind(R.id.query_pv_info_carnum)
    TextView queryPvInfoCarnum;
    @Bind(R.id.query_pv_info_cartype)
    TextView queryPvInfoCartype;
    @Bind(R.id.query_pv_info_pztype)
    TextView queryPvInfoPztype;
    @Bind(R.id.query_pv_info_ptime)
    TextView queryPvInfoPtime;
    @Bind(R.id.query_pv_info_itime)
    TextView queryPvInfoItime;
    private String carnum, cartype, pztype, ptime;
    private int intcartype, intpztype;
    private long itime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_pv_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        intcartype = intent.getIntExtra("cartype", 0);
        intpztype = intent.getIntExtra("pztype", 0);
        itime = intent.getLongExtra("itime", 0);
        ptime = new TimeDifferTools(this).getDistanceTime(itime * 1000, gettime());
        jfjudge();
        queryPvInfoCarnum.setText(carnum);
        queryPvInfoCartype.setText(cartype);
        queryPvInfoPztype.setText(pztype);
        queryPvInfoItime.setText(DateTools.getDate(itime * 1000));
        queryPvInfoPtime.setText(ptime);
    }

    @OnClick(R.id.query_pv_info_return)
    public void onViewClicked() {
        finish();
    }

    private void jfjudge() {
        switch (intcartype) {
            case 1:
                cartype = getString(R.string.motorcycle);
                break;
            case 2:
                cartype = getString(R.string.compacts);
                break;
            case 3:
                cartype = getString(R.string.Intermediate);
                break;
            case 4:
                cartype = getString(R.string.large_vehicle);
                break;
            case 5:
                cartype = getString(R.string.transporter);
                break;
            case 6:
                cartype = getString(R.string.spare_car);
                break;
        }

        switch (intpztype) {
            case 1:
                pztype = getString(R.string.VIP_car);
                break;
            case 2:
                pztype = getString(R.string.monthly_ticket_car);
                break;
            case 3:
                pztype = getString(R.string.reserve_car);
                break;
            case 4:
                pztype = getString(R.string.temporary_car);
                break;
            case 5:
                pztype = getString(R.string.free_car);
                break;
            case 6:
                pztype = getString(R.string.parking_pool_car);
                break;
            case 7:
                pztype = getString(R.string.car_rental);
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
