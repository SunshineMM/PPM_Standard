package com.example.npttest.activity;

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
import com.example.npttest.adapter.CaroutAdapter;
import com.example.npttest.camera.CameraActivity;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Carout;
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

public class QueryCaroutRecord extends NoStatusbarActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, CompoundButton.OnCheckedChangeListener {


    @Bind(R.id.query_carout_return)
    ImageView queryCaroutReturn;
    @Bind(R.id.query_carout_camera)
    ImageView queryCaroutCamera;
    @Bind(R.id.query_carout_inputbox1)
    EditText queryCaroutInputbox1;
    @Bind(R.id.query_carout_inputbox2)
    EditText queryCaroutInputbox2;
    @Bind(R.id.query_carout_inputbox3)
    EditText queryCaroutInputbox3;
    @Bind(R.id.query_carout_inputbox4)
    EditText queryCaroutInputbox4;
    @Bind(R.id.query_carout_inputbox5)
    EditText queryCaroutInputbox5;
    @Bind(R.id.query_carout_inputbox6)
    EditText queryCaroutInputbox6;
    @Bind(R.id.query_carout_inputbox7)
    EditText queryCaroutInputbox7;
    @Bind(R.id.query_carout_inputbox8)
    EditText queryCaroutInputbox8;
    @Bind(R.id.query_carout_lin_input)
    LinearLayout queryCaroutLinInput;
    @Bind(R.id.query_carout_sbtn)
    SwitchButton queryCaroutSbtn;
    @Bind(R.id.query_carout_rv)
    RecyclerView queryCaroutRv;
    @Bind(R.id.query_carout_swipeLayout)
    SwipeRefreshLayout queryCaroutSwipeLayout;
    @Bind(R.id.query_carout_btn)
    Button queryCaroutBtn;
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
    private CaroutAdapter caroutAdapter;
    private List<Carout> list = new ArrayList<>();
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_carout_record);
        ButterKnife.bind(this);

        caroutAdapter = new CaroutAdapter(list);
        queryCaroutSwipeLayout.setOnRefreshListener(this);
        caroutAdapter.setOnLoadMoreListener(this, queryCaroutRv);
        queryCaroutSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        queryCaroutRv.setLayoutManager(new LinearLayoutManager(this));
        queryCaroutRv.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent caroutInfo_intent = new Intent(QueryCaroutRecord.this, CaroutDetailedInfo.class);
                caroutInfo_intent.putExtra("carnum", caroutAdapter.getData().get(position).getPnum());
                caroutInfo_intent.putExtra("cartype", caroutAdapter.getData().get(position).getCtype());
                caroutInfo_intent.putExtra("pztype", caroutAdapter.getData().get(position).getCdtp());
                caroutInfo_intent.putExtra("ctime", caroutAdapter.getData().get(position).getEtime());
                startActivity(caroutInfo_intent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_img:
                        final AlertDialog dialog = new AlertDialog.Builder(QueryCaroutRecord.this).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(QueryCaroutRecord.this);
                        Glide.with(QueryCaroutRecord.this).load(caroutAdapter.getData().get(position).getEurl())
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

        edits = new EditText[]{queryCaroutInputbox1, queryCaroutInputbox2, queryCaroutInputbox3,
                queryCaroutInputbox4, queryCaroutInputbox5, queryCaroutInputbox6,
                queryCaroutInputbox7, queryCaroutInputbox8};

        keyboardUtil = new LicenseKeyboardUtil_input(QueryCaroutRecord.this, edits);

        comCity = (String) SPUtils.get(QueryCaroutRecord.this, Constant.COM_CITY, "");
        char[] chars = comCity.toCharArray();
        if (TextUtils.isEmpty(comCity)) {

        } else {
            queryCaroutInputbox1.setText(String.valueOf(chars[0]));
            queryCaroutInputbox2.setText(String.valueOf(chars[1]));
        }

        Boolean aBoolean = (Boolean) SPUtils.get(QueryCaroutRecord.this, "open_new_car", false);
        queryCaroutSbtn.setCheckedImmediately(aBoolean);
        queryCaroutSbtn.setOnCheckedChangeListener(this);
        if (aBoolean) {
            queryCaroutInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
        } else {
            queryCaroutInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
        }
    }

    //点击空白处隐藏键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keyboardUtil.hideKeyboard();
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.query_carout_return, R.id.query_carout_camera, R.id.query_carout_inputbox1, R.id.query_carout_inputbox2, R.id.query_carout_inputbox3, R.id.query_carout_inputbox4, R.id.query_carout_inputbox5, R.id.query_carout_inputbox6, R.id.query_carout_inputbox7, R.id.query_carout_inputbox8, R.id.query_carout_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query_carout_return:
                finish();
                break;
            case R.id.query_carout_camera:
                Intent intent1 = new Intent(QueryCaroutRecord.this, CameraActivity.class);
                intent1.putExtra("camera", true);
                //startActivity(intent);
                startActivityForResult(intent1, 0x11);
                break;
            case R.id.query_carout_inputbox1:
                currentEditText_input = 0;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox2:
                currentEditText_input = 1;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox3:
                currentEditText_input = 2;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox4:
                currentEditText_input = 3;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox5:
                currentEditText_input = 4;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox6:
                currentEditText_input = 5;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox7:
                currentEditText_input = 6;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_inputbox8:
                currentEditText_input = 7;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_carout_btn:
                stringarray = queryCaroutInputbox1.getText().toString() +
                        queryCaroutInputbox2.getText().toString() +
                        queryCaroutInputbox3.getText().toString() +
                        queryCaroutInputbox4.getText().toString() +
                        queryCaroutInputbox5.getText().toString() +
                        queryCaroutInputbox6.getText().toString() +
                        queryCaroutInputbox7.getText().toString() +
                        queryCaroutInputbox8.getText().toString();

                if (TextUtils.isEmpty(queryCaroutInputbox1.getText().toString()) ||
                        TextUtils.isEmpty(queryCaroutInputbox2.getText().toString()) ||
                        TextUtils.isEmpty(queryCaroutInputbox3.getText().toString()) ||
                        TextUtils.isEmpty(queryCaroutInputbox4.getText().toString()) ||
                        TextUtils.isEmpty(queryCaroutInputbox5.getText().toString()) ||
                        TextUtils.isEmpty(queryCaroutInputbox6.getText().toString()) ||
                        TextUtils.isEmpty(queryCaroutInputbox7.getText().toString())) {
                    Toasty.error(this, "请输入正确的车牌号", Toast.LENGTH_SHORT, true).show();
                } else {
                    list.clear();
                    if (App.serverurl != null) {
                        getrecord(App.serverurl);
                    }
                }
                break;
        }
    }

    //获取出场车辆记录
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
        String getrcord = "{\"cmd\":\"156\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE +
                "\",\"dsv\":\"" + Constant.DSV + "\",\"qtype\":\"0\",\"num\":\"" + stringarray + "\",\"stime\":\"\"," +
                "\"etime\":\"\",\"spare\":\" \",\"sign\":\"abcd\"}";
        Log.e("TAG", getrcord);
        OkHttpUtils.postString().url(url)
                .content(getrcord)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(QueryCaroutRecord.this, "无网络", Toast.LENGTH_SHORT).show();
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
                            Carout carout = new Carout();
                            carout.setCnum(jsonObject.getString("cnum"));
                            carout.setPnum(jsonObject.getString("pnum"));
                            carout.setEtime(jsonObject.getInt("etime"));
                            carout.setEurl(jsonObject.getString("eurl"));
                            carout.setSid(jsonObject.getString("sid"));
                            carout.setCtype(jsonObject.getString("ctype"));
                            carout.setCdtp(jsonObject.getString("cdtp"));
                            list.add(carout);
                            handler.sendEmptyMessage(0x123);
                        }
                    } else {
                        Toasty.error(QueryCaroutRecord.this, "未查询到该记录", Toast.LENGTH_SHORT, true).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                if (caroutAdapter != null && queryCaroutRv != null) {
                    caroutAdapter.notifyDataSetChanged();
                    queryCaroutRv.setAdapter(caroutAdapter);
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        caroutAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                if (App.serverurl != null) {
                    getrecord(App.serverurl);
                }
                //rvListFg1.setLayoutManager(new LinearLayoutManager(getActivity()));
                //carintoAdapter.setNewData(list);
                caroutAdapter.notifyDataSetChanged();
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                if (queryCaroutSwipeLayout != null) {
                    queryCaroutSwipeLayout.setRefreshing(false);
                }
                caroutAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {
        queryCaroutSwipeLayout.setEnabled(false);
        if (caroutAdapter.getData().size() < PAGE_SIZE) {
            caroutAdapter.loadMoreEnd(true);//加载完成不再加载
        } else {
            if (mCurrentCounter >= TOTAL_COUNTER) {
                caroutAdapter.loadMoreEnd();//default visible
                //数据全部加载完毕
                caroutAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
            } else {
                if (isErr) {
                    caroutAdapter.addData(list);
                    mCurrentCounter = caroutAdapter.getData().size();
                    caroutAdapter.loadMoreComplete();
                } else {
                    isErr = true;
                    caroutAdapter.loadMoreFail();
                }
            }
            queryCaroutSwipeLayout.setEnabled(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            SPUtils.put(QueryCaroutRecord.this, "open_new_car", true);
            queryCaroutInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
            if (!TextUtils.isEmpty(queryCaroutInputbox7.getText())) {
                currentEditText_input = 7;
                initcolor1();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edits[currentEditText_input].setBackground(getDrawable(R.drawable.keyboard_bg_red));
                }
            }

        } else {
            SPUtils.put(QueryCaroutRecord.this, "open_new_car", false);
            queryCaroutInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
            if (!TextUtils.isEmpty(queryCaroutInputbox6.getText())) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        carnumb = data.getStringExtra("number").toString();
        if (carnumb.equals("null")) {

        } else {
            //设置每个edit的值
            char[] carnumber = carnumb.toCharArray();
            if (carnumber.length == 7) {
                queryCaroutInputbox8.setVisibility(View.GONE);
                queryCaroutInputbox1.setText(String.valueOf(carnumber[0]));
                queryCaroutInputbox2.setText(String.valueOf(carnumber[1]));
                queryCaroutInputbox3.setText(String.valueOf(carnumber[2]));
                queryCaroutInputbox4.setText(String.valueOf(carnumber[3]));
                queryCaroutInputbox5.setText(String.valueOf(carnumber[4]));
                queryCaroutInputbox6.setText(String.valueOf(carnumber[5]));
                queryCaroutInputbox7.setText(String.valueOf(carnumber[6]));
            } else if (carnumber.length == 8) {
                queryCaroutInputbox1.setText(String.valueOf(carnumber[0]));
                queryCaroutInputbox2.setText(String.valueOf(carnumber[1]));
                queryCaroutInputbox3.setText(String.valueOf(carnumber[2]));
                queryCaroutInputbox4.setText(String.valueOf(carnumber[3]));
                queryCaroutInputbox5.setText(String.valueOf(carnumber[4]));
                queryCaroutInputbox6.setText(String.valueOf(carnumber[5]));
                queryCaroutInputbox7.setText(String.valueOf(carnumber[6]));
                queryCaroutInputbox8.setText(String.valueOf(carnumber[7]));
            }

        }

    }
}
