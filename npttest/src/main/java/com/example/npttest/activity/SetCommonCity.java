package com.example.npttest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.example.npttest.R;
import com.example.npttest.constant.Constant;
import com.example.npttest.util.SPUtils;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/11.
 */

public class SetCommonCity extends NoStatusbarActivity implements CompoundButton.OnCheckedChangeListener {


    @Bind(R.id.setCity_return)
    ImageView setCityReturn;
    @Bind(R.id.setCity_sbtn)
    SwitchButton setCitySbtn;
    @Bind(R.id.setCity_city)
    EditText setCityCity;
    @Bind(R.id.setCity_lin)
    LinearLayout setCityLin;
    private OptionsPickerView pickerView;
    private List<String> citys = new ArrayList<>();
    private List<String> abcs1 = new ArrayList<>();
    private List<String> abcs2 = new ArrayList<>();
    private List<String> abcs3 = new ArrayList<>();
    private List<String> abcs4 = new ArrayList<>();
    private List<String> abcs5 = new ArrayList<>();
    private List<String> abcs6 = new ArrayList<>();
    private List<String> abcs7 = new ArrayList<>();
    private List<String> abcs8 = new ArrayList<>();
    private List<String> abcs9 = new ArrayList<>();
    private List<String> abcs10 = new ArrayList<>();
    private List<String> abcs11 = new ArrayList<>();
    private List<String> abcs12 = new ArrayList<>();
    private List<String> abcs13 = new ArrayList<>();
    private List<String> abcs14 = new ArrayList<>();
    private List<String> abcs15 = new ArrayList<>();
    private List<String> abcs16 = new ArrayList<>();
    private List<String> abcs17 = new ArrayList<>();
    private List<String> abcs18 = new ArrayList<>();
    private List<String> abcs19 = new ArrayList<>();
    private List<String> abcs20 = new ArrayList<>();
    private List<String> abcs21 = new ArrayList<>();
    private List<String> abcs22 = new ArrayList<>();
    private List<String> abcs23 = new ArrayList<>();
    private List<String> abcs24 = new ArrayList<>();
    private List<String> abcs25 = new ArrayList<>();
    private List<String> abcs26 = new ArrayList<>();
    private List<String> abcs27 = new ArrayList<>();
    private List<String> abcs28 = new ArrayList<>();
    private List<String> abcs29 = new ArrayList<>();
    private List<String> abcs30 = new ArrayList<>();
    private List<String> abcs31 = new ArrayList<>();
    private List<List<String>> abc = new ArrayList<>();
    private String sex;
    private String cityTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_common_city);
        ButterKnife.bind(this);
        //setCitySbtn.setChecked(true);
        Boolean aBoolean = (Boolean) SPUtils.get(SetCommonCity.this, Constant.SET_CITY, false);
        if (aBoolean) {
            setCityLin.setVisibility(View.VISIBLE);
        } else {
            setCityLin.setVisibility(View.GONE);
        }
        cityTv = (String) SPUtils.get(SetCommonCity.this, Constant.COM_CITY_A, "京A");
        Log.e("TAG", cityTv);
        setCityCity.setText(cityTv);
        setCitySbtn.setCheckedImmediately(aBoolean);
        setCitySbtn.setOnCheckedChangeListener(this);
        initCity();
        initpickView();
    }

    @OnClick({R.id.setCity_return, R.id.setCity_sbtn, R.id.setCity_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setCity_return:
                finish();
                break;
            case R.id.setCity_sbtn:

                break;
            case R.id.setCity_city:
                setCityCity.setTextColor(Color.parseColor("#55BEB7"));
                pickerView.show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            //Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            SPUtils.put(SetCommonCity.this, Constant.SET_CITY, true);
            SPUtils.put(SetCommonCity.this, Constant.COM_CITY, cityTv);
            setCityLin.setVisibility(View.VISIBLE);
        } else {
            //Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
            SPUtils.put(SetCommonCity.this, Constant.SET_CITY, false);
            SPUtils.put(SetCommonCity.this, Constant.COM_CITY, "");
            setCityLin.setVisibility(View.GONE);
        }
    }

    private void initpickView() {
        pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                sex = citys.get(options1) + abc.get(options1).get(options2);
                setCityCity.setText(sex);
                SPUtils.put(SetCommonCity.this, Constant.COM_CITY, sex);
                SPUtils.put(SetCommonCity.this, Constant.COM_CITY_A, sex);
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
                        setCityCity.setTextColor(Color.parseColor("#48495f"));
                    }
                });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickerView.dismiss();
                        setCityCity.setTextColor(Color.parseColor("#48495f"));
                    }
                });
            }
        }).setOutSideCancelable(false).build();//setOutSideCancelable点击屏幕，点在控件外部范围时，是否取消显示

        pickerView.setPicker(citys, abc);
    }

    private void initCity() {
        citys.add("京");
        citys.add("津");
        citys.add("冀");
        citys.add("鲁");
        citys.add("晋");
        citys.add("蒙");
        citys.add("辽");
        citys.add("吉");
        citys.add("黑");
        citys.add("沪");
        citys.add("苏");
        citys.add("浙");
        citys.add("皖");
        citys.add("闽");
        citys.add("赣");
        citys.add("豫");
        citys.add("鄂");
        citys.add("湘");
        citys.add("粤");
        citys.add("桂");
        citys.add("渝");
        citys.add("川");
        citys.add("贵");
        citys.add("云");
        citys.add("藏");
        citys.add("陕");
        citys.add("甘");
        citys.add("青");
        citys.add("琼");
        citys.add("新");
        citys.add("宁");

       /* abcs1.add("A");abcs1.add("B");abcs1.add("C");abcs1.add("D");abcs1.add("E");abcs1.add("F");abcs1.add("G");abcs1.add("H");
        abcs1.add("I");abcs1.add("J");abcs1.add("K");abcs1.add("L");abcs1.add("M");abcs1.add("N");abcs1.add("O");abcs1.add("P");
        abcs1.add("Q");abcs1.add("R");abcs1.add("S");abcs1.add("T");abcs1.add("U");abcs1.add("V");abcs1.add("W");abcs1.add("X");
        abcs1.add("Y");abcs1.add("Z");*/
        //北京
        abcs1.add("A");
        abcs1.add("B");
        abcs1.add("C");
        abcs1.add("D");
        abcs1.add("E");
        abcs1.add("F");
        abcs1.add("G");
        abcs1.add("H");
        abcs1.add("J");
        abcs1.add("K");
        abcs1.add("L");
        abcs1.add("M");
        abcs1.add("N");
        abcs1.add("O");
        abcs1.add("P");
        abcs1.add("Q");
        abcs1.add("R");
        abcs1.add("S");
        abcs1.add("T");
        abcs1.add("U");
        abcs1.add("V");
        abcs1.add("W");
        abcs1.add("X");
        abcs1.add("Y");
        abcs1.add("Z");
        //津
        abcs2.add("A");
        abcs2.add("B");
        abcs2.add("C");
        abcs2.add("D");
        abcs2.add("E");
        abcs2.add("F");
        abcs2.add("G");
        abcs2.add("H");
        abcs2.add("J");
        abcs2.add("K");
        abcs2.add("L");
        abcs2.add("M");
        abcs2.add("N");
        abcs2.add("Q");
        abcs2.add("R");

        //冀
        abcs3.add("A");
        abcs3.add("B");
        abcs3.add("C");
        abcs3.add("D");
        abcs3.add("E");
        abcs3.add("F");
        abcs3.add("G");
        abcs3.add("H");
        abcs3.add("J");
        abcs3.add("R");
        abcs3.add("T");

        //鲁
        abcs4.add("A");
        abcs4.add("B");
        abcs4.add("C");
        abcs4.add("D");
        abcs4.add("E");
        abcs4.add("F");
        abcs4.add("G");
        abcs4.add("H");
        abcs4.add("J");
        abcs4.add("K");
        abcs4.add("L");
        abcs4.add("M");
        abcs4.add("N");
        abcs4.add("O");
        abcs4.add("P");
        abcs4.add("Q");
        abcs4.add("R");
        abcs4.add("S");
        abcs4.add("U");
        abcs4.add("V");
        abcs4.add("W");
        abcs4.add("Y");

        //晋
        abcs5.add("A");
        abcs5.add("B");
        abcs5.add("C");
        abcs5.add("D");
        abcs5.add("E");
        abcs5.add("F");
        abcs5.add("G");
        abcs5.add("H");
        abcs5.add("J");
        abcs5.add("K");
        abcs5.add("L");
        abcs5.add("M");
        abcs5.add("N");
        abcs5.add("O");

        //蒙
        abcs6.add("A");
        abcs6.add("B");
        abcs6.add("C");
        abcs6.add("D");
        abcs6.add("E");
        abcs6.add("F");
        abcs6.add("G");
        abcs6.add("H");
        abcs6.add("J");
        abcs6.add("K");
        abcs6.add("L");
        abcs6.add("M");
        abcs6.add("O");

        //辽
        abcs7.add("A");
        abcs7.add("B");
        abcs7.add("C");
        abcs7.add("D");
        abcs7.add("E");
        abcs7.add("F");
        abcs7.add("G");
        abcs7.add("H");
        abcs7.add("J");
        abcs7.add("K");
        abcs7.add("L");
        abcs7.add("M");
        abcs7.add("N");
        abcs7.add("P");
        abcs7.add("V");

        //吉
        abcs8.add("A");
        abcs8.add("B");
        abcs8.add("C");
        abcs8.add("D");
        abcs8.add("E");
        abcs8.add("F");
        abcs8.add("G");
        abcs8.add("H");
        abcs8.add("J");
        abcs8.add("K");

        //黑
        abcs9.add("A");
        abcs9.add("B");
        abcs9.add("C");
        abcs9.add("D");
        abcs9.add("E");
        abcs9.add("F");
        abcs9.add("G");
        abcs9.add("H");
        abcs9.add("J");
        abcs9.add("K");
        abcs9.add("L");
        abcs9.add("M");
        abcs9.add("N");
        abcs9.add("P");
        abcs9.add("R");

        //沪
        abcs10.add("A");
        abcs10.add("B");
        abcs10.add("C");
        abcs10.add("D");
        abcs10.add("E");
        abcs10.add("F");
        abcs10.add("G");
        abcs10.add("H");
        abcs10.add("J");
        abcs10.add("K");
        abcs10.add("L");
        abcs10.add("M");
        abcs10.add("N");
        abcs10.add("P");
        abcs10.add("R");

        //苏
        abcs11.add("A");
        abcs11.add("B");
        abcs11.add("C");
        abcs11.add("D");
        abcs11.add("E");
        abcs11.add("F");
        abcs11.add("G");
        abcs11.add("H");
        abcs11.add("J");
        abcs11.add("K");
        abcs11.add("L");
        abcs11.add("M");
        abcs11.add("N");
        //浙
        abcs12.add("A");
        abcs12.add("B");
        abcs12.add("C");
        abcs12.add("D");
        abcs12.add("E");
        abcs12.add("F");
        abcs12.add("G");
        abcs12.add("H");
        abcs12.add("J");
        abcs12.add("K");
        abcs12.add("L");

        //皖
        abcs13.add("A");
        abcs13.add("B");
        abcs13.add("C");
        abcs13.add("D");
        abcs13.add("E");
        abcs13.add("F");
        abcs13.add("G");
        abcs13.add("H");
        abcs13.add("J");
        abcs13.add("K");
        abcs13.add("L");
        abcs13.add("M");
        abcs13.add("N");
        abcs13.add("P");
        abcs13.add("R");

        //闽
        abcs14.add("A");
        abcs14.add("B");
        abcs14.add("C");
        abcs14.add("D");
        abcs14.add("E");
        abcs14.add("F");
        abcs14.add("G");
        abcs14.add("H");
        abcs14.add("J");
        abcs14.add("K");
        abcs14.add("L");
        abcs14.add("M");
        abcs14.add("N");
        abcs14.add("P");
        abcs14.add("Q");
        abcs14.add("R");
        abcs14.add("S");
        //赣
        abcs15.add("A");
        abcs15.add("B");
        abcs15.add("C");
        abcs15.add("D");
        abcs15.add("E");
        abcs15.add("F");
        abcs15.add("G");
        abcs15.add("H");
        abcs15.add("J");
        abcs15.add("K");
        abcs15.add("L");
        abcs15.add("M");
        //豫
        abcs16.add("A");
        abcs16.add("B");
        abcs16.add("C");
        abcs16.add("D");
        abcs16.add("E");
        abcs16.add("F");
        abcs16.add("G");
        abcs16.add("H");
        abcs16.add("J");
        abcs16.add("K");
        abcs16.add("L");
        abcs16.add("M");
        abcs16.add("N");
        abcs16.add("P");
        abcs16.add("Q");
        abcs16.add("R");
        abcs16.add("S");
        abcs16.add("U");
        //鄂
        abcs17.add("A");
        abcs17.add("B");
        abcs17.add("C");
        abcs17.add("D");
        abcs17.add("E");
        abcs17.add("F");
        abcs17.add("G");
        abcs17.add("H");
        abcs17.add("J");
        abcs17.add("K");
        abcs17.add("L");
        abcs17.add("M");
        abcs17.add("N");
        abcs17.add("P");
        abcs17.add("Q");
        abcs17.add("R");
        abcs17.add("S");
        //湘
        abcs18.add("A");
        abcs18.add("B");
        abcs18.add("C");
        abcs18.add("D");
        abcs18.add("E");
        abcs18.add("F");
        abcs18.add("G");
        abcs18.add("H");
        abcs18.add("J");
        abcs18.add("K");
        abcs18.add("L");
        abcs18.add("M");
        abcs18.add("N");
        abcs18.add("S");
        abcs18.add("U");
        //粤
        abcs19.add("A");
        abcs19.add("B");
        abcs19.add("C");
        abcs19.add("D");
        abcs19.add("E");
        abcs19.add("F");
        abcs19.add("G");
        abcs19.add("H");
        abcs19.add("J");
        abcs19.add("K");
        abcs19.add("L");
        abcs19.add("M");
        abcs19.add("N");
        abcs19.add("O");
        abcs19.add("P");
        abcs19.add("Q");
        abcs19.add("R");
        abcs19.add("S");
        abcs19.add("T");
        abcs19.add("U");
        abcs19.add("V");
        abcs19.add("W");
        abcs19.add("X");
        abcs19.add("Y");
        abcs19.add("Z");
        //桂
        abcs20.add("A");
        abcs20.add("B");
        abcs20.add("C");
        abcs20.add("D");
        abcs20.add("E");
        abcs20.add("F");
        abcs20.add("G");
        abcs20.add("H");
        abcs20.add("J");
        abcs20.add("K");
        abcs20.add("L");
        abcs20.add("M");
        abcs20.add("N");
        abcs20.add("O");
        abcs20.add("P");
        abcs20.add("R");

        //渝
        abcs21.add("A");
        abcs21.add("B");
        abcs21.add("C");
        abcs21.add("D");
        abcs21.add("E");
        abcs21.add("F");
        abcs21.add("G");
        abcs21.add("H");
        //川
        abcs22.add("A");
        abcs22.add("B");
        abcs22.add("C");
        abcs22.add("D");
        abcs22.add("E");
        abcs22.add("F");
        abcs22.add("G");
        abcs22.add("H");
        abcs22.add("J");
        abcs22.add("K");
        abcs22.add("L");
        abcs22.add("M");
        abcs22.add("N");
        abcs22.add("O");
        abcs22.add("P");
        abcs22.add("Q");
        abcs22.add("R");
        abcs22.add("S");
        abcs22.add("T");
        abcs22.add("U");
        abcs22.add("V");
        abcs22.add("W");
        abcs22.add("X");
        abcs22.add("Y");
        abcs22.add("Z");
        //贵
        abcs23.add("A");
        abcs23.add("B");
        abcs23.add("C");
        abcs23.add("D");
        abcs23.add("E");
        abcs23.add("F");
        abcs23.add("G");
        abcs23.add("H");
        abcs23.add("J");
        //云
        abcs24.add("A");
        abcs24.add("B");
        abcs24.add("C");
        abcs24.add("D");
        abcs24.add("E");
        abcs24.add("F");
        abcs24.add("G");
        abcs24.add("H");
        abcs24.add("J");
        abcs24.add("K");
        abcs24.add("L");
        abcs24.add("M");
        abcs24.add("N");
        abcs24.add("O");
        abcs24.add("P");
        abcs24.add("Q");
        abcs24.add("R");
        abcs24.add("S");
        //藏
        abcs25.add("A");
        abcs25.add("B");
        abcs25.add("C");
        abcs25.add("D");
        abcs25.add("E");
        abcs25.add("F");
        abcs25.add("G");
        abcs25.add("H");
        abcs25.add("J");
        //陕
        abcs26.add("A");
        abcs26.add("B");
        abcs26.add("C");
        abcs26.add("D");
        abcs26.add("E");
        abcs26.add("F");
        abcs26.add("G");
        abcs26.add("H");
        abcs26.add("J");
        abcs26.add("K");
        abcs26.add("V");
        //甘
        abcs27.add("A");
        abcs27.add("B");
        abcs27.add("C");
        abcs27.add("D");
        abcs27.add("E");
        abcs27.add("F");
        abcs27.add("G");
        abcs27.add("H");
        abcs27.add("J");
        abcs27.add("K");
        abcs27.add("L");
        abcs27.add("M");
        abcs27.add("N");
        abcs27.add("O");
        abcs27.add("P");
        //青
        abcs28.add("A");
        abcs28.add("B");
        abcs28.add("C");
        abcs28.add("D");
        abcs28.add("E");
        abcs28.add("F");
        abcs28.add("G");
        abcs28.add("H");
        abcs28.add("O");
        //琼
        abcs29.add("A");
        abcs29.add("B");
        abcs29.add("C");
        abcs29.add("D");
        abcs29.add("E");
        abcs29.add("F");
        abcs29.add("O");
        //新
        abcs30.add("A");
        abcs30.add("B");
        abcs30.add("C");
        abcs30.add("D");
        abcs30.add("E");
        abcs30.add("F");
        abcs30.add("G");
        abcs30.add("H");
        abcs30.add("J");
        abcs30.add("K");
        abcs30.add("L");
        abcs30.add("M");
        abcs30.add("N");
        abcs30.add("O");
        abcs30.add("P");
        abcs30.add("Q");
        abcs30.add("R");
        abcs30.add("S");
        //宁
        abcs31.add("A");
        abcs31.add("B");
        abcs31.add("C");
        abcs31.add("D");
        abcs31.add("E");


        abc.add(abcs1);
        abc.add(abcs2);
        abc.add(abcs3);
        abc.add(abcs4);
        abc.add(abcs5);
        abc.add(abcs6);
        abc.add(abcs7);
        abc.add(abcs8);
        abc.add(abcs9);
        abc.add(abcs10);
        abc.add(abcs11);
        abc.add(abcs12);
        abc.add(abcs13);
        abc.add(abcs14);
        abc.add(abcs15);
        abc.add(abcs16);
        abc.add(abcs17);
        abc.add(abcs18);
        abc.add(abcs19);
        abc.add(abcs20);
        abc.add(abcs21);
        abc.add(abcs22);
        abc.add(abcs23);
        abc.add(abcs24);
        abc.add(abcs25);
        abc.add(abcs26);
        abc.add(abcs27);
        abc.add(abcs28);
        abc.add(abcs29);
        abc.add(abcs30);
        abc.add(abcs31);
    }
}
