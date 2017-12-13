package com.example.npttest.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.constant.Constant;
import com.example.npttest.util.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by liuji on 2017/10/16.
 */

public class SurplusCarParkingLot extends Activity {


    @Bind(R.id.scpl_return)
    ImageView scplReturn;
    @Bind(R.id.scpl_temporary)
    EditText scplTemporary;
    @Bind(R.id.scpl_no_temporary)
    EditText scplNoTemporary;
    @Bind(R.id.scpl_btn)
    Button scplBtn;
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surplus_car_parking_lot);
        ButterKnife.bind(this);
        LogUtils.e(App.serverurl);
        if (App.serverurl != null) {
            getEmpty(App.serverurl);
        }
    }


    @OnClick({R.id.scpl_return, R.id.scpl_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scpl_return:
                finish();
                break;
            case R.id.scpl_btn:
                if (TextUtils.isEmpty(scplTemporary.getText().toString().trim()) || TextUtils.isEmpty(scplNoTemporary.getText().toString().trim())) {
                    Toasty.error(SurplusCarParkingLot.this, "请输入修改空车位数", Toast.LENGTH_SHORT, true).show();
                } else {
                    if (App.serverurl != null) {
                        modifyEmpty(App.serverurl);
                    }
                }
                break;
        }
    }

    //查询空车位信息
    private void getEmpty(String url) {
        dialog = new ZLoadingDialog(SurplusCarParkingLot.this);
        dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();

        String postjs = "{\"cmd\":\"136\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\",\"dsv\":\"" + Constant.DSV + "\",\"spare\":\"0\",\"sign\":\"abcd\"}";

        Log.e("TAG", postjs);
        OkHttpUtils.postString().url(url)
                .content(postjs)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    dialog.dismiss();
                    JSONObject rjsonObject = new JSONObject(response);
                    String reasonjson = rjsonObject.getString("reason");
                    JSONObject resultjson = rjsonObject.getJSONObject("result");
                    JSONObject datajson = resultjson.getJSONObject("data");
                    int tlot = datajson.getInt("tlot");
                    int elot = datajson.getInt("elot");
                    if (reasonjson.equals("操作成功")) {
                        scplTemporary.setText(tlot + "");
                        scplNoTemporary.setText(elot + "");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //修改空车位

    private void modifyEmpty(String url) {
        dialog = new ZLoadingDialog(SurplusCarParkingLot.this);
        dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();

        String postjs = "{\"cmd\":\"161\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\"," +
                "\"dsv\":\"" + Constant.DSV + "\",\"tlot\":\"" + scplTemporary.getText() + "\",\"olot\":\"" +
                scplNoTemporary.getText() + "\",\"sign\":\"abcd\"}";
        Log.e("TAG", postjs);
        OkHttpUtils.postString().url(url)
                .content(postjs)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    dialog.dismiss();
                    JSONObject rjsonObject = new JSONObject(response);
                    String reasonjson = rjsonObject.getString("reason");
                    JSONObject resultjson = rjsonObject.getJSONObject("result");
                    JSONObject datajson = resultjson.getJSONObject("data");
                    int mrs = datajson.getInt("mrs");
                    if (mrs == 0) {
                        App.surpluscar = String.valueOf(Integer.parseInt(scplTemporary.getText().toString()) + Integer.parseInt(scplNoTemporary.getText().toString()));
                        //Toasty.success(SurplusCarParkingLot.this,"车位修改成功", Toast.LENGTH_SHORT,true).show();
                        /*AlertDialog.Builder normalDialog = new AlertDialog.Builder(SurplusCarParkingLot.this);
                        normalDialog.setTitle("温馨提示");
                        normalDialog.setMessage("车位修改成功");
                        normalDialog.setPositiveButton("确定", null);
                        normalDialog.setNegativeButton("关闭", null);
                        // 显示
                        normalDialog.show();*/
                        Toasty.success(SurplusCarParkingLot.this,"空车位修改成功", Toast.LENGTH_SHORT,true).show();
                        finish();
                    } else {
                        /*AlertDialog.Builder normalDialog = new AlertDialog.Builder(SurplusCarParkingLot.this);
                        normalDialog.setTitle("通知");
                        normalDialog.setMessage("车位修改失败");
                        normalDialog.setPositiveButton("确定", null);
                        normalDialog.setNegativeButton("关闭", null);
                        // 显示
                        normalDialog.show();*/
                        Toasty.error(SurplusCarParkingLot.this,"空车位修改失败", Toast.LENGTH_SHORT,true).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
