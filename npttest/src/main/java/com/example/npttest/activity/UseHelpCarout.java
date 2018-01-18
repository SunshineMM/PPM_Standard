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

public class UseHelpCarout extends NoStatusbarActivity {

    @Bind(R.id.usehelp_carout_return)
    ImageView usehelpCaroutReturn;
    @Bind(R.id.usehelp_carout_banner)
    Banner usehelpCaroutBanner;
    private List<Integer> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help_caiout);
        ButterKnife.bind(this);
        images.add(R.mipmap.use_help_carout1);
        images.add(R.mipmap.use_help_carout2);
        images.add(R.mipmap.use_help_carout3);
        images.add(R.mipmap.use_help_carout4);
        images.add(R.mipmap.use_help_carout5);
        images.add(R.mipmap.use_help_carout6);
        images.add(R.mipmap.use_help_carout7);
        images.add(R.mipmap.use_help_carout8);
        images.add(R.mipmap.use_help_carout9);
        usehelpCaroutBanner.setImages(images).setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default).isAutoPlay(false).start();
    }

    @OnClick(R.id.usehelp_carout_return)
    public void onViewClicked() {
        finish();
    }
}
