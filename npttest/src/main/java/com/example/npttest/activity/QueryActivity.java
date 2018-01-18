package com.example.npttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.npttest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/10/21.
 */

public class QueryActivity extends NoStatusbarActivity {


    @Bind(R.id.query_aty_return)
    ImageView queryAtyReturn;
    @Bind(R.id.query_aty_changnei)
    LinearLayout queryAtyChangnei;
    @Bind(R.id.query_aty_carin)
    LinearLayout queryAtyCarin;
    @Bind(R.id.query_aty_carout)
    LinearLayout queryAtyCarout;
    @Bind(R.id.query_aty_charge)
    LinearLayout queryAtyCharge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_activity);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.query_aty_return, R.id.query_aty_changnei, R.id.query_aty_carin, R.id.query_aty_carout, R.id.query_aty_charge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query_aty_return:
                finish();
                break;
            case R.id.query_aty_changnei:
                startActivity(new Intent(QueryActivity.this, QueryCarnum.class));
                break;
            case R.id.query_aty_carin:
                startActivity(new Intent(QueryActivity.this, CarinRecord.class));
                break;
            case R.id.query_aty_carout:
                startActivity(new Intent(QueryActivity.this, CaroutRecord.class));
                break;
            case R.id.query_aty_charge:
                startActivity(new Intent(QueryActivity.this, ChargeRecord.class));
                break;
        }
    }
}
