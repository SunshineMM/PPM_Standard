package com.example.npttest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.npttest.R;
import com.example.npttest.entity.Weather;

import java.util.List;

/**
 * Created by liuji on 2017/10/25.
 */

public class WeatherAdapter extends BaseQuickAdapter<Weather,BaseViewHolder> {
    public WeatherAdapter(@Nullable List<Weather> data) {
        super(R.layout.weather_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Weather item) {
        helper.setText(R.id.wthitem_week,item.getWeek());
        helper.setText(R.id.wthitem_weather,item.getWeather());
        helper.setText(R.id.wthitem_temp,item.getTemphigh()+"°/"+item.getTemplow()+"°");
        helper.setText(R.id.wthitem_winddirect,item.getWindpower());
        switch (Integer.parseInt(item.getImg())){
            case 0:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_qing_0);
                break;
            case 1:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_duoyun_1);
                break;
            case 2:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_yin_2);
                break;
            case 3:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_zhenyu_3);
                break;
            case 4:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_leizy_4);
                break;
            case 5:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_leizy_5);
                break;
            case 6:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_yujiaxue_6);
                break;
            case 7:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_xiaoyu_7);
                break;
            case 8:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_zhongyu_8);
                break;
            case 9:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_dayu_9);
                break;
            case 10:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_baoyu_10);
                break;
            case 11:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_dabaoyu_11);
                break;
            case 12:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_tedaby_12);
                break;
            case 13:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_zhenxue_13);
                break;
            case 14:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_xiaoxue_14);
                break;
            case 15:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_zhongxue_15);
                break;
            case 16:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_daxue_16);
                break;
            case 17:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_baoxue_17);
                break;
            case 18:
                helper.setImageResource(R.id.wthitem_wthimg,R.mipmap.ic_wu_18);
                break;
        }
        //helper.setText(R.id.wthitem_week,item.getWeek());
    }
}
