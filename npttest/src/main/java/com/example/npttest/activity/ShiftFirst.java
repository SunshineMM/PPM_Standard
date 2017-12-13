package com.example.npttest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.npttest.R;
import com.example.npttest.constant.Constant;
import com.example.npttest.util.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/8/31.
 */

public class ShiftFirst extends Activity {


    @Bind(R.id.shift_return)
    ImageView shiftReturn;
    @Bind(R.id.shift_img)
    ImageView shiftImg;
    @Bind(R.id.shift_name)
    TextView shiftName;
    @Bind(R.id.shift_money)
    TextView shiftMoney;
    @Bind(R.id.shift_modifymon)
    TextView shiftModifymon;
    @Bind(R.id.shift_worktime)
    TextView shiftWorktime;
    @Bind(R.id.shift_password)
    EditText shiftPassword;
    @Bind(R.id.shift_submit)
    Button shiftSubmit;
    private String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shiftfirst);
        ButterKnife.bind(this);
        username = (String) SPUtils.get(ShiftFirst.this, Constant.USERNAME, "");
        shiftName.setText(username);
    }

    @OnClick({R.id.shift_return, R.id.shift_modifymon, R.id.shift_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shift_return:
                finish();
                break;
            case R.id.shift_modifymon:

                break;
            case R.id.shift_submit:

                break;
        }
    }
}
