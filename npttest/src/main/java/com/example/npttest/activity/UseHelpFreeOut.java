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

public class UseHelpFreeOut extends Activity {

    @Bind(R.id.usehelp_free_return)
    ImageView usehelpFreeReturn;
    @Bind(R.id.usehelp_free_banner)
    Banner usehelpFreeBanner;
    private List<Integer> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help_free_out);
        ButterKnife.bind(this);
        images.add(R.mipmap.use_help_freeout1);
        images.add(R.mipmap.use_help_freeout2);
        images.add(R.mipmap.use_help_freeout3);
        usehelpFreeBanner.setImages(images).setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default).isAutoPlay(false).start();
    }

    @OnClick(R.id.usehelp_free_return)
    public void onViewClicked() {
        finish();
    }
}
