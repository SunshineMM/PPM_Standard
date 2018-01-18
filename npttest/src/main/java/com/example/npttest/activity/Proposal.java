package com.example.npttest.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.npttest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by liuji on 2017/9/18.
 */

public class Proposal extends NoStatusbarActivity {

    @Bind(R.id.propo_iv)
    ImageView propoIv;
    @Bind(R.id.propo_et)
    EditText propoEt;
    @Bind(R.id.propo_btn)
    Button propoBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal);
        ButterKnife.bind(this);
        propoEt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                Proposal.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = Proposal.this.getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                //Log.e("TAG", "Size: " + heightDifference+"屏幕高度："+screenHeight);

                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

        });
    }

    @OnClick({R.id.propo_iv, R.id.propo_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.propo_iv:
                finish();
                break;
            case R.id.propo_btn:
                if (TextUtils.isEmpty(propoEt.getText().toString())){
                    //Toast.makeText(this, "请输入建议", Toast.LENGTH_SHORT).show();
                    Toasty.error(this, "请输入建议", Toast.LENGTH_SHORT,true).show();
                }else {
                    //Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                    Toasty.success(this, "提交成功", Toast.LENGTH_SHORT,true).show();
                }
                break;
        }
    }
}
