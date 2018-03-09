package com.example.npttest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.npttest.R;
import com.example.npttest.entity.Carout;
import com.example.npttest.tool.DateTools;

import java.util.List;

/**
 * Created by liuji on 2017/8/28.
 */

public class CaroutAdapter extends BaseQuickAdapter<Carout,BaseViewHolder> {
    public CaroutAdapter(@Nullable List<Carout> data) {
        super(R.layout.recorditem,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Carout item) {
            long etime=item.getEtime();
            helper.setText(R.id.item_car_num,item.getPnum());
            helper.setText(R.id.item_car_type,mContext.getString(R.string.vehicle_type_)+item.getCtype());
            helper.setText(R.id.item_card_type,mContext.getString(R.string.billing_type_)+item.getCdtp());
            helper.setText(R.id.item_carin_time, mContext.getString(R.string.admission_time_)+DateTools.getDate(etime*1000));
            // 加载网络图片
            Glide.with(mContext).load(item.getEurl())
                    .centerCrop()
                    .placeholder(R.mipmap.carnum_default)//占位图
                    .error(R.mipmap.carnum_default)//错误网址显示图片
                    .crossFade().into((ImageView) helper.getView(R.id.item_img));
            //Log.e("TAG","url**********"+Content.OssImgUrl+"/"+item.getEurl());
            helper.addOnClickListener(R.id.item_img);
        }
}
