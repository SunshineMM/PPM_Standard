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

public class UseHelpCarin extends NoStatusbarActivity {

    @Bind(R.id.usehelp_carin_return)
    ImageView usehelpCarinReturn;
    @Bind(R.id.usehelp_carin_banner)
    Banner usehelpCarinBanner;
    private List<Integer> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help_carin);
        ButterKnife.bind(this);
        images.add(R.mipmap.use_help_carin1);
        images.add(R.mipmap.use_help_carin2);
        images.add(R.mipmap.use_help_carin3);
        images.add(R.mipmap.use_help_carin4);
        images.add(R.mipmap.use_help_carin5);
        usehelpCarinBanner.setImages(images).setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default).isAutoPlay(false).start();
    }

    @OnClick(R.id.usehelp_carin_return)
    public void onViewClicked() {
        finish();
    }
}
