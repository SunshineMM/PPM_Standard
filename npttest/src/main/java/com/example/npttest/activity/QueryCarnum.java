package com.example.npttest.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.adapter.QuerynumAdapter;
import com.example.npttest.camera.CameraActivity;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Querynum;
import com.example.npttest.util.LicenseKeyboardUtil_input;
import com.example.npttest.util.SPUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
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
 * Created by liuji on 2017/8/12.
 */

public class QueryCarnum extends NoStatusbarActivity implements CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    public static final String INPUT_LICENSE_COMPLETE = "me.kevingo.licensekeyboard.input.comp";
    @Bind(R.id.query_return)
    ImageView queryReturn;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.query_camera)
    ImageView queryCamera;
    @Bind(R.id.query_inputbox1)
    EditText queryInputbox1;
    @Bind(R.id.query_inputbox2)
    EditText queryInputbox2;
    @Bind(R.id.query_inputbox3)
    EditText queryInputbox3;
    @Bind(R.id.query_inputbox4)
    EditText queryInputbox4;
    @Bind(R.id.query_inputbox5)
    EditText queryInputbox5;
    @Bind(R.id.query_inputbox6)
    EditText queryInputbox6;
    @Bind(R.id.query_inputbox7)
    EditText queryInputbox7;
    @Bind(R.id.query_inputbox8)
    EditText queryInputbox8;
    @Bind(R.id.query_lin_input)
    LinearLayout queryLinInput;
    @Bind(R.id.query_sbtn)
    SwitchButton querySbtn;
    @Bind(R.id.query_rv_list)
    RecyclerView queryRvList;
    @Bind(R.id.query_swipeLayout)
    SwipeRefreshLayout querySwipeLayout;
    @Bind(R.id.query_btn)
    Button queryBtn;
    @Bind(R.id.keyboard_view)
    KeyboardView keyboardView;
    private LicenseKeyboardUtil_input keyboardUtil;
    private EditText edits[];
    private String stringEdit;
    private String carnumb, cname, cartype;
    private int ctype, cdtp;
    private long ctime;
    private ZLoadingDialog dialog1;
    private String comCity, stringarray;
    private int rstat, ptype, preson;
    private String jfType;//计费类型
    private String comfirmYy;//确认原因
    private String carnum;
    public static CardView cardView;
    private static final int TOTAL_COUNTER = 0;
    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr;
    private boolean mLoadMoreEndGone = false;
    private QuerynumAdapter querynumAdapter;
    private List<Querynum> list = new ArrayList<>();
    private int mdposition,coutposition;
    SynthesizerListener mSynListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_carnum);
        ButterKnife.bind(this);
        querynumAdapter = new QuerynumAdapter(list);
        querySwipeLayout.setOnRefreshListener(this);
        querynumAdapter.setOnLoadMoreListener(this, queryRvList);
        querySwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        queryRvList.setLayoutManager(new LinearLayoutManager(this));
        queryRvList.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(PresenceVehicle.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent carinfointent = new Intent(QueryCarnum.this, PresenceVehicleInfo.class);
                carinfointent.putExtra("carnum", querynumAdapter.getData().get(position).getPnum());
                carinfointent.putExtra("cartype", querynumAdapter.getData().get(position).getCtype());
                carinfointent.putExtra("pztype", querynumAdapter.getData().get(position).getCdtp());
                carinfointent.putExtra("itime", querynumAdapter.getData().get(position).getItime());
                startActivity(carinfointent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(QueryCarnum.this, Integer.toString(position) + "long", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.prece_modify:
                        //Toast.makeText(PresenceVehicle.this, "修改", Toast.LENGTH_SHORT).show();
                        Intent modiftintent = new Intent(QueryCarnum.this, ModifyCarnum.class);
                        modiftintent.putExtra("number", querynumAdapter.getData().get(position).getPnum());
                        modiftintent.putExtra("cartype", querynumAdapter.getData().get(position).getCtype());
                        modiftintent.putExtra("pztype", querynumAdapter.getData().get(position).getCdtp());
                        modiftintent.putExtra("pvrefresh", true);
                        modiftintent.putExtra("sid", querynumAdapter.getData().get(position).getSid());
                        startActivity(modiftintent);
                        mdposition=position;
                        break;
                    case R.id.prece_outcar:
                        carnum = querynumAdapter.getData().get(position).getPnum();
                        ctype = querynumAdapter.getData().get(position).getCtype();
                        //cartype=querynumAdapter.getData().get(position).getCtype();
                        carout(App.serverurl);
                        coutposition=position;
                        break;
                    case R.id.prece_img:
                        final AlertDialog dialog = new AlertDialog.Builder(QueryCarnum.this).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(QueryCarnum.this);
                        Glide.with(QueryCarnum.this).load(querynumAdapter.getData().get(position).getIurl())
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
        edits = new EditText[]{queryInputbox1, queryInputbox2, queryInputbox3,
                queryInputbox4, queryInputbox5, queryInputbox6,
                queryInputbox7, queryInputbox8};

        //输入车牌完成后的intent过滤器
        IntentFilter finishFilter = new IntentFilter(INPUT_LICENSE_COMPLETE);
        keyboardUtil = new LicenseKeyboardUtil_input(QueryCarnum.this, edits);

        /*comCity = (String) SPUtils.get(QueryCarnum.this, Constant.COM_CITY, "");
        char[] chars = comCity.toCharArray();
        if (TextUtils.isEmpty(comCity)) {

        } else {
            queryInputbox1.setText(String.valueOf(chars[0]));
            queryInputbox2.setText(String.valueOf(chars[1]));
        }*/
        Boolean aBoolean = (Boolean) SPUtils.get(QueryCarnum.this, "open_new_car", false);
        querySbtn.setCheckedImmediately(aBoolean);
        querySbtn.setOnCheckedChangeListener(this);
        if (aBoolean) {
            queryInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
        } else {
            queryInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
        }

    }

    @Override
    protected void onStart() {
        if (!App.pvRefresh) {
            Log.e("TAG", "onstart 刷新");
            onRefresh();
            App.pvRefresh = true;
        }
        super.onStart();
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

    @OnClick({R.id.query_return, R.id.query_inputbox1, R.id.query_inputbox2, R.id.query_inputbox3,
            R.id.query_inputbox4, R.id.query_inputbox5, R.id.query_inputbox6, R.id.query_inputbox7,
            R.id.query_inputbox8, R.id.query_btn, R.id.query_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.query_return:
                finish();
                break;
            case R.id.query_inputbox1:
                currentEditText_input = 0;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox2:
                currentEditText_input = 1;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox3:
                currentEditText_input = 2;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox4:
                currentEditText_input = 3;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox5:
                currentEditText_input = 4;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox6:
                currentEditText_input = 5;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox7:
                currentEditText_input = 6;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_inputbox8:
                currentEditText_input = 7;
                initcolor1();
                edits[LicenseKeyboardUtil_input.currentEditText_input].setBackgroundResource(R.drawable.keyboard_bg_red);
                keyboardUtil.showKeyboard();
                break;
            case R.id.query_btn:
                stringarray = queryInputbox1.getText().toString().trim() +
                        queryInputbox2.getText().toString().trim() +
                        queryInputbox3.getText().toString().trim() +
                        queryInputbox4.getText().toString().trim() +
                        queryInputbox5.getText().toString().trim() +
                        queryInputbox6.getText().toString().trim() +
                        queryInputbox7.getText().toString().trim() +
                        queryInputbox8.getText().toString().trim();
                char[] carnumber = stringarray.toCharArray();
                if (carnumber.length < 3) {
                    Toasty.error(this, "请输入正确的车牌号", Toast.LENGTH_SHORT).show();
                } else {
                    stringEdit = (queryInputbox1.getText().toString() == null || "".equals(queryInputbox1.getText().toString().trim()) ? "*" : queryInputbox1.getText().toString()) +
                            (queryInputbox2.getText().toString() == null || "".equals(queryInputbox2.getText().toString().trim()) ? "*" : queryInputbox2.getText().toString()) +
                            (queryInputbox3.getText().toString() == null || "".equals(queryInputbox3.getText().toString().trim()) ? "*" : queryInputbox3.getText().toString()) +
                            (queryInputbox4.getText().toString() == null || "".equals(queryInputbox4.getText().toString().trim()) ? "*" : queryInputbox4.getText().toString()) +
                            (queryInputbox5.getText().toString() == null || "".equals(queryInputbox5.getText().toString().trim()) ? "*" : queryInputbox5.getText().toString()) +
                            (queryInputbox6.getText().toString() == null || "".equals(queryInputbox6.getText().toString().trim()) ? "*" : queryInputbox6.getText().toString()) +
                            (queryInputbox7.getText().toString() == null || "".equals(queryInputbox7.getText().toString().trim()) ? "*" : queryInputbox7.getText().toString());
                    //Toast.makeText(this, stringEdit, Toast.LENGTH_SHORT).show();
                    if (App.serverurl!=null){
                        list.clear();
                        query_Car(App.serverurl);
                    }

                    //keyboardUtil.hideKeyboard();
                }

                break;
            case R.id.query_camera:
                //jumpVideoRecog();
                Intent intent1 = new Intent(QueryCarnum.this, CameraActivity.class);
                intent1.putExtra("camera", true);
                //startActivity(intent);
                startActivityForResult(intent1, 0x11);
                break;

        }
    }

    //查询车辆{"cmd":"143","type":"2","code":"abcd","dsv":"110","ptype":"0","num":"鲁B1FL39","spare":"0","sign":"abcd"}
    public void query_Car(String url) {
        if (!isFinishing()) {
            dialog1 = new ZLoadingDialog(this);
            dialog1.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                    .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                    .setHintText("Loading...")
                    .setHintTextColor(Color.parseColor("#55BEB7"))
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                    .show();
        }
        String queryjs = "{\"cmd\":\"160\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\",\"dsv\":\"" + Constant.DSV + "\",\"num\":\"" + stringarray + "\",\"spare\":\"0\",\"sign\":\"abcd\"}";

        Log.e("TAG", queryjs);
        OkHttpUtils.postString().url(url)
                .content(queryjs)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    dialog1.dismiss();
                    JSONObject rjsonObject = new JSONObject(response);
                    String reasonjson = rjsonObject.getString("reason");
                    JSONObject resultjson = rjsonObject.getJSONObject("result");
                    JSONArray listjsAr = resultjson.getJSONArray("list");
                    if (listjsAr.length() > 0) {
                        for (int i = listjsAr.length() - 1; i >= 0; i--) {
                            JSONObject jsonObject = listjsAr.getJSONObject(i);
                            Querynum querynum = new Querynum();
                            querynum.setPnum(jsonObject.getString("pnum"));
                            querynum.setCtype(jsonObject.getInt("ctype"));
                            querynum.setCdtp(jsonObject.getInt("cdtp"));
                            querynum.setItime(jsonObject.getLong("itime"));
                            querynum.setIurl(jsonObject.getString("iurl"));
                            querynum.setSid(jsonObject.getString("sid"));
                            list.add(querynum);
                        }
                    } else {
                        Toasty.error(QueryCarnum.this, "未查询到该车辆", Toast.LENGTH_SHORT, true).show();
                    }
                    Log.e("TAG", "查询在场车辆：" + list.size());
                    handler.sendEmptyMessage(0x123);
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
                if (querynumAdapter != null && queryRvList != null) {
                    querynumAdapter.notifyDataSetChanged();
                    queryRvList.setAdapter(querynumAdapter);
                }
            }
        }
    };

    private void jfjudge() {
        /*switch (ctype) {
            case 1:
                cartype = "摩托车";
                break;
            case 2:
                cartype = "小型车";
                break;
            case 3:
                cartype = "中型车";
                break;
            case 4:
                cartype = "大型车";
                break;
            case 5:
                cartype = "运输车";
                break;
            case 6:
                cartype = "备用车";
                break;
        }*/

        switch (cdtp) {
            case 1:
                jfType = "贵宾车";
                break;
            case 2:
                jfType = "月票车";
                break;
            case 3:
                jfType = "储值车";
                break;
            case 4:
                jfType = "临时车";
                break;
            case 5:
                jfType = "免费车";
                break;
            case 6:
                jfType = "车位池车";
                break;
            case 7:
                jfType = "时租车";
                break;
        }
    }

    //点击空白处隐藏键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keyboardUtil.hideKeyboard();
        return super.onTouchEvent(event);
    }

    //车辆出场
    private void carout(String url) {
        Log.e("TAG", "多少次");
        dialog1 = new ZLoadingDialog(QueryCarnum.this);
        dialog1.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();
        //{"cmd":"140","type":"2","code":"17083B3DE","dsv":"110","ptype":"0","io":"0",
        // "num":"京B1FL39","ctype":"2","spare":"0","sign":"abcd"}

        String intocar_jS ="{\"cmd\":\"140\",\"type\":\""+ Constant.TYPE+"\",\"code\":\""+ Constant.CODE+"\",\"dsv\":\""
                + Constant.DSV+"\",\"ptype\":\"0\",\"io\":\"1\",\"num\":\""+carnum+"\",\"ctype\":\""+ctype+
                "\",\"muna\":\"1\",\"spare\":\"0\",\"sign\":\"abcd\"}";
        Log.e("TAG", intocar_jS);
        OkHttpUtils.postString().url(url)
                .content(intocar_jS)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("TAG", "成功");
                Log.e("TAG", response);
                dialog1.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String reasonjson = jsonObject.getString("reason");//操作成功
                    JSONObject resultjson = jsonObject.getJSONObject("result");
                    JSONObject datajson = resultjson.getJSONObject("data");
                    String carnum = datajson.getString("num");//车牌号
                    rstat = datajson.getInt("rstat");//放行状态
                    //rstat=2;
                    ptype = datajson.getInt("ptype");//凭证类型（卡或车牌）
                    ctype = datajson.getInt("ctype");//车辆类型
                    cdtp = datajson.getInt("cdtp");//计费类型（月票车）
                    long ctime = datajson.getLong("ctime");//计费时间
                    preson = datajson.getInt("preson");//原因
                    long itime = datajson.getLong("itime");//入场时间
                    double nmon = datajson.getInt("nmon");//应收金额
                    double rmon = datajson.getInt("rmon");//实收金额
                    double smon = datajson.getInt("smon");//折扣金额
                    String sid = datajson.getString("sid");
                    //判断
                    jfjudge();
                    if (rstat == 0) {
                        carout_start_voice();
                        //自动放行
                        Intent zdintent = new Intent(QueryCarnum.this, CaroutSuccessful.class);
                        zdintent.putExtra("carnum", carnum);
                        zdintent.putExtra("jfType", jfType);
                        zdintent.putExtra("ctype", ctype);
                        zdintent.putExtra("ctime", ctime);
                        zdintent.putExtra("itime", itime);
                        zdintent.putExtra("pvrefresh", false);
                        startActivity(zdintent);
                        finish();
                        //Toast.makeText(InputCarnum.this, "自动放行", Toast.LENGTH_SHORT).show();
                    } else if (rstat == 1) {
                        //确认放行
                        confirmjudge();
                        Intent qrintent = new Intent(QueryCarnum.this, CaroutConfirmPass.class);
                        qrintent.putExtra("carnum", carnum);
                        qrintent.putExtra("jfType", jfType);
                        qrintent.putExtra("ctype", ctype);
                        qrintent.putExtra("itime", itime);
                        qrintent.putExtra("ctime", ctime);
                        qrintent.putExtra("comfirmYy", comfirmYy);
                        qrintent.putExtra("sid", sid);
                        qrintent.putExtra("cdtp", cdtp);
                        qrintent.putExtra("pvrefresh", false);
                        startActivity(qrintent);

                    } else if (rstat == 2) {
                        //收费放行
                        chargejudge();
                        Intent sfintent = new Intent(QueryCarnum.this, CaroutChargeActivity.class);
                        sfintent.putExtra("carnum", carnum);
                        sfintent.putExtra("jfType", jfType);
                        sfintent.putExtra("ctype", ctype);
                        sfintent.putExtra("itime", itime);
                        sfintent.putExtra("ctime", ctime);
                        sfintent.putExtra("comfirmYy", comfirmYy);
                        sfintent.putExtra("nmon", nmon);
                        sfintent.putExtra("rmon", rmon);
                        sfintent.putExtra("smon", smon);
                        sfintent.putExtra("sid", sid);
                        sfintent.putExtra("cdtp", cdtp);
                        sfintent.putExtra("pvrefresh", false);
                        startActivity(sfintent);
                    } else if (rstat == 3) {
                        //禁止通行
                        prohibitjudge();
                        Intent jzintent = new Intent(QueryCarnum.this, ProhibitPass.class);
                        jzintent.putExtra("carnum", carnum);
                        jzintent.putExtra("jfType", jfType);
                        jzintent.putExtra("ctype", ctype);
                        jzintent.putExtra("comfirmYy", comfirmYy);
                        startActivity(jzintent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void carout_start_voice() {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "100");//设置音量，范围0~100
        //mTts.setParameter(SpeechConstant.PITCH, "50");// 设置音调
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端

        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        // mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        //3.开始合成
        // mTts.startSpeaking(carnum+"出场成功！", mSynListener);
        char[] carnumber = carnum.toCharArray();
        if (carnumber.length == 7) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + "，" + "出场成功。", mSynListener);
        } else if (carnumber.length == 8) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + String.valueOf(carnumber[7]) + "，" + "出场成功。", mSynListener);
        }

        //合成监听器
        mSynListener = new SynthesizerListener() {
            //会话结束回调接口，没有错误时，error为null
            public void onCompleted(SpeechError error) {
                System.out.println("error--------" + error);
            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
            public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            }

            //开始播放
            public void onSpeakBegin() {
                System.out.println("开始播放");
            }

            //暂停播放
            public void onSpeakPaused() {
            }

            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
            public void onSpeakProgress(int percent, int beginPos, int endPos) {
            }

            //恢复播放回调接口
            public void onSpeakResumed() {
            }

            //会话事件回调接口
            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            }

        };
    }


    private void prohibitjudge() {
        switch (preson) {
            case 0:
                comfirmYy = "黑名单";
                break;
            case 1:
                comfirmYy = "有效期未开始";
                break;
            case 2:
                comfirmYy = "已过期";
                break;
            case 3:
                comfirmYy = "重复进出";
                break;
            case 4:
                comfirmYy = "值班员信息有误";
                break;
            case 5:
                comfirmYy = "未授权";
                break;
            case 6:
                comfirmYy = "需要收费，未找到在场车辆信息";
                break;
            case 7:
                comfirmYy = "通道禁止通行";
                break;
            case 8:
                comfirmYy = "车位池满禁止通行";
                break;
            case 9:
                comfirmYy = "车位满禁止通行";
                break;
            case 10:
                comfirmYy = "无效请求";
                break;
        }
    }

    private void chargejudge() {
        switch (preson) {
            case 0:
                comfirmYy = "临时车";
                break;
            case 1:
                comfirmYy = "储值票车";
                break;
            case 2:
                comfirmYy = "时租车";
                break;
            case 3:
                comfirmYy = "有效期未开始";
                break;
            case 4:
                comfirmYy = "已过期";
                break;
            case 5:
                comfirmYy = "余额不足";
                break;
            case 6:
                comfirmYy = "车位池满";
                break;
            case 7:
                comfirmYy = "车库未授权";
                break;
            case 8:
                comfirmYy = "子库未授权";
                break;
            case 9:
                comfirmYy = "报停车辆";
                break;
            case 10:
                comfirmYy = "已禁用车辆";
                break;
        }
    }

    private void confirmjudge() {
        switch (preson) {
            case 0:
                comfirmYy = "通道确认通行";
                break;
            case 1:
                comfirmYy = "车位满确认放行";
                break;
            case 2:
                comfirmYy = "车位池满确认放行";
                break;
            case 3:
                comfirmYy = "有效期未开始";
                break;
            case 4:
                comfirmYy = "已过期";
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
                queryInputbox8.setVisibility(View.GONE);
                queryInputbox1.setText(String.valueOf(carnumber[0]));
                queryInputbox2.setText(String.valueOf(carnumber[1]));
                queryInputbox3.setText(String.valueOf(carnumber[2]));
                queryInputbox4.setText(String.valueOf(carnumber[3]));
                queryInputbox5.setText(String.valueOf(carnumber[4]));
                queryInputbox6.setText(String.valueOf(carnumber[5]));
                queryInputbox7.setText(String.valueOf(carnumber[6]));
            } else if (carnumber.length == 8) {
                queryInputbox1.setText(String.valueOf(carnumber[0]));
                queryInputbox2.setText(String.valueOf(carnumber[1]));
                queryInputbox3.setText(String.valueOf(carnumber[2]));
                queryInputbox4.setText(String.valueOf(carnumber[3]));
                queryInputbox5.setText(String.valueOf(carnumber[4]));
                queryInputbox6.setText(String.valueOf(carnumber[5]));
                queryInputbox7.setText(String.valueOf(carnumber[6]));
                queryInputbox8.setText(String.valueOf(carnumber[7]));
            }

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            SPUtils.put(QueryCarnum.this, "open_new_car", true);
            queryInputbox8.setVisibility(View.VISIBLE);
            LicenseKeyboardUtil_input.etsize = 7;
            if (!TextUtils.isEmpty(queryInputbox7.getText())) {
                currentEditText_input = 7;
                initcolor1();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edits[currentEditText_input].setBackground(getDrawable(R.drawable.keyboard_bg_red));
                }
            }

        } else {
            SPUtils.put(QueryCarnum.this, "open_new_car", false);
            queryInputbox8.setVisibility(View.GONE);
            LicenseKeyboardUtil_input.etsize = 6;
            if (!TextUtils.isEmpty(queryInputbox6.getText())) {
                currentEditText_input = 6;
                initcolor1();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    edits[currentEditText_input].setBackground(getDrawable(R.drawable.keyboard_bg_red));
                }
            }

        }
    }

    @Override
    public void onRefresh() {
        querynumAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //list.clear();
                //getPreVeh(App.serverurl);
                //vehicleAdapter.setNewData(list);
                /*querynumAdapter.notifyDataSetChanged();
                isErr = false;
                mCurrentCounter = PAGE_SIZE;*/
                if (App.mdRefresh){
                    Log.e("TAG","修改车牌刷新***item+"+mdposition+"修改后的车牌"+ModifyCarnum.mdcarnum);
                    App.mdRefresh=false;
                    querynumAdapter.getData().get(mdposition).setPnum(ModifyCarnum.mdcarnum);
                    querynumAdapter.notifyDataSetChanged();
                    App.zcRefresh=false;
                }else {
                    Log.e("TAG","出场自动刷新***item+"+coutposition);
                    App.zcRefresh=false;
                    list.remove(coutposition);
                    querynumAdapter.notifyItemRemoved(coutposition);
                    //vehicleAdapter.notifyDataSetChanged();
                }
                if (querySwipeLayout != null) {
                    querySwipeLayout.setRefreshing(false);
                }
                querynumAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {
        querySwipeLayout.setEnabled(false);
        if (querynumAdapter.getData().size() < PAGE_SIZE) {
            querynumAdapter.loadMoreEnd(true);//加载完成不再加载
        } else {
            if (mCurrentCounter >= TOTAL_COUNTER) {
                querynumAdapter.loadMoreEnd();//default visible
                //数据全部加载完毕
                querynumAdapter.loadMoreEnd(mLoadMoreEndGone);//true is gone,false is visible
            } else {
                if (isErr) {
                    querynumAdapter.addData(list);
                    mCurrentCounter = querynumAdapter.getData().size();
                    querynumAdapter.loadMoreComplete();
                } else {
                    isErr = true;
                    querynumAdapter.loadMoreFail();
                }
            }
            querySwipeLayout.setEnabled(true);
        }
    }
}
