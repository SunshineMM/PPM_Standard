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

public class UseHelpModifyInfo extends NoStatusbarActivity {

    @Bind(R.id.usehelp_modify_return)
    ImageView usehelpModifyReturn;
    @Bind(R.id.usehelp_modify_banner)
    Banner usehelpModifyBanner;
    private List<Integer> images = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help_modify_info);
        ButterKnife.bind(this);
        images.add(R.mipmap.use_help_modify1);
        images.add(R.mipmap.use_help_modify2);

        usehelpModifyBanner.setImages(images).setImageLoader(new GlideImageLoader())
                .setBannerAnimation(Transformer.Default).isAutoPlay(false).start();
    }

    @OnClick(R.id.usehelp_modify_return)
    public void onViewClicked() {
        finish();
    }
}
