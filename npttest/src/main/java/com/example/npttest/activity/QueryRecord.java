package com.example.npttest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.npttest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/10/20.
 */

public class QueryRecord extends NoStatusbarActivity {


    @Bind(R.id.query_record_return)
    ImageView queryRecordReturn;
    @Bind(R.id.textView9)
    TextView textView9;
    @Bind(R.id.query_carinrecord)
    LinearLayout queryCarinrecord;
    @Bind(R.id.textView10)
    TextView textView10;
    @Bind(R.id.query_caroutrecord)
    LinearLayout queryCaroutrecord;
    @Bind(R.id.query_chargerecord)
    LinearLayout queryChargerecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_record);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.query_record_return, R.id.query_carinrecord, R.id.query_caroutrecord, R.id.query_chargerecord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query_record_return:
                finish();
                break;
            case R.id.query_carinrecord:
                startActivity(new Intent(QueryRecord.this, CarinRecord.class));
                break;
            case R.id.query_caroutrecord:
                startActivity(new Intent(QueryRecord.this, CaroutRecord.class));
                break;
            case R.id.query_chargerecord:
                startActivity(new Intent(QueryRecord.this, ChargeRecord.class));
                break;
        }
    }
}
