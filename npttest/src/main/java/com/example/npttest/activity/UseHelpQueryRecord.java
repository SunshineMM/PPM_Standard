package com.example.npttest.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.npttest.R;
import com.example.npttest.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UseHelpQueryRecord extends NoStatusbarActivity {

    @Bind(R.id.usehelp_queryRecord_return)
    ImageView usehelpQueryRecordReturn;
    @Bind(R.id.usehelp_queryRecord_banner)
    Banner usehelpQueryRecordBanner;
    private List<Integer> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help_query_record);
        ButterKnife.bind(this);
        images.add(R.mipmap.use_help_query1);
        images.add(R.mipmap.use_help_query2);
        images.add(R.mipmap.use_help_query3);
        usehelpQueryRecordBanner.setImages(images).setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default).isAutoPlay(false).start();
    }

    @OnClick(R.id.usehelp_queryRecord_return)
    public void onViewClicked() {
        finish();
    }
}
