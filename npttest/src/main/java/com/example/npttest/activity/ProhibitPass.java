package com.example.npttest.activity;

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

public class ProhibitPass extends NoStatusbarActivity {

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
    }
}
