package com.example.npttest.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.adapter.CarintoAdapter;
import com.example.npttest.camera.CameraActivity;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Carinto;
import com.example.npttest.util.LicenseKeyboardUtil_input;
import com.example.npttest.util.SPUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.MediaType;

import static com.example.npttest.util.LicenseKeyboardUtil_input.currentEditText_input;

/**
 * Created by liuji on 2017/10/21.
 */

public class QueryCarinRecord extends NoStatusbarActivity implements CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.query_carin_return)
    ImageView queryCarinReturn;
    @Bind(R.id.query_carin_camera)
    ImageView queryCarinCamera;
    @Bind(R.id.query_carin_inputbox1)
    EditText queryCarinInputbox1;
    @Bind(R.id.query_carin_inputbox2)
    EditText queryCarinInputbox2;
    @Bind(R.id.query_carin_inputbox3)
    EditText queryCarinInputbox3;
    @Bind(R.id.query_carin_inputbox4)
    EditText queryCarinInputbox4;
    @Bind(R.id.query_carin_inputbox5)
    EditText queryCarinInputbox5;
    @Bind(R.id.query_carin_inputbox6)
    EditText queryCarinInputbox6;
    @Bind(R.id.query_carin_inputbox7)
    EditText queryCarinInputbox7;
    @Bind(R.id.query_carin_inputbox8)
    EditText queryCarinInputbox8;
    @Bind(R.id.query_carin_lin_input)
    LinearLayout queryCarinLinInput;
    @Bind(R.id.query_carin_sbtn)
    SwitchButton queryCarinSbtn;
    @Bind(R.id.query_carin_rv)
    RecyclerView queryCarinRv;
    @Bind(R.id.query_carin_swipeLayout)
    SwipeRefreshLayout queryCarinSwipeLayout;
    @Bind(R.id.query_carin_btn)
    Button queryCarinBtn;
    @Bind(R.id.keyboard_view)
    KeyboardView keyboardView;
    private LicenseKeyboardUtil_input keyboardUtil;
    private EditText edits[];
    private String carnumb;
    private String comCity, stringarray;
    private static final int TOTAL_COUNTER = 0;
    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr;
    private boolean mLoadMoreEndGone = false;
    private CarintoAdapter carintoAdapter;
    private List<Carinto> list = new ArrayList<>();
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_carin_record);
        ButterKnife.bind(this);
        carintoAdapter = new CarintoAdapter(list);
        queryCarinSwipeLayout.setOnRefreshListener(this);
        carintoAdapter.setOnLoadMoreListener(this, queryCarinRv);
        queryCarinSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        queryCarinRv.setLayoutManager(new LinearLayoutManager(this));
        queryCarinRv.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent carintoInfo_intent = new Intent(QueryCarinRecord.this, CarinDetailedInfo.class);
                carintoInfo_intent.putExtra("carnum", carintoAdapter.getData().get(position).getPnum());
                carintoInfo_intent.putExtra("cartype", carintoAdapter.getData().get(position).getCtype());
                carintoInfo_intent.putExtra("pztype", carintoAdapter.getData().get(position).getCdtp());
                carintoInfo_intent.putExtra("itime", carintoAdapter.getData().get(position).getItime());
                startActivity(carintoInfo_intent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_img:
                        final AlertDialog dialog = new AlertDialog.Builder(QueryCarinRecord.this).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(QueryCarinRecord.this);
                        Glide.with(QueryCarinRecord.this).load(carintoAdapter.getData().get(position).getIurl())
                                .centerCrop()
                                .placeholder(R.mipmap.carnum_default)//占位图
                                .error(R.mipmap.carnum_default)//错误网址显示图片
                                .crossFade().into(imageView);
                        Window window = dialog.getWindow();
                        window.getDecorView().setPadding(0, 0, 0, 0);
                        window.setGravity(Gravity.CENTER);
                        window.setContentView(imageView);
                        WindowManager.LayoutParams lp = window.getAttributes();
                        lp.width = WindowManager.LayoutParams.FILL_PARENT;
                        lp.height = WindowManager.LayoutParams.FILL_PARENT;
                        window.setAttributes(lp);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        edits = new EditText[]{queryCarinInputbox1, queryCarinInputbox2, queryCarinInputbox3,
                queryCarinInputbox4, queryCarinInputbox5, queryCarinInputbox6,
                queryCarinInputbox7, queryCarinInputbox8};

        keyboardUtil = new LicenseKeyboardUtil_input(QueryCarinRecord.this, edits);

        comCity = (String) SPUtils.get(QueryCarinRecord.this, Constant.COM_CITY, "");
        char[] chars = comCity.toCharArray();
        if (TextUtils.isEmpty(comCity)) {

        } else {
            queryCarinInputbox1.setText(String.valueOf(chars[0]));
            queryCarinInputbox2.setText(String.valueOf(chars[1]));
        }

        Boolean aBoolean = (Boolean) SPUtils.get(QueryCarinRecord.this, "open_new_car", false);
        queryCarinSbtn.setCheckedImmediately(aBoolean);
        queryCarinSbtn.setOnCheckedChangeListener(this);
        if (aBoolean) {
            queryCarinInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
        } else {
            queryCarinInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
        }
    }

    //点击空白处隐藏键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keyboardUtil.hideKeyboard();
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.query_carin_return, R.id.query_carin_camera, R.id.query_carin_inputbox1,
            R.id.query_carin_inputbox2, R.id.query_carin_inputbox3, R.id.query_carin_inputbox4,
            R.id.query_carin_inputbox5, R.id.query_carin_inputbox6, R.id.query_carin_inputbox7,
            R.id.query_carin_inputbox8, R.id.query_carin_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query_carin_return:
                finish();
                break;
            case R.id.query_carin_camera:
                Intent intent1 = new Intent(QueryCarinRecord.this, CameraActivity.class);
                intent1.putExtra("camera", true);
                //startActivity(intent);
                startActivityForResult(intent1, 0x11);
                break;
            case R.id.query_carin_inputbox1:
                currentEditText_input = 0;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox2:
                currentEditText_input = 1;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox3:
                currentEditText_input = 2;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox4:
                currentEditText_input = 3;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox5:
                currentEditText_input = 4;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox6:
                currentEditText_input = 5;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox7:
                currentEditText_input = 6;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_inputbox8:
                currentEditText_input = 7;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carin_btn:
                stringarray = queryCarinInputbox1.getText().toString() +
                        queryCarinInputbox2.getText().toString() +
                        queryCarinInputbox3.getText().toString() +
                        queryCarinInputbox4.getText().toString() +
                        queryCarinInputbox5.getText().toString() +
                        queryCarinInputbox6.getText().toString() +
                        queryCarinInputbox7.getText().toString() +
                        queryCarinInputbox8.getText().toString();

                if (TextUtils.isEmpty(queryCarinInputbox1.getText().toString()) ||
                        TextUtils.isEmpty(queryCarinInputbox2.getText().toString()) ||
                        TextUtils.isEmpty(queryCarinInputbox3.getText().toString()) ||
                        TextUtils.isEmpty(queryCarinInputbox4.getText().toString()) ||
                        TextUtils.isEmpty(queryCarinInputbox5.getText().toString()) ||
                        TextUtils.isEmpty(queryCarinInputbox6.getText().toString()) ||
                        TextUtils.isEmpty(queryCarinInputbox7.getText().toString())) {
                    Toasty.error(this, getString(R.string.enter_correct_license_plate_number), Toast.LENGTH_SHORT, true).show();
                } else {
                    list.clear();
                    if (App.serverurl != null) {
                        getrecord(App.serverurl);
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        carnumb = data.getStringExtra("number").toString();
        if (carnumb.equals("null")) {

        } else {
            //设置每个edit的值
            char[] carnumber = carnumb.toCharArray();
            if (carnumber.length == 7) {
                queryCarinInputbox8.setVisibility(View.GONE);
                queryCarinInputbox1.setText(String.valueOf(carnumber[0]));
                queryCarinInputbox2.setText(String.valueOf(carnumber[1]));
                queryCarinInputbox3.setText(String.valueOf(carnumber[2]));
                queryCarinInputbox4.setText(String.valueOf(carnumber[3]));
                queryCarinInputbox5.setText(String.valueOf(carnumber[4]));
                queryCarinInputbox6.setText(String.valueOf(carnumber[5]));
                queryCarinInputbox7.setText(String.valueOf(carnumber[6]));
            } else if (carnumber.length == 8) {
                queryCarinInputbox1.setText(String.valueOf(carnumber[0]));
                queryCarinInputbox2.setText(String.valueOf(carnumber[1]));
                queryCarinInputbox3.setText(String.valueOf(carnumber[2]));
                queryCarinInputbox4.setText(String.valueOf(carnumber[3]));
                queryCarinInputbox5.setText(String.valueOf(carnumber[4]));
                queryCarinInputbox6.setText(String.valueOf(carnumber[5]));
                queryCarinInputbox7.setText(String.valueOf(carnumber[6]));
                queryCarinInputbox8.setText(String.valueOf(carnumber[7]));
            }

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            SPUtils.put(QueryCarinRecord.this, "open_new_car", true);
            queryCarinInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
            if (!TextUtils.isEmpty(queryCarinInputbox7.getText())) {
                currentEditText_input = 7;
                initcolor1();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edits[currentEditText_input].setBackground(getDrawable(R.drawable.keyboard_bg_red));
                }
            }

        } else {
            SPUtils.put(QueryCarinRecord.this, "open_new_car", false);
            queryCarinInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
            if (!TextUtils.isEmpty(queryCarinInputbox6.getText())) {
                currentEditText_input = 6;
                initcolor1();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edits[currentEditText_input].setBackground(getDrawable(R.drawable.keyboard_bg_red));
                }
            }

        }
    }

    private void initcolor1() {
        edits[0].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[1].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[2].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[3].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[4].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[5].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[6].setBackgroundResource(R.drawable.keyboard_bg_white);
        edits[7].setBackgroundResource(R.drawable.keyboard_bg_white);
    }

    //获取入场车辆记录
    public void getrecord(String url) {
        if (!isFinishing()) {
            dialog = new ZLoadingDialog(this);
            dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                    .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                    .setHintText("Loading...")
                    .setHintTextColor(Color.parseColor("#55BEB7"))
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                    .show();
        }
        String getrcord = "{\"cmd\":\"155\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE +
                "\",\"dsv\":\"" + Constant.DSV + "\",\"qtype\":\"0\",\"num\":\"" + stringarray + "\",\"stime\":\"\"," +
                "\"etime\":\"\",\"spare\":\" \",\"sign\":\"abcd\"}";
        Log.e("TAG", getrcord);
        OkHttpUtils.postString().url(url)
                .content(getrcord)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(QueryCarinRecord.this, getString(R.string.please_check_the_network), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                if (dialog != null) {
                    dialog.cancel();
                }
                try {
                    JSONObject rjsonObject = new JSONObject(response);
                    JSONObject resultjsOb = rjsonObject.getJSONObject("result");
                    JSONArray listjsAr = resultjsOb.getJSONArray("list");
                    if (listjsAr.length() > 0) {
                        for (int i = 0; i < listjsAr.length(); i++) {
                            JSONObject jsonObject = listjsAr.getJSONObject(i);
                            Carinto carinto = new Carinto();
                            carinto.setCnum(jsonObject.getString("cnum"));
                            carinto.setPnum(jsonObject.getString("pnum"));
                            carinto.setItime(jsonObject.getInt("itime"));
                            carinto.setIurl(jsonObject.getString("iurl"));
                            carinto.setSid(jsonObject.getString("sid"));
                            carinto.setCtype(jsonObject.getString("ctype"));
                            carinto.setCdtp(jsonObject.getString("cdtp"));
                            list.add(carinto);
                            Log.e("TAG", "入场车辆：" + list.size());
                            handler.sendEmptyMessage(0x123);
                        }
                    } else {
                        Toasty.error(QueryCarinRecord.this, getString(R.string.not_check_the_admission_vehicle_records), Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                if (carintoAdapter != null && queryCarinRv != null) {
                    carintoAdapter.notifyDataSetChanged();
                    queryCarinRv.setAdapter(carintoAdapter);
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        carintoAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                if (App.serverurl != null) {
                    getrecord(App.serverurl);
                }
                //rvListFg1.setLayoutManager(new LinearLayoutManager(getActivity()));
                //carintoAdapter.setNewData(list);
                carintoAdapter.notifyDataSetChanged();
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                if (queryCarinSwipeLayout != null) {
                    queryCarinSwipeLayout.setRefreshing(false);
                }
                carintoAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {
        queryCarinSwipeLayout.setEnabled(false);
        if (carintoAdapter.getData().size() < PAGE_SIZE) {
            carintoAdapter.loadMoreEnd(true);//加载完成不再加载
        } else {
            if (mCurrentCounter >= TOTAL_COUNTER) {
                carintoAdapter.loadMoreEnd();//default visible
                //数据全部加载完毕
                carintoAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
            } else {
                if (isErr) {
                    carintoAdapter.addData(list);
                    mCurrentCounter = carintoAdapter.getData().size();
                    carintoAdapter.loadMoreComplete();
                } else {
                    isErr = true;
                    carintoAdapter.loadMoreFail();
                }
            }
            queryCarinSwipeLayout.setEnabled(true);
        }
    }
}
