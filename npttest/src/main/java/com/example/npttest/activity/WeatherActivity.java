package com.example.npttest.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.npttest.R;
import com.example.npttest.adapter.WeatherAdapter;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Weather;
import com.example.npttest.manager.LinearLayoutManagerWrapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;


/**
 * Created by liuji on 2017/10/25.
 */

public class WeatherActivity extends Activity {

    @Bind(R.id.wth_return)
    ImageView wthReturn;
    @Bind(R.id.wth_city)
    TextView wthCity;
    @Bind(R.id.wth_temp)
    TextView wthTemp;
    @Bind(R.id.wth_temp2)
    TextView wthTemp2;
    @Bind(R.id.wth_aqi)
    TextView wthAqi;
    @Bind(R.id.wth_wthimg)
    ImageView wthWthimg;
    @Bind(R.id.wth_weather)
    TextView wthWeather;
    @Bind(R.id.wth_winddirect)
    TextView wthWinddirect;
    @Bind(R.id.wth_time)
    TextView wthTime;
    @Bind(R.id.wth_humidity)
    TextView wthHumidity;
    @Bind(R.id.wth_pressure)
    TextView wthPressure;
    @Bind(R.id.wth_pm)
    TextView wthPm;
    @Bind(R.id.wth_rv)
    RecyclerView wthRv;
    @Bind(R.id.wth_swipeLayout)
    SwipeRefreshLayout wthSwipeLayout;
    private List<Weather> list = new ArrayList<>();
    private WeatherAdapter weatherAdapter;
    private String img;
    private ZLoadingDialog dialog, dialog1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        ButterKnife.bind(this);
        list.clear();
        getweather();
        initAdapter();
        wthSwipeLayout.setEnabled(false);
        wthRv.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
    }

    @OnClick({R.id.wth_return, R.id.wth_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wth_return:
                finish();
                break;
            case R.id.wth_city:
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                builder.setTitle("请输入城市");//设置标题
                //实例化两个控件
                final EditText citytv = new EditText(WeatherActivity.this);
                //将值添加到控件
                citytv.setText(wthCity.getText());
                //将控件添加到线性布局
                LinearLayout linearLayout = new LinearLayout(WeatherActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);//设置线性布局的排列方式
                linearLayout.addView(citytv);
                builder.setView(linearLayout);//将线性布局添加到build
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wthCity.setText(citytv.getText().toString().trim());
                        list.clear();
                        getweather_city(citytv.getText().toString().trim());
                    }
                });
                builder.show();
                break;
        }
    }

    //设置适配器
    private void initAdapter() {
        weatherAdapter = new WeatherAdapter(list);
        weatherAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        wthRv.setAdapter(weatherAdapter);
    }

    //获取天气
    private void getweather() {
        if (!isFinishing()) {
            dialog = new ZLoadingDialog(WeatherActivity.this);
            dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                    .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                    .setHintText("Loading...")
                    .setHintTextColor(Color.parseColor("#55BEB7"))
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                    .show();
        }

        String url = "http://jisutianqi.market.alicloudapi.com/weather/query";
        String appcode = "1d737752e21e44598c9eb8acf7ae2165";
        /*String jindu = "113.944539";
        String weidu = "22.59016";*/
        OkHttpUtils.get().url(url)
                .addHeader("Authorization", "APPCODE " + appcode)
                .addParams("location", Constant.lat + "," + Constant.lng)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toasty.error(WeatherActivity.this, "无法查询到天气", Toast.LENGTH_SHORT, true).show();
                dialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject rjsonObject = new JSONObject(response);
                    String msg = rjsonObject.getString("msg");
                    Log.e("TAG", msg);
                    JSONObject resultjsonObject = rjsonObject.getJSONObject("result");
                    String city = resultjsonObject.getString("city");
                    String weather = resultjsonObject.getString("weather");
                    String temp = resultjsonObject.getString("temp");
                    String temphigh = resultjsonObject.getString("temphigh");
                    String templow = resultjsonObject.getString("templow");
                    img = resultjsonObject.getString("img");
                    String winddirect = resultjsonObject.getString("winddirect");//风
                    String windpower = resultjsonObject.getString("windpower");//风力
                    String humidity = resultjsonObject.getString("humidity");//湿度
                    String pressure = resultjsonObject.getString("pressure");//气压
                    String date = resultjsonObject.getString("date");
                    JSONObject aqijsonObject = resultjsonObject.getJSONObject("aqi");
                    String quality = aqijsonObject.getString("quality");//空气质量
                    String pm25 = aqijsonObject.getString("pm2_5");
                    JSONArray dailyjsonarray = resultjsonObject.getJSONArray("daily");

                    for (int i = 0; i < dailyjsonarray.length(); i++) {
                        JSONObject jsonObject = dailyjsonarray.getJSONObject(i);
                        Weather weather1 = new Weather();
                        weather1.setWeek(jsonObject.getString("week"));
                        weather1.setWeather(jsonObject.getJSONObject("day").getString("weather"));
                        weather1.setImg(jsonObject.getJSONObject("day").getString("img"));
                        weather1.setTemphigh(jsonObject.getJSONObject("day").getString("temphigh"));
                        weather1.setTemplow(jsonObject.getJSONObject("night").getString("templow"));
                        weather1.setWinddirect(jsonObject.getJSONObject("day").getString("winddirect"));
                        weather1.setWindpower(jsonObject.getJSONObject("day").getString("windpower"));
                        list.add(weather1);
                    }
                    wthTime.setText(date);
                    wthCity.setText(city);
                    wthWeather.setText(weather);
                    wthTemp.setText(temp + "°");
                    wthTemp2.setText(templow + "°/" + temphigh + "°");
                    wthAqi.setText("空气" + quality);
                    wthWinddirect.setText(windpower + winddirect);
                    wthHumidity.setText("湿度" + humidity);
                    wthPressure.setText("气压" + pressure);
                    wthPm.setText("Pm" + pm25);
                    getimg();
                    dialog.dismiss();
                    Log.e("TAG", "天气：" + list.size());
                    handler.sendEmptyMessage(0x123);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //手动获取天气
    private void getweather_city(String city) {
        if (!isFinishing()) {
            dialog1 = new ZLoadingDialog(WeatherActivity.this);
            dialog1.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                    .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                    .setHintText("Loading...")
                    .setHintTextColor(Color.parseColor("#55BEB7"))
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                    .show();
        }
        String url = "http://jisutianqi.market.alicloudapi.com/weather/query";
        String appcode = "1d737752e21e44598c9eb8acf7ae2165";
        String jindu = "113.944539";
        String weidu = "22.59016";

        OkHttpUtils.get().url(url)
                .addHeader("Authorization", "APPCODE " + appcode)
                .addParams("city", city)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toasty.error(WeatherActivity.this, "未查询到该城市", Toast.LENGTH_SHORT, true).show();
                dialog1.dismiss();
                Log.e("TAG", "无返回天气数据");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject rjsonObject = new JSONObject(response);
                    String msg = rjsonObject.getString("msg");
                    Log.e("TAG", msg);
                    dialog1.dismiss();
                    if (msg.equals("ok")) {
                        JSONObject resultjsonObject = rjsonObject.getJSONObject("result");
                        String city = resultjsonObject.getString("city");
                        String weather = resultjsonObject.getString("weather");
                        String temp = resultjsonObject.getString("temp");
                        String temphigh = resultjsonObject.getString("temphigh");
                        String templow = resultjsonObject.getString("templow");
                        img = resultjsonObject.getString("img");
                        String winddirect = resultjsonObject.getString("winddirect");//风
                        String windpower = resultjsonObject.getString("windpower");//风力
                        String humidity = resultjsonObject.getString("humidity");//湿度
                        String pressure = resultjsonObject.getString("pressure");//气压
                        String date = resultjsonObject.getString("date");
                        JSONObject aqijsonObject = resultjsonObject.getJSONObject("aqi");
                        String quality = aqijsonObject.getString("quality");//空气质量
                        String pm25 = aqijsonObject.getString("pm2_5");
                        JSONArray dailyjsonarray = resultjsonObject.getJSONArray("daily");
                        for (int i = 0; i < dailyjsonarray.length(); i++) {
                            JSONObject jsonObject = dailyjsonarray.getJSONObject(i);
                            Weather weather1 = new Weather();
                            weather1.setWeek(jsonObject.getString("week"));
                            weather1.setWeather(jsonObject.getJSONObject("day").getString("weather"));
                            weather1.setImg(jsonObject.getJSONObject("day").getString("img"));
                            weather1.setTemphigh(jsonObject.getJSONObject("day").getString("temphigh"));
                            weather1.setTemplow(jsonObject.getJSONObject("night").getString("templow"));
                            weather1.setWinddirect(jsonObject.getJSONObject("day").getString("winddirect"));
                            weather1.setWindpower(jsonObject.getJSONObject("day").getString("windpower"));
                            list.add(weather1);
                        }
                        wthTime.setText(date);
                        wthCity.setText(city);
                        wthWeather.setText(weather);
                        wthTemp.setText(temp + "°");
                        wthTemp2.setText(templow + "°/" + temphigh + "°");
                        wthAqi.setText("空气" + quality);
                        wthWinddirect.setText(windpower + winddirect);
                        wthHumidity.setText("湿度 " + humidity);
                        wthPressure.setText("气压 " + pressure);
                        wthPm.setText("Pm2.5 " + pm25);
                        getimg();
                        Log.e("TAG", "天气：" + list.size());
                        handler.sendEmptyMessage(0x123);
                    } else {
                        Toasty.error(WeatherActivity.this, "未查询到该城市", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                if (weatherAdapter != null && wthRv != null) {
                    weatherAdapter.notifyDataSetChanged();
                    wthRv.setAdapter(weatherAdapter);
                }
            }
        }
    };

    private void getimg() {
        switch (Integer.parseInt(img)) {
            case 0:
                wthWthimg.setImageResource(R.mipmap.ic_qing_0);
                break;
            case 1:
                wthWthimg.setImageResource(R.mipmap.ic_duoyun_1);
                break;
            case 2:
                wthWthimg.setImageResource(R.mipmap.ic_yin_2);
                break;
            case 3:
                wthWthimg.setImageResource(R.mipmap.ic_zhenyu_3);
                break;
            case 4:
                wthWthimg.setImageResource(R.mipmap.ic_leizy_4);
                break;
            case 5:
                wthWthimg.setImageResource(R.mipmap.ic_leizy_5);
                break;
            case 6:
                wthWthimg.setImageResource(R.mipmap.ic_yujiaxue_6);
                break;
            case 7:
                wthWthimg.setImageResource(R.mipmap.ic_xiaoyu_7);
                break;
            case 8:
                wthWthimg.setImageResource(R.mipmap.ic_zhongyu_8);
                break;
            case 9:
                wthWthimg.setImageResource(R.mipmap.ic_dayu_9);
                break;
            case 10:
                wthWthimg.setImageResource(R.mipmap.ic_baoyu_10);
                break;
            case 11:
                wthWthimg.setImageResource(R.mipmap.ic_dabaoyu_11);
                break;
            case 12:
                wthWthimg.setImageResource(R.mipmap.ic_tedaby_12);
                break;
            case 13:
                wthWthimg.setImageResource(R.mipmap.ic_zhenxue_13);
                break;
            case 14:
                wthWthimg.setImageResource(R.mipmap.ic_xiaoxue_14);
                break;
            case 15:
                wthWthimg.setImageResource(R.mipmap.ic_zhongxue_15);
                break;
            case 16:
                wthWthimg.setImageResource(R.mipmap.ic_daxue_16);
                break;
            case 17:
                wthWthimg.setImageResource(R.mipmap.ic_baoxue_17);
                break;
            case 18:
                wthWthimg.setImageResource(R.mipmap.ic_wu_18);
                break;
        }
    }


}
