package com.example.npttest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.npttest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UseHelp extends Activity {

    @Bind(R.id.UseHelp_return)
    ImageView UseHelpReturn;
    @Bind(R.id.UseHelp_userlogin)
    LinearLayout UseHelpUserlogin;
    @Bind(R.id.UseHelp_carin)
    LinearLayout UseHelpCarin;
    @Bind(R.id.UseHelp_carout)
    LinearLayout UseHelpCarout;
    @Bind(R.id.UseHelp_query)
    LinearLayout UseHelpQuery;
    @Bind(R.id.UseHelp_free_out)
    LinearLayout UseHelpFreeOut;
    @Bind(R.id.UseHelp_modify)
    LinearLayout UseHelpModify;
    @Bind(R.id.UseHelp_common_city)
    LinearLayout UseHelpCommonCity;
    @Bind(R.id.UseHelp_offwork)
    LinearLayout UseHelpOffwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.UseHelp_return, R.id.UseHelp_userlogin, R.id.UseHelp_carin, R.id.UseHelp_carout, R.id.UseHelp_query, R.id.UseHelp_free_out, R.id.UseHelp_modify, R.id.UseHelp_common_city, R.id.UseHelp_offwork})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.UseHelp_return:
                finish();
                break;
            case R.id.UseHelp_userlogin:
                startActivity(new Intent(UseHelp.this, UseHelpUserlogin.class));
                break;
            case R.id.UseHelp_carin:
                startActivity(new Intent(UseHelp.this, UseHelpCarin.class));
                break;
            case R.id.UseHelp_carout:
                startActivity(new Intent(UseHelp.this, UseHelpCarout.class));
                break;
            case R.id.UseHelp_query:
                startActivity(new Intent(UseHelp.this, UseHelpQueryRecord.class));
                break;
            case R.id.UseHelp_free_out:
                startActivity(new Intent(UseHelp.this, UseHelpFreeOut.class));
                break;
            case R.id.UseHelp_modify:
                startActivity(new Intent(UseHelp.this, UseHelpModifyInfo.class));
                break;
            case R.id.UseHelp_common_city:
                startActivity(new Intent(UseHelp.this, UseHelpCommonCity.class));
                break;
            case R.id.UseHelp_offwork:
                startActivity(new Intent(UseHelp.this, UseHelpOffwork.class));
                break;
        }
    }
}
