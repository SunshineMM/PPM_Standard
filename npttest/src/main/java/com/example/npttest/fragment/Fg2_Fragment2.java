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
import com.example.npttest.activity.CaroutDetailedInfo;
import com.example.npttest.adapter.CaroutAdapter;
import com.example.npttest.constant.Constant;
import com.example.npttest.entity.Carout;
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

public class Fg2_Fragment2 extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.rv_list_fg2)
    RecyclerView rvListFg2;
    @Bind(R.id.swipeLayout_fg2)
    SwipeRefreshLayout swipeLayoutFg2;
    @Bind(R.id.ffg2_nulltv)
    TextView ffg2Nulltv;
    private List<Carout> list = new ArrayList<>();
    private CaroutAdapter caroutAdapter;
    private static final int TOTAL_COUNTER = 0;

    private static final int PAGE_SIZE = 1;

    private int delayMillis = 1000;

    private int mCurrentCounter = 0;

    private boolean isErr;
    private boolean mLoadMoreEndGone = false;
//    private ZLoadingDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fg2_fragment2, null);
        ButterKnife.bind(this, v);
        list.clear();
        if (App.serverurl != null) {
            getrecord(App.serverurl);
            initAdapter();
        }
        swipeLayoutFg2.setOnRefreshListener(this);
        swipeLayoutFg2.setColorSchemeResources(R.color.colorPrimaryDark);
        //initAdapter();
        //rvListFg2.setLayoutManager(new LinearLayoutManager(getActivity()));   //*****这句注释就不会了
        rvListFg2.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG", "httpupdate出场");
        if (!App.outRefresh) {
            onRefresh();
            App.outRefresh = true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        /*list.clear();
        getrecord(App.serverurl);*/

    }

    //获取出场车辆记录
    private void getrecord(String url) {
       /* dialog = new ZLoadingDialog(getActivity());
        dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();*/
        String getrcord = "{\"cmd\":\"156\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE +
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
                        Carout carout = new Carout();
                        carout.setCnum(jsonObject.getString("cnum"));
                        carout.setPnum(jsonObject.getString("pnum"));
                        carout.setEtime(jsonObject.getInt("etime"));
                        carout.setEurl(jsonObject.getString("eurl"));
                        carout.setSid(jsonObject.getString("sid"));
                        carout.setCtype(jsonObject.getString("ctype"));
                        carout.setCdtp(jsonObject.getString("cdtp"));
                        list.add(carout);
                    }
                    Log.e("TAG", "出场车辆:" + list.size());
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
                caroutAdapter.notifyDataSetChanged();
                rvListFg2.setAdapter(caroutAdapter);
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
        caroutAdapter = new CaroutAdapter(list);
        caroutAdapter.setOnLoadMoreListener(this, rvListFg2);
        caroutAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        caroutAdapter.isFirstOnly(false);
        caroutAdapter.setLoadMoreView(new CustomLoadMoreView1());
        rvListFg2.setAdapter(caroutAdapter);
        mCurrentCounter = caroutAdapter.getData().size();

       /* rvListFg1.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();
            }
        });*/

        rvListFg2.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                Intent caroutInfo_intent = new Intent(getActivity(), CaroutDetailedInfo.class);
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
                        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.show();
                        ImageView imageView = new ImageView(getActivity());
                        Glide.with(getActivity()).load(caroutAdapter.getData().get(position).getEurl())
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
        swipeLayoutFg2.setEnabled(false);
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
            swipeLayoutFg2.setEnabled(true);
        }
    }

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
                //caroutAdapter.setNewData(list);
                caroutAdapter.notifyDataSetChanged();
                isErr = false;
                mCurrentCounter = PAGE_SIZE;
                swipeLayoutFg2.setRefreshing(false);
                caroutAdapter.setEnableLoadMore(true);
            }
        }, delayMillis);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
