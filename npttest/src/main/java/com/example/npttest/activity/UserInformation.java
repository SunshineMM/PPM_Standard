package com.example.npttest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.example.npttest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/5.
 */

public class UserInformation extends NoStatusbarActivity {


    @Bind(R.id.user_return)
    ImageView userReturn;
    @Bind(R.id.user_username)
    EditText userUsername;
    @Bind(R.id.user_nickname)
    EditText userNickname;
    @Bind(R.id.user_sex)
    EditText userSex;
    @Bind(R.id.user_tel)
    EditText userTel;
    @Bind(R.id.user_email)
    EditText userEmail;
    @Bind(R.id.user_worktime)
    EditText userWorktime;
    private OptionsPickerView pickerView;
    private List<String> sexs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinformation);
        ButterKnife.bind(this);
        sexs.add("男");
        sexs.add("女");
        initpickview();
    }


    @OnClick({R.id.user_return, R.id.user_sex})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_return:
                finish();
                break;
            case R.id.user_sex:
                userSex.setTextColor(Color.parseColor("#55BEB7"));
                pickerView.show();
                break;
        }
    }

    private void initpickview() {
        pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String sex = sexs.get(options1);
                userSex.setText(sex);
            }
        }).setLayoutRes(R.layout.pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView submitTv = v.findViewById(R.id.tv_finish);
                ImageView imageView = v.findViewById(R.id.iv_cancel);
                submitTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickerView.returnData();
                        pickerView.dismiss();
                        userSex.setTextColor(Color.parseColor("#48495f"));
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickerView.dismiss();
                        userSex.setTextColor(Color.parseColor("#48495f"));
                    }
                });
            }

        }).setContentTextSize(20).setOutSideCancelable(false).build();//setOutSideCancelable点击屏幕，点在控件外部范围时，是否取消显示
        pickerView.setPicker(sexs);
    }
}
