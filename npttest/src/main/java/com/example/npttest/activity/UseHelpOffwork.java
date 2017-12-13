package com.example.npttest.activity;

import android.app.Activity;
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

public class UseHelpOffwork extends Activity {

    @Bind(R.id.usehelp_offwork_return)
    ImageView usehelpOffworkReturn;
    @Bind(R.id.usehelp_offwork_banner)
    Banner usehelpOffworkBanner;
    private List<Integer> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help_offwork);
        ButterKnife.bind(this);
        images.add(R.mipmap.use_help_offwork1);
        images.add(R.mipmap.use_help_offwork2);
        usehelpOffworkBanner.setImages(images).setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default).isAutoPlay(false).start();
    }

    @OnClick(R.id.usehelp_offwork_return)
    public void onViewClicked() {
        finish();
    }
}
