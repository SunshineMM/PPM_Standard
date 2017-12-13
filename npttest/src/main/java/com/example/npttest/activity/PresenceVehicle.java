package com.example.npttest.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.adapter.PresenceVehicleAdapter;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.MyJson;
import com.example.npttest.entity.Prese;
import com.example.npttest.view.CustomLoadMoreView;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
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
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by liuji on 2017/9/1.
 */

public class PresenceVehicle extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.prece_return)
    ImageView preceReturn;
    @Bind(R.id.prece_search)
    ImageView preceSearch;
    @Bind(R.id.prece_rv_list)
    RecyclerView preceRvList;
    @Bind(R.id.prece_swipeLayout)
    SwipeRefreshLayout preceSwipeLayout;
    private PresenceVehicleAdapter vehicleAdapter;
    private static final int TOTAL_COUNTER = 100;
    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr=false;
    private boolean mLoadMoreEndGone = false;
    private ZLoadingDialog dialog, dialog1;
    private int rstat, ptype, ctype, cdtp, preson;
    private String jfType;//计费类型
    private String comfirmYy;//确认原因
    private String carnum;
    private int cartype;
    SynthesizerListener mSynListener;
    private String sid;
    private Gson gson=new Gson();
    private MyJson myJson=new MyJson();
    private List<Prese> AddPreseList;
    private boolean LoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presence_vehicle);
        ButterKnife.bind(this);
        SpeechUtility.createUtility(PresenceVehicle.this, SpeechConstant.APPID + "=59df2c0c");
        initAdapter();
        preceSwipeLayout.setOnRefreshListener(this);
        preceSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        preceRvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //preceRvList.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        /* if (App.serverurl != null) {
            getPreVeh(App.serverurl);
            *//*if (vehicleAdapter != null && preceRvList != null) {
                vehicleAdapter.notifyDataSetChanged();
                preceRvList.setAdapter(vehicleAdapter);
            }*//*
        }*/
        if (!isFinishing()) {
            dialog = new ZLoadingDialog(PresenceVehicle.this);
            dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                    .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                    .setHintText("Loading...")
                    .setHintTextColor(Color.parseColor("#55BEB7"))
                    .setHintTextSize(16) // 设置字体大小 dp
                    .setHintTextColor(Color.GRAY) // 设置字体颜色
                    //.setCanceledOnTouchOutside(false)
                    .show();
        }
        getData();
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
        char[] carnumber = carnum.toCharArray();
        if (carnumber.length == 7) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + "出场成功！", mSynListener);
        } else if (carnumber.length == 8) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + String.valueOf(carnumber[7]) + " " + "出场成功！", mSynListener);
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


    @Override
    public void onStart() {
        super.onStart();
        if (!App.pvRefresh) {
            Log.e("TAG", "onstart 刷新");
            onRefresh();
            App.pvRefresh = true;
        }

    }

    @OnClick({R.id.prece_return, R.id.prece_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.prece_return:
                finish();
                break;
            case R.id.prece_search:
                startActivity(new Intent(PresenceVehicle.this, QueryCarnum.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            //dialog.dismiss();
            dialog.cancel();
        }
    }


    private void getPreVeh() {
        //final String jsonS = "{\"cmd\":\"154\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\",\"dsv\":\"" + Constant.DSV + "\",\"spare\":\"\",\"sign\":\"abcd\"}";
        final String jsonS = "{\"cmd\":\"154\",\"type\":\""+ Constant.TYPE+"\",\"code\":\""+ Constant.CODE+"\",\"dsv\":\""+ Constant.DSV+"\",\"index\":\"0\",\"psize\":\"20\",\"spare\":\"\",\"sign\":\"abcd\"}";
        Log.e("TAG","刷新："+jsonS);
        OkHttpUtils.postString().url(App.serverurl)
                .content(jsonS)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(PresenceVehicle.this, "请检查网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    if (AddPreseList!=null){
                        AddPreseList.clear();
                    }
                    AddPreseList = new ArrayList<>();
                    JSONObject rjsonObject = new JSONObject(response);
                    JSONObject resultjsOb = rjsonObject.getJSONObject("result");
                    JSONObject datajsob=resultjsOb.getJSONObject("data");
                    JSONArray listjsAr = datajsob.getJSONArray("inners");
                    // for (int i = listjsAr.length()-1; i >=0 ; i--) {  --倒叙
                    for (int i = 0; i < listjsAr.length(); i++) {
                        JSONObject jsonObject = listjsAr.getJSONObject(i);
                        Prese prese = new Prese();
                        prese.setPnum(jsonObject.getString("pnum"));
                        prese.setCtype(jsonObject.getInt("ctype"));
                        prese.setCdtp(jsonObject.getInt("cdtp"));
                        prese.setItime(jsonObject.getLong("itime"));
                        prese.setIurl(jsonObject.getString("iurl"));
                        prese.setSid(jsonObject.getString("sid"));
                        AddPreseList.add(prese);
                    }
                    Log.e("TAG", "在场车辆：" + AddPreseList.size());
                    handler.sendEmptyMessage(0x123);
                } catch (JSONException e) {
                    e.printStackTrace();
                    vehicleAdapter.loadMoreFail();
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                vehicleAdapter.notifyDataSetChanged();
                preceRvList.setAdapter(vehicleAdapter);
            }
        }
    };
    private void getData(){
        OkHttpUtils
                .postString()
                .url(App.serverurl)
                .content(gson.toJson(myJson))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        vehicleAdapter.loadMoreEnd(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (dialog != null) {
                            dialog.cancel();
                        }
                        Log.e("TAG",gson.toJson(myJson));
                        //解析json
                        try {
                            JSONObject JSONObject = new JSONObject(response);
                            AddPreseList = new ArrayList<>();
                            JSONArray JSONArray = JSONObject.getJSONObject("result").getJSONObject("data").getJSONArray("inners");
                            //获取的数据放在集合里
                            for (int i=0;i<JSONArray.length();i++) {
                                AddPreseList.add(gson.fromJson(JSONArray.get(i).toString(), Prese.class));
                            }
                            Log.e("TAG","加载后数据长度"+AddPreseList.size());
                            if (AddPreseList.size() > 0) {
                                //添加数据
                                LoadMore=true;
                                vehicleAdapter.addData(AddPreseList);
                                vehicleAdapter.loadMoreComplete();
                            } else{
                                LoadMore=false;
                                vehicleAdapter.loadMoreEnd(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            vehicleAdapter.loadMoreEnd(false);
                            LoadMore=false;
                        }
                    }
                });
    }


    private void initAdapter() {
        vehicleAdapter = new PresenceVehicleAdapter(AddPreseList);
        vehicleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (LoadMore){
                    myJson.setIndex(myJson.getIndex()+1);
                }
                getData();
            }
        }, preceRvList);
        //vehicleAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        vehicleAdapter.setLoadMoreView(new CustomLoadMoreView());
        preceRvList.setAdapter(vehicleAdapter);
        //vehicleAdapter.disableLoadMoreIfNotFullPage();
        //vehicleAdapter.isFirstOnly(false);  动画重复执行
        //mCurrentCounter = vehicleAdapter.getData().size();
        //preceRvList.setAdapter(vehicleAdapter);
        preceRvList.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(PresenceVehicle.this, Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent carinfointent = new Intent(PresenceVehicle.this, PresenceVehicleInfo.class);
                carinfointent.putExtra("carnum", vehicleAdapter.getData().get(position).getPnum());
                carinfointent.putExtra("cartype", vehicleAdapter.getData().get(position).getCtype());
                carinfointent.putExtra("pztype", vehicleAdapter.getData().get(position).getCdtp());
                carinfointent.putExtra("itime", vehicleAdapter.getData().get(position).getItime());
                carinfointent.putExtra("sid", vehicleAdapter.getData().get(position).getSid());
                startActivity(carinfointent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(PresenceVehicle.this, Integer.toString(position) + "long", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.prece_modify:
                        //Toast.makeText(PresenceVehicle.this, "修改", Toast.LENGTH_SHORT).show();
                        Intent modiftintent = new Intent(PresenceVehicle.this, ModifyCarnum.class);
                        modiftintent.putExtra("number", vehicleAdapter.getData().get(position).getPnum());
                        modiftintent.putExtra("cartype", vehicleAdapter.getData().get(position).getCtype());
                        modiftintent.putExtra("pztype", vehicleAdapter.getData().get(position).getCdtp());
                        modiftintent.putExtra("pvrefresh", true);
                        modiftintent.putExtra("sid", vehicleAdapter.getData().get(position).getSid());
                        startActivity(modiftintent);
                        break;
                    case R.id.prece_outcar:
                        carnum = vehicleAdapter.getData().get(position).getPnum();
                        cartype = vehicleAdapter.getData().get(position).getCtype();
                        sid=vehicleAdapter.getData().get(position).getSid();
                        carout(App.serverurl);
                        break;
                    case R.id.prece_img:
                        final AlertDialog dialog = new AlertDialog.Builder(PresenceVehicle.this).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(PresenceVehicle.this);
                        Glide.with(PresenceVehicle.this).load(vehicleAdapter.getData().get(position).getIurl())
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
    }




    //车辆出场
    private void carout(String url) {
        Log.e("TAG", "多少次");
        dialog1 = new ZLoadingDialog(PresenceVehicle.this);
        dialog1.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();
        //{"cmd":"140","type":"2","code":"17083B3DE","dsv":"110","ptype":"0","io":"0",
        // "num":"京B1FL39","ctype":"2","spare":"0","sign":"abcd"}
        /*String intocar_jS = "{\"cmd\":\"140\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\"," +
                "\"dsv\":\"" + Constant.DSV + "\",\"ptype\":\"0\",\"io\":\"1\",\"num\":\"" + carnum + "\"," +
                "\"ctype\":\"" + cartype + "\",\"spare\":\"0\",\"sign\":\"abcd\"}";*/

        String intocar_jS ="{\"cmd\":\"140\",\"type\":\""+ Constant.TYPE+"\",\"code\":\""+ Constant.CODE+"\",\"dsv\":\""
                + Constant.DSV+"\",\"ptype\":\"6\",\"io\":\"1\",\"num\":\""+sid+"\",\"ctype\":\""+cartype+
                "\",\"muna\":\"1\",\"spare\":\"0\",\"sign\":\"abcd\"}";

        Log.e("TAG", intocar_jS);
        OkHttpUtils.postString().url(url)
                .content(intocar_jS)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("TAG", "无网络");
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
                    String carnum1 = datajson.getString("num");//车牌号
                    rstat = datajson.getInt("rstat");//放行状态
                    //rstat=2;
                    ptype = datajson.getInt("ptype");//凭证类型（卡或车牌）
                    ctype = datajson.getInt("ctype");//车辆类型
                    cdtp = datajson.getInt("cdtp");//计费类型（月票车）
                    long ctime = datajson.getLong("ctime");//计费时间
                    preson = datajson.getInt("preson");//原因
                    long itime = datajson.getLong("itime");//入场时间
                    double nmon = datajson.getDouble("nmon");//应收金额
                    double rmon = datajson.getDouble("rmon");//实收金额
                    double smon = datajson.getDouble("smon");//折扣金额
                    String sid = datajson.getString("sid");
                    //判断
                    jfjudge();
                    if (rstat == 0) {
                        carout_start_voice();
                        //自动放行
                        Intent zdintent = new Intent(PresenceVehicle.this, CaroutSuccessful.class);
                        zdintent.putExtra("carnum", carnum);
                        zdintent.putExtra("jfType", jfType);
                        zdintent.putExtra("ctype", ctype);
                        zdintent.putExtra("ctime", ctime);
                        zdintent.putExtra("itime", itime);
                        zdintent.putExtra("pvrefresh", true);
                        startActivity(zdintent);
                        //Toast.makeText(InputCarnum.this, "自动放行", Toast.LENGTH_SHORT).show();
                    } else if (rstat == 1) {
                        //确认放行
                        confirmjudge();
                        Intent qrintent = new Intent(PresenceVehicle.this, CaroutConfirmPass.class);
                        qrintent.putExtra("carnum", carnum);
                        qrintent.putExtra("jfType", jfType);
                        qrintent.putExtra("ctype", ctype);
                        qrintent.putExtra("itime", itime);
                        qrintent.putExtra("ctime", ctime);
                        qrintent.putExtra("comfirmYy", comfirmYy);
                        qrintent.putExtra("sid", sid);
                        qrintent.putExtra("cdtp", cdtp);
                        qrintent.putExtra("pvrefresh", true);
                        startActivity(qrintent);

                    } else if (rstat == 2) {
                        //收费放行
                        chargejudge();
                        Intent sfintent = new Intent(PresenceVehicle.this, CaroutChargeActivity.class);
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
                        sfintent.putExtra("pvrefresh", true);
                        startActivity(sfintent);
                    } else if (rstat == 3) {
                        //禁止通行
                        prohibitjudge();
                        Intent jzintent = new Intent(PresenceVehicle.this, ProhibitPass.class);
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

    private void jfjudge() {
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

    //刷新
    @Override
    public void onRefresh() {
        Log.e("TAG","刷新");
        vehicleAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //index=0;
               // getPreVeh(App.serverurl);
                //myJson.setIndex(0);
                getPreVeh();
                //vehicleAdapter.setNewData(list);
                //vehicleAdapter.notifyDataSetChanged();
                //isErr = false;
                mCurrentCounter = PAGE_SIZE;
                if (preceSwipeLayout != null) {
                    preceSwipeLayout.setRefreshing(false);
                }
                vehicleAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    //上拉加载
   /* @Override
    public void onLoadMoreRequested() {
        Log.e("TAG","我在加载");
        Log.e("TAG","mCurrentCounter: "+mCurrentCounter);
        Log.e("TAG","PAGE_SIZE: "+PAGE_SIZE);
        Log.e("TAG","TOTAL_COUNTER: "+TOTAL_COUNTER);
        Log.e("TAG","vehicleAdapter.getData().size(): "+vehicleAdapter.getData().size());
        preceSwipeLayout.setEnabled(false);
        index=index+1;
        getPreVeh(App.serverurl);
       *//* if (vehicleAdapter.getData().size()<PAGE_SIZE){
            vehicleAdapter.loadMoreEnd(true);//加载完成不再加载
        }else {
            if (vehicleAdapter.getData().size()>=100){
                vehicleAdapter.loadMoreEnd(false);
            }else {
                if (isErr){
                    isErr=false;
                    index++;
                    Log.e("TAG",index+"");
                    //vehicleAdapter.addData(list);
                    getPreVehload(App.serverurl);
                    //vehicleAdapter.setNewData(list);
                    //vehicleAdapter.notifyDataSetChanged();
                    //vehicleAdapter.setNewData(list);
                    //mCurrentCounter = vehicleAdapter.getData().size();
                    vehicleAdapter.loadMoreComplete();
                }else {
                    isErr = true;
                    vehicleAdapter.loadMoreFail();
                }
                //vehicleAdapter.loadMoreComplete();
                //vehicleAdapter.loadMoreFail();
                //getPreVehload(App.serverurl);

            }
        }*//*
        preceSwipeLayout.setEnabled(true);
    }*/

}
