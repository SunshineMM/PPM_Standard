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
import com.example.npttest.adapter.ChargeAdapter;
import com.example.npttest.camera.CameraActivity;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Charge;
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

public class QueryChargeRecord extends NoStatusbarActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, CompoundButton.OnCheckedChangeListener {


    @Bind(R.id.query_charge_return)
    ImageView queryChargeReturn;
    @Bind(R.id.query_charge_camera)
    ImageView queryChargeCamera;
    @Bind(R.id.query_charge_inputbox1)
    EditText queryChargeInputbox1;
    @Bind(R.id.query_charge_inputbox2)
    EditText queryChargeInputbox2;
    @Bind(R.id.query_charge_inputbox3)
    EditText queryChargeInputbox3;
    @Bind(R.id.query_charge_inputbox4)
    EditText queryChargeInputbox4;
    @Bind(R.id.query_charge_inputbox5)
    EditText queryChargeInputbox5;
    @Bind(R.id.query_charge_inputbox6)
    EditText queryChargeInputbox6;
    @Bind(R.id.query_charge_inputbox7)
    EditText queryChargeInputbox7;
    @Bind(R.id.query_charge_inputbox8)
    EditText queryChargeInputbox8;
    @Bind(R.id.query_charge_lin_input)
    LinearLayout queryChargeLinInput;
    @Bind(R.id.query_charge_sbtn)
    SwitchButton queryChargeSbtn;
    @Bind(R.id.query_charge_rv)
    RecyclerView queryChargeRv;
    @Bind(R.id.query_charge_swipeLayout)
    SwipeRefreshLayout queryChargeSwipeLayout;
    @Bind(R.id.query_charge_btn)
    Button queryChargeBtn;
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
    private ChargeAdapter chargeAdapter;
    private List<Charge> list = new ArrayList<>();
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_charge_record);
        ButterKnife.bind(this);

        chargeAdapter = new ChargeAdapter(list);
        queryChargeSwipeLayout.setOnRefreshListener(this);
        chargeAdapter.setOnLoadMoreListener(this, queryChargeRv);
        queryChargeSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        queryChargeRv.setLayoutManager(new LinearLayoutManager(this));
        queryChargeRv.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent chargeinfo_intent = new Intent(QueryChargeRecord.this, ChargeDetailedInfo.class);
                chargeinfo_intent.putExtra("carnum", chargeAdapter.getData().get(position).getPnum());
                chargeinfo_intent.putExtra("cartype", chargeAdapter.getData().get(position).getCtype());
                chargeinfo_intent.putExtra("pztype", chargeAdapter.getData().get(position).getCdtp());
                chargeinfo_intent.putExtra("nmon", chargeAdapter.getData().get(position).getNmon());//应收金额
                chargeinfo_intent.putExtra("smon", chargeAdapter.getData().get(position).getSmon());//优惠金额
                chargeinfo_intent.putExtra("rmon", chargeAdapter.getData().get(position).getRmon());//实收金额
                chargeinfo_intent.putExtra("itime", chargeAdapter.getData().get(position).getItime());
                chargeinfo_intent.putExtra("ctime", chargeAdapter.getData().get(position).getCtime());
                chargeinfo_intent.putExtra("ptype", chargeAdapter.getData().get(position).getPtype());
                startActivity(chargeinfo_intent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_img:
                        final AlertDialog dialog = new AlertDialog.Builder(QueryChargeRecord.this).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(QueryChargeRecord.this);
                        Glide.with(QueryChargeRecord.this).load(chargeAdapter.getData().get(position).getOurl())
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

        edits = new EditText[]{queryChargeInputbox1, queryChargeInputbox2, queryChargeInputbox3,
                queryChargeInputbox4, queryChargeInputbox5, queryChargeInputbox6,
                queryChargeInputbox7, queryChargeInputbox8};

        keyboardUtil = new LicenseKeyboardUtil_input(QueryChargeRecord.this, edits);

        comCity = (String) SPUtils.get(QueryChargeRecord.this, Constant.COM_CITY, "");
        char[] chars = comCity.toCharArray();
        if (TextUtils.isEmpty(comCity)) {

        } else {
            queryChargeInputbox1.setText(String.valueOf(chars[0]));
            queryChargeInputbox2.setText(String.valueOf(chars[1]));
        }

        Boolean aBoolean = (Boolean) SPUtils.get(QueryChargeRecord.this, "open_new_car", false);
        queryChargeSbtn.setCheckedImmediately(aBoolean);
        queryChargeSbtn.setOnCheckedChangeListener(this);
        if (aBoolean) {
            queryChargeInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
        } else {
            queryChargeInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
        }
    }

    //点击空白处隐藏键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keyboardUtil.hideKeyboard();
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.query_charge_return, R.id.query_charge_camera, R.id.query_charge_inputbox1, R.id.query_charge_inputbox2, R.id.query_charge_inputbox3, R.id.query_charge_inputbox4, R.id.query_charge_inputbox5, R.id.query_charge_inputbox6, R.id.query_charge_inputbox7, R.id.query_charge_inputbox8, R.id.query_charge_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query_charge_return:
                finish();
                break;
            case R.id.query_charge_camera:
                Intent intent1 = new Intent(QueryChargeRecord.this, CameraActivity.class);
                intent1.putExtra("camera", true);
                //startActivity(intent);
                startActivityForResult(intent1, 0x11);
                break;
            case R.id.query_charge_inputbox1:
                currentEditText_input = 0;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox2:
                currentEditText_input = 1;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox3:
                currentEditText_input = 2;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox4:
                currentEditText_input = 3;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox5:
                currentEditText_input = 4;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox6:
                currentEditText_input = 5;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox7:
                currentEditText_input = 6;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_inputbox8:
                currentEditText_input = 7;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_charge_btn:
                stringarray = queryChargeInputbox1.getText().toString() +
                        queryChargeInputbox2.getText().toString() +
                        queryChargeInputbox3.getText().toString() +
                        queryChargeInputbox4.getText().toString() +
                        queryChargeInputbox5.getText().toString() +
                        queryChargeInputbox6.getText().toString() +
                        queryChargeInputbox7.getText().toString() +
                        queryChargeInputbox8.getText().toString();

                if (TextUtils.isEmpty(queryChargeInputbox1.getText().toString()) ||
                        TextUtils.isEmpty(queryChargeInputbox2.getText().toString()) ||
                        TextUtils.isEmpty(queryChargeInputbox3.getText().toString()) ||
                        TextUtils.isEmpty(queryChargeInputbox4.getText().toString()) ||
                        TextUtils.isEmpty(queryChargeInputbox5.getText().toString()) ||
                        TextUtils.isEmpty(queryChargeInputbox6.getText().toString()) ||
                        TextUtils.isEmpty(queryChargeInputbox7.getText().toString())) {
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

    //获取收费车辆记录
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
        String getrcord = "{\"cmd\":\"157\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE +
                "\",\"dsv\":\"" + Constant.DSV + "\",\"qtype\":\"0\",\"num\":\"" + stringarray + "\",\"stime\":\"\"," +
                "\"etime\":\"\",\"spare\":\" \",\"sign\":\"abcd\"}";
        Log.e("TAG", getrcord);
        OkHttpUtils.postString().url(url)
                .content(getrcord)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(QueryChargeRecord.this, getString(R.string.please_check_the_network), Toast.LENGTH_SHORT).show();
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
                            Charge charge = new Charge();
                            charge.setCnum(jsonObject.getString("cnum"));
                            charge.setPnum(jsonObject.getString("pnum"));
                            charge.setItime(jsonObject.getLong("itime"));
                            charge.setCtime(jsonObject.getLong("ctime"));
                            charge.setNmon(jsonObject.getInt("nmon"));
                            charge.setRmon(jsonObject.getInt("rmon"));
                            charge.setPmon(jsonObject.getInt("pmon"));
                            charge.setSmon(jsonObject.getInt("smon"));
                            charge.setDmon(jsonObject.getInt("dmon"));
                            charge.setPreson(jsonObject.getInt("preson"));
                            charge.setPtype(jsonObject.getString("ptype"));
                            charge.setCdtp(jsonObject.getString("cdtp"));
                            charge.setCtype(jsonObject.getString("ctype"));
                            charge.setIurl(jsonObject.getString("iurl"));
                            charge.setOurl(jsonObject.getString("ourl"));
                            list.add(charge);
                            Log.e("TAG", "入场车辆：" + list.size());
                            handler.sendEmptyMessage(0x123);
                        }
                    } else {
                        Toasty.error(QueryChargeRecord.this, "未查询到该记录", Toast.LENGTH_SHORT, true).show();
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
                if (chargeAdapter != null && queryChargeRv != null) {
                    chargeAdapter.notifyDataSetChanged();
                    queryChargeRv.setAdapter(chargeAdapter);
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        chargeAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                if (App.serverurl != null) {
                    getrecord(App.serverurl);
                }
                //rvListFg1.setLayoutManager(new LinearLayoutManager(getActivity()));
                //carintoAdapter.setNewData(list);
                chargeAdapter.notifyDataSetChanged();
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                if (queryChargeSwipeLayout != null) {
                    queryChargeSwipeLayout.setRefreshing(false);
                }
                chargeAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {
        queryChargeSwipeLayout.setEnabled(false);
        if (chargeAdapter.getData().size() < PAGE_SIZE) {
            chargeAdapter.loadMoreEnd(true);//加载完成不再加载
        } else {
            if (mCurrentCounter >= TOTAL_COUNTER) {
                chargeAdapter.loadMoreEnd();//default visible
                //数据全部加载完毕
                chargeAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
            } else {
                if (isErr) {
                    chargeAdapter.addData(list);
                    mCurrentCounter = chargeAdapter.getData().size();
                    chargeAdapter.loadMoreComplete();
                } else {
                    isErr = true;
                    chargeAdapter.loadMoreFail();
                }
            }
            queryChargeSwipeLayout.setEnabled(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            SPUtils.put(QueryChargeRecord.this, "open_new_car", true);
            queryChargeInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
            if (!TextUtils.isEmpty(queryChargeInputbox7.getText())) {
                currentEditText_input = 7;
                initcolor1();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edits[currentEditText_input].setBackground(getDrawable(R.drawable.keyboard_bg_red));
                }
            }

        } else {
            SPUtils.put(QueryChargeRecord.this, "open_new_car", false);
            queryChargeInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
            if (!TextUtils.isEmpty(queryChargeInputbox6.getText())) {
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
                queryChargeInputbox8.setVisibility(View.GONE);
                queryChargeInputbox1.setText(String.valueOf(carnumber[0]));
                queryChargeInputbox2.setText(String.valueOf(carnumber[1]));
                queryChargeInputbox3.setText(String.valueOf(carnumber[2]));
                queryChargeInputbox4.setText(String.valueOf(carnumber[3]));
                queryChargeInputbox5.setText(String.valueOf(carnumber[4]));
                queryChargeInputbox6.setText(String.valueOf(carnumber[5]));
                queryChargeInputbox7.setText(String.valueOf(carnumber[6]));
            } else if (carnumber.length == 8) {
                queryChargeInputbox1.setText(String.valueOf(carnumber[0]));
                queryChargeInputbox2.setText(String.valueOf(carnumber[1]));
                queryChargeInputbox3.setText(String.valueOf(carnumber[2]));
                queryChargeInputbox4.setText(String.valueOf(carnumber[3]));
                queryChargeInputbox5.setText(String.valueOf(carnumber[4]));
                queryChargeInputbox6.setText(String.valueOf(carnumber[5]));
                queryChargeInputbox7.setText(String.valueOf(carnumber[6]));
                queryChargeInputbox8.setText(String.valueOf(carnumber[7]));
            }

        }

    }
}
