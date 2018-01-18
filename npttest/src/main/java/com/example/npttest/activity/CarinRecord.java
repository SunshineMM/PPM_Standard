package com.example.npttest.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.adapter.CarintoAdapter;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Carinto;
import com.example.npttest.manager.LinearLayoutManagerWrapper;
import com.example.npttest.util.TimeUtils;
import com.example.npttest.view.CustomLoadMoreView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by liuji on 2017/10/20.
 */

public class CarinRecord extends NoStatusbarActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.record_carin_return)
    ImageView recordCarinReturn;
    @Bind(R.id.record_carin_search)
    ImageView recordCarinSearch;
    @Bind(R.id.record_carin_tv_time1)
    TextView recordCarinTvTime1;
    @Bind(R.id.record_carin_time1)
    LinearLayout recordCarinTime1;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.record_carin_tv_time2)
    TextView recordCarinTvTime2;
    @Bind(R.id.record_carin_time2)
    LinearLayout recordCarinTime2;
    @Bind(R.id.record_carin_rv)
    RecyclerView recordCarinRv;
    @Bind(R.id.recard_carin_swipeLayout)
    SwipeRefreshLayout recardCarinSwipeLayout;
    private TimePickerView pvCustomTime, pvCustomTime2;
    private List<Carinto> list = new ArrayList<>();
    private CarintoAdapter carintoAdapter;
    private static final int TOTAL_COUNTER = 0;
    private static final int PAGE_SIZE = 6;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean isErr;
    private boolean mLoadMoreEndGone = false;
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carin_record);
        ButterKnife.bind(this);
        recordCarinTvTime1.setText(TimeUtils.getStrTime(String.valueOf(gettime() - 86400)));
        recordCarinTvTime2.setText(TimeUtils.getStrTime(String.valueOf(gettime())));
        initCustomTimePicker();
        list.clear();
        if (App.serverurl != null) {
            getrecord(App.serverurl);
            initAdapter();
        }
        recardCarinSwipeLayout.setOnRefreshListener(this);
        recardCarinSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        //initAdapter();
        //rvListFg1.setLayoutManager(new LinearLayoutManager(getActivity()));
        recordCarinRv.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
    }

    //获取当前时间
    private long gettime() {
        Date date = new Date();
        long time = (date.getTime() / 1000);
        //Log.e("TAG",date.getTime()/1000+"");
        return time;
    }

    //设置适配器
    private void initAdapter() {
        carintoAdapter = new CarintoAdapter(list);
        carintoAdapter.setOnLoadMoreListener(this, recordCarinRv);
        carintoAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        carintoAdapter.setLoadMoreView(new CustomLoadMoreView());
        carintoAdapter.isFirstOnly(false);
        recordCarinRv.setAdapter(carintoAdapter);
        mCurrentCounter = carintoAdapter.getData().size();

       /* rvListFg1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        } );*/

        /**
         * 注册监听事件
         */
        recordCarinRv.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent carintoInfo_intent = new Intent(CarinRecord.this, CarinDetailedInfo.class);
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
                        final AlertDialog dialog = new AlertDialog.Builder(CarinRecord.this).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(CarinRecord.this);
                        Glide.with(CarinRecord.this).load(carintoAdapter.getData().get(position).getIurl())
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
        String stime = String.valueOf(TimeUtils.getTimeStamp(String.valueOf(recordCarinTvTime1.getText()), "yyyy-MM-dd HH:mm:ss"));
        String etime = String.valueOf(TimeUtils.getTimeStamp(String.valueOf(recordCarinTvTime2.getText()), "yyyy-MM-dd HH:mm:ss"));

        String getrcord = "{\"cmd\":\"155\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE +
                "\",\"dsv\":\"" + Constant.DSV + "\",\"qtype\":\"0\",\"num\":\"\",\"stime\":\"" + stime + "\"," +
                "\"etime\":\"" + etime + "\",\"spare\":\" \",\"sign\":\"abcd\"}";
        Log.e("TAG", getrcord);
        OkHttpUtils.postString().url(url)
                .content(getrcord)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(CarinRecord.this, "无网络", Toast.LENGTH_SHORT).show();
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
                            handler.sendEmptyMessage(0x123);
                        }
                    } else {
                        Toasty.error(CarinRecord.this, "未查询到该记录", Toast.LENGTH_SHORT, true).show();
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
                if (carintoAdapter != null && recordCarinRv != null) {
                    carintoAdapter.notifyDataSetChanged();
                    recordCarinRv.setAdapter(carintoAdapter);
                }
            }
        }
    };


    @OnClick({R.id.record_carin_return, R.id.record_carin_time1, R.id.record_carin_time2, R.id.record_carin_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.record_carin_return:
                finish();
                break;
            case R.id.record_carin_time1:
                pvCustomTime.show();
                break;
            case R.id.record_carin_time2:
                pvCustomTime2.show();
                break;
            case R.id.record_carin_search:
                startActivity(new Intent(CarinRecord.this, QueryCarinRecord.class));
                break;
        }
    }

    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                recordCarinTvTime1.setText(getTime(date));
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                list.clear();
                                if (App.serverurl != null) {
                                    getrecord(App.serverurl);
                                }
                                //rvListFg1.setLayoutManager(new LinearLayoutManager(getActivity()));
                                //carintoAdapter.setNewData(list);
                                carintoAdapter.notifyDataSetChanged();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

        pvCustomTime2 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                recordCarinTvTime2.setText(getTime(date));
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
               /*.gravity(Gravity.RIGHT)// default is center*/
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit1 = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel1 = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime2.returnData();
                                list.clear();
                                if (App.serverurl != null) {
                                    getrecord(App.serverurl);
                                }
                                //rvListFg1.setLayoutManager(new LinearLayoutManager(getActivity()));
                                //carintoAdapter.setNewData(list);
                                carintoAdapter.notifyDataSetChanged();
                                pvCustomTime2.dismiss();
                            }
                        });
                        ivCancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime2.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

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
                if (recardCarinSwipeLayout != null) {
                    recardCarinSwipeLayout.setRefreshing(false);
                }
                carintoAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {
        recardCarinSwipeLayout.setEnabled(false);
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
            recardCarinSwipeLayout.setEnabled(true);
        }
    }
}
