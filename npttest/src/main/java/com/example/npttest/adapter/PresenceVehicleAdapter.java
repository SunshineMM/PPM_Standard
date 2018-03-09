package com.example.npttest.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.npttest.R;
import com.example.npttest.entity.Prese;
import com.example.npttest.tool.DateTools;

import java.util.List;

/**
 * Created by liuji on 2017/9/1.
 */

public class PresenceVehicleAdapter extends BaseQuickAdapter<Prese,BaseViewHolder> {

    private String cartype,pztype;
    public PresenceVehicleAdapter(@Nullable List<Prese> data) {
        super(R.layout.presenceitem,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Prese item) {
        long itime=item.getItime();
            switch (item.getCdtp()){
                case 1:
                    pztype =  mContext.getString(R.string.VIP_car);
                    break;
                case 2:
                    pztype =  mContext.getString(R.string.monthly_ticket_car);
                    break;
                case 3:
                    pztype =  mContext.getString(R.string.reserve_car);
                    break;
                case 4:
                    pztype =  mContext.getString(R.string.temporary_car);
                    break;
                case 5:
                    pztype =  mContext.getString(R.string.free_car);
                    break;
                case 6:
                    pztype =  mContext.getString(R.string.parking_pool_car);
                    break;
                case 7:
                    pztype =  mContext.getString(R.string.car_rental);
                    break;
            }

        switch (item.getCtype()){
            case 1:
                cartype = mContext.getString(R.string.motorcycle);
                break;
            case 2:
                cartype = mContext.getString(R.string.compacts);
                break;
            case 3:
                cartype = mContext.getString(R.string.Intermediate);
                break;
            case 4:
                cartype = mContext.getString(R.string.large_vehicle);
                break;
            case 5:
                cartype = mContext.getString(R.string.transporter);
                break;
            case 6:
                cartype = mContext.getString(R.string.spare_car);
                break;
        }
        helper.setText(R.id.prece_car_num,item.getPnum());
        helper.setText(R.id.prece_carin_time,mContext.getString(R.string.admission_time_)+ DateTools.getDate(itime*1000));
        helper.setText(R.id.prece_car_type,mContext.getString(R.string.vehicle_type_)+cartype);
        helper.setText(R.id.prece_card_type,mContext.getString(R.string.billing_type_)+pztype);
        // 加载网络图片
        Glide.with(mContext).load(item.getIurl())
                .centerCrop()
                .placeholder(R.mipmap.carnum_default)//占位图
                .error(R.mipmap.carnum_default)//错误网址显示图片
                .crossFade().into((ImageView) helper.getView(R.id.prece_img));
       // Log.e("TAG","url**********"+Content.OssImgUrl+"/"+item.getIurl());
        helper.addOnClickListener(R.id.prece_modify);
        helper.addOnClickListener(R.id.prece_outcar);
        helper.addOnClickListener(R.id.prece_img);
    }
}
