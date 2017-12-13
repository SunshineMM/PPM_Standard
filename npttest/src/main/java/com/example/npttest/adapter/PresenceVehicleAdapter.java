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
                    pztype="贵宾车";
                    break;
                case 2:
                    pztype="月票车";
                    break;
                case 3:
                    pztype="储值车";
                    break;
                case 4:
                    pztype="临时车";
                    break;
                case 5:
                    pztype="免费车";
                    break;
                case 6:
                    pztype="车位池车";
                    break;
                case 7:
                    pztype="时租车";
                    break;
            }

        switch (item.getCtype()){
            case 1:
                cartype="摩托车";
                break;
            case 2:
                cartype="小型车";
                break;
            case 3:
                cartype="中型车";
                break;
            case 4:
                cartype="大型车";
                break;
            case 5:
                cartype="运输车";
                break;
            case 6:
                cartype="备用车";
                break;
        }
        helper.setText(R.id.prece_car_num,item.getPnum());
        helper.setText(R.id.prece_carin_time,"入场时间："+ DateTools.getDate(itime*1000));
        helper.setText(R.id.prece_car_type,"车辆类型："+cartype);
        helper.setText(R.id.prece_card_type,"计费类型："+pztype);
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
