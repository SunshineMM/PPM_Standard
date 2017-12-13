package com.example.npttest.activity;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npttest.R;
import com.example.npttest.util.MD5Utils;
import com.example.npttest.util.MPermissionHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/7/31.
 */

public class Test extends Activity implements MPermissionHelper.PermissionCallBack {


    @Bind(R.id.img_1)
    ImageView img1;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.text_btn)
    Button textBtn;
    private String bitmapPath;
    private Bitmap bitmap = null;
    private MPermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
        permissionHelper = new MPermissionHelper(this);
        //申请权限
        permissionHelper.requestPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        );
    }

    //权限回调
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.handleRequestPermissionsResult(requestCode, this, grantResults);
    }

    @OnClick({R.id.img_1, R.id.tv_1, R.id.tv_2, R.id.text_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_1:
                break;
            case R.id.tv_1:
                break;
            case R.id.tv_2:
                break;
            case R.id.text_btn:
                tv1.setText(MD5Utils.encode("123456"));
                Log.e("TAG", MD5Utils.encode("123456"));
                break;
        }
    }


    @Override
    public void permissionRegisterSuccess(String... permissions) {
        Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void permissionRegisterError(String... permissions) {
        permissionHelper.showGoSettingPermissionsDialog("定位");
    }

    protected void onDestroy() {
        super.onDestroy();
        permissionHelper.destroy();
    }
}
