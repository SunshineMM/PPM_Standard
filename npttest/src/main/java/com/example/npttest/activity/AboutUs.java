package com.example.npttest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.constant.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.MediaType;
import util.UpdateAppUtils;

public class AboutUs extends Activity {

    @Bind(R.id.aboutUs_return)
    ImageView aboutUsReturn;
    @Bind(R.id.aboutUs_check_version)
    LinearLayout aboutUsCheckVersion;
    @Bind(R.id.aboutUs_update_img)
    ImageView aboutUsUpdateImg;
    private boolean update;
    private int dsv;
    private String updateurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        get_update();
    }

    @OnClick({R.id.aboutUs_return, R.id.aboutUs_check_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aboutUs_return:
                finish();
                break;
            case R.id.aboutUs_check_version:
                if (update) {
                    UpdateAppUtils.from(AboutUs.this)
                            .checkBy(UpdateAppUtils.CHECK_BY_VERSION_CODE) //更新检测方式，默认为VersionCode
                            .serverVersionCode(dsv)
                            .serverVersionName(String.valueOf(((double)dsv)/100))
                            .apkPath("http://" + updateurl)
                            .downloadBy(UpdateAppUtils.DOWNLOAD_BY_APP) //下载方式：app下载、手机浏览器下载。默认app下载
                            .isForce(false) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                            .update();
                } else {
                    Toasty.info(AboutUs.this, "已经是最新版本", Toast.LENGTH_SHORT, true).show();
                }
                break;
        }
    }

    /**
     * 判断是否需要更新
     */

    private void get_update() {
        String jsons = "{\"cmd\":\"13\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\",\"dsv\":\"" + Constant.DSV + "\",\"task\":\"10\",\"sign\":\"abcd\"}";
        Log.e("TAG", jsons);
        OkHttpUtils.postString().url(App.serverurl)
                .content(jsons)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", response);
                try {
                    JSONObject rpjson = new JSONObject(response);
                    String reasonjson = rpjson.getString("reason");
                    JSONObject resultjson = rpjson.getJSONObject("result");
                    JSONObject datajsonObject = resultjson.getJSONObject("data");
                    // Log.e("TAG", datajsonObject.toString());
                    int rs = datajsonObject.getInt("rs");
                    dsv = datajsonObject.getInt("dsv");
                    updateurl = datajsonObject.getString("url");
                    Log.e("TAG", dsv + "");
                    if (rs == 0) {
                        update = false;
                        aboutUsUpdateImg.setVisibility(View.GONE);
                    } else if (rs == 1) {
                        update = true;
                        aboutUsUpdateImg.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}