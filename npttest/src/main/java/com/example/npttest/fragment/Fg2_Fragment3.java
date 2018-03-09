package com.example.npttest.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.activity.ChargeDetailedInfo;
import com.example.npttest.adapter.ChargeAdapter;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Charge;
import com.example.npttest.manager.LinearLayoutManagerWrapper;
import com.example.npttest.view.CustomLoadMoreView1;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by liuji on 2017/7/28.
 */

public class Fg2_Fragment3 extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.rv_list_fg3)
    RecyclerView rvListFg3;
    @Bind(R.id.swipeLayout_fg3)
    SwipeRefreshLayout swipeLayoutFg3;
    @Bind(R.id.ffg3_nulltv)
    TextView ffg3Nulltv;
    private List<Charge> list = new ArrayList<>();
    private ChargeAdapter chargeAdapter;
    private static final int TOTAL_COUNTER = 0;

    private static final int PAGE_SIZE = 1;

    private int delayMillis = 1000;

    private int mCurrentCounter = 0;

    private boolean isErr;
    private boolean mLoadMoreEndGone = false;
//    private ZLoadingDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fg2_fragment3, null);
        ButterKnife.bind(this, v);
        list.clear();
        if (App.serverurl != null) {
            getrecord(App.serverurl);
            initAdapter();
        }
        swipeLayoutFg3.setOnRefreshListener(this);
        swipeLayoutFg3.setColorSchemeResources(R.color.colorPrimaryDark);
        //initAdapter();
        //rvListFg2.setLayoutManager(new LinearLayoutManager(getActivity()));   //*****这句注释就不会了
        rvListFg3.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG", "httpupdate收费");
        if (!App.chaRefresh) {
            onRefresh();
            App.chaRefresh = true;
        }

    }

    //获取收费纪录
    private void getrecord(String url) {
        /*dialog = new ZLoadingDialog(getActivity());
        dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();*/
        String getrcord = "{\"cmd\":\"157\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE +
                "\",\"dsv\":\"" + Constant.DSV + "\",\"qtype\":\"0\",\"num\":\"\",\"stime\":\"\"," +
                "\"etime\":\"\",\"spare\":\" \",\"sign\":\"abcd\"}";
        Log.e("TAG", getrcord);
        OkHttpUtils.postString().url(url)
                .content(getrcord)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "无网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
//                dialog.dismiss();
                try {
                    JSONObject rjsonObject = new JSONObject(response);
                    JSONObject resultjsOb = rjsonObject.getJSONObject("result");
                    JSONArray listjsAr = resultjsOb.getJSONArray("list");
                    for (int i = 0; i < listjsAr.length(); i++) {
                        JSONObject jsonObject = listjsAr.getJSONObject(i);
                        Charge charge = new Charge();
                        charge.setCnum(jsonObject.getString("cnum"));
                        charge.setPnum(jsonObject.getString("pnum"));
                        charge.setItime(jsonObject.getLong("itime"));
                        charge.setCtime(jsonObject.getLong("ctime"));
                        charge.setNmon(jsonObject.getDouble("nmon"));
                        charge.setRmon(jsonObject.getDouble("rmon"));
                        charge.setPmon(jsonObject.getDouble("pmon"));
                        charge.setSmon(jsonObject.getDouble("smon"));
                        charge.setDmon(jsonObject.getDouble("dmon"));
                        charge.setPreson(jsonObject.getInt("preson"));
                        charge.setPtype(jsonObject.getString("ptype"));
                        charge.setCdtp(jsonObject.getString("cdtp"));
                        charge.setCtype(jsonObject.getString("ctype"));
                        charge.setIurl(jsonObject.getString("iurl"));
                        charge.setOurl(jsonObject.getString("ourl"));
                        list.add(charge);
                    }
                    Log.e("TAG", "收费纪录:" + list.size());
                    handler.sendEmptyMessage(0x123);

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
                if (chargeAdapter != null && rvListFg3 != null) {
                    chargeAdapter.notifyDataSetChanged();
                    rvListFg3.setAdapter(chargeAdapter);
                }
            }
        }
    };

    // 默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）

   /* public static final int ALPHAIN = 0x00000001;

    public static final int SCALEIN = 0x00000002;

    public static final int SLIDEIN_BOTTOM = 0x00000003;

    public static final int SLIDEIN_LEFT = 0x00000004;

    public static final int SLIDEIN_RIGHT = 0x00000005;*/

    private void initAdapter() {
        chargeAdapter = new ChargeAdapter(list);
        chargeAdapter.setOnLoadMoreListener(this, rvListFg3);
        chargeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        chargeAdapter.isFirstOnly(false);
        chargeAdapter.setLoadMoreView(new CustomLoadMoreView1());
        rvListFg3.setAdapter(chargeAdapter);
        mCurrentCounter = chargeAdapter.getData().size();

       /* rvListFg1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });*/

        rvListFg3.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent chargeinfo_intent = new Intent(getActivity(), ChargeDetailedInfo.class);
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
                //Toast.makeText(getActivity(), Integer.toString(position) + "long", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_img:
                        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(getActivity());
                        Glide.with(getActivity()).load(chargeAdapter.getData().get(position).getOurl())
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

    @Override
    public void onLoadMoreRequested() {
        swipeLayoutFg3.setEnabled(false);
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
            swipeLayoutFg3.setEnabled(true);
        }
    }

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
                //caroutAdapter.setNewData(list);
                chargeAdapter.notifyDataSetChanged();
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                if (swipeLayoutFg3 != null) {
                    swipeLayoutFg3.setRefreshing(false);
                }
                chargeAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
