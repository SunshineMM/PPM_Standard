package com.example.npttest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.npttest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by liuji on 2017/9/18.
 */

public class Proposal extends Activity {

    @Bind(R.id.propo_iv)
    ImageView propoIv;
    @Bind(R.id.propo_et)
    EditText propoEt;
    @Bind(R.id.propo_btn)
    Button propoBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.propo_iv, R.id.propo_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.propo_iv:
                finish();
                break;
            case R.id.propo_btn:
                if (TextUtils.isEmpty(propoEt.getText().toString())){
                    //Toast.makeText(this, "请输入建议", Toast.LENGTH_SHORT).show();
                    Toasty.error(this, "请输入建议", Toast.LENGTH_SHORT,true).show();
                }else {
                    //Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                    Toasty.success(this, "提交成功", Toast.LENGTH_SHORT,true).show();
                }
                break;
        }
    }
}
