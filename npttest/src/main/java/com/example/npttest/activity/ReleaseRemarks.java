package com.example.npttest.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.adapter.ReleaseRemarksAdapter;
import com.example.npttest.constant.Constant;
import com.example.npttest.tool.DateTools;
import com.example.npttest.util.TimeUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.ThermalPrinter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.MediaType;

import static com.example.npttest.activity.CaroutChargeActivity.caroutactivity;
import static com.example.npttest.constant.Constant.TABLE_USER;

/**
 * Created by liuji on 2017/9/12.
 */

public class ReleaseRemarks extends NoStatusbarActivity implements OnItemDragListener, OnItemSwipeListener {


    @Bind(R.id.release_return)
    ImageView releaseReturn;
    @Bind(R.id.release_add)
    ImageView releaseAdd;
    @Bind(R.id.release_RecyclerView)
    RecyclerView releaseRecyclerView;
    @Bind(R.id.release_sweipeLayout)
    SwipeRefreshLayout releaseSweipeLayout;
    private ReleaseRemarksAdapter remarksAdapter;
    private Paint paint;
    private List<String> list = new ArrayList<>();
    private Boolean aBoolean, pvrefresh;
    private String carnum, cartype, jfType, comfirmYy, sid, pktime,snmon,ssmon,srmon;
    private long itime, ctime;
    private int ctype, cdtp;
    private ZLoadingDialog dialog1, dialog;
    SynthesizerListener mSynListener;

    private final int PRINTVERSION = 5;
    ProgressDialog pgdialog;
    private ProgressDialog progressDialog;
    MyHandler handler;
    private static String printVersion;
    private String Result;
    private Boolean nopaper = false;
    private final int OVERHEAT = 12;
    private final int PRINTERR = 11;
    private final int CANCELPROMPT = 10;
    private final int NOPAPER = 3;
    private Boolean printok=false;
    private String print;


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOPAPER:
                    noPaperDlg();
                    break;
                case OVERHEAT:
                    android.app.AlertDialog.Builder overHeatDialog = new android.app.AlertDialog.Builder(ReleaseRemarks.this);
                    overHeatDialog.setTitle("温馨提示");
                    overHeatDialog.setMessage("打印过热");
                    overHeatDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            free_start_voice();
                            Intent zdintent = new Intent(ReleaseRemarks.this, CaroutSuccessful.class);
                            zdintent.putExtra("carnum", carnum);
                            zdintent.putExtra("jfType", jfType);
                            zdintent.putExtra("ctype", ctype);
                            zdintent.putExtra("ctime", ctime);
                            zdintent.putExtra("itime", itime);
                            zdintent.putExtra("pvrefresh", pvrefresh);
                            startActivity(zdintent);
                            finish();
                        }
                    });
                    overHeatDialog.show();
                    break;
                case CANCELPROMPT:
                    if (progressDialog != null && !ReleaseRemarks.this.isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    break;
                case 0x0123:
                    free_start_voice();
                    Intent zdintent = new Intent(ReleaseRemarks.this, CaroutSuccessful.class);
                    zdintent.putExtra("carnum", carnum);
                    zdintent.putExtra("jfType", jfType);
                    zdintent.putExtra("ctype", ctype);
                    zdintent.putExtra("ctime", ctime);
                    zdintent.putExtra("itime", itime);
                    zdintent.putExtra("pvrefresh", pvrefresh);
                    startActivity(zdintent);
                    finish();
                    break;
                case PRINTVERSION:
                    if (msg.obj.equals("1")) {
                        Log.e("TAG",printVersion);
                    } else{
                        //Toast.makeText(InputCarnum.this, "", Toast.LENGTH_LONG).show();
                        Log.e("TAG","未找到打印机");
                    }
                    break;
                default:
                    Toast.makeText(ReleaseRemarks.this, "打印机异常！", Toast.LENGTH_LONG).show();
                    free_start_voice();
                    Intent zintent = new Intent(ReleaseRemarks.this, CaroutSuccessful.class);
                    zintent.putExtra("carnum", carnum);
                    zintent.putExtra("jfType", jfType);
                    zintent.putExtra("ctype", ctype);
                    zintent.putExtra("ctime", ctime);
                    zintent.putExtra("itime", itime);
                    zintent.putExtra("pvrefresh", pvrefresh);
                    startActivity(zintent);
                    finish();
                    break;
            }
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_remarks);
        ButterKnife.bind(this);
        SpeechUtility.createUtility(ReleaseRemarks.this, SpeechConstant.APPID + "=59df2c0c");
        Intent intent = getIntent();
        aBoolean = intent.getBooleanExtra("relBoo", false);
        if (aBoolean) {
            carnum = intent.getStringExtra("carnum");
            ctype = intent.getIntExtra("ctype", 0);
            cdtp = intent.getIntExtra("cdtp", 0);
            jfType = intent.getStringExtra("jfType");
            itime = intent.getLongExtra("itime", 0);
            ctime = intent.getLongExtra("ctime", 0);
            pvrefresh = intent.getBooleanExtra("pvrefresh", false);
            sid = intent.getStringExtra("sid");
            pktime=intent.getStringExtra("pktime");
            snmon=intent.getStringExtra("snmon");
            ssmon=intent.getStringExtra("ssmon");
            srmon=intent.getStringExtra("srmon");
            Log.e("TAG", "*****" + pvrefresh);
        }
        //carnumber=intent.getStringExtra("carnum");
        remarksAdapter = new ReleaseRemarksAdapter(list);
        query_DB();
        releaseSweipeLayout.setEnabled(false);
        releaseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemDragAndSwipeCallback swipeCallback = new ItemDragAndSwipeCallback(remarksAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeCallback);
        touchHelper.attachToRecyclerView(releaseRecyclerView);
        //开启拖拽
        /*remarksAdapter.enableDragItem(touchHelper);
        remarksAdapter.setOnItemDragListener(this);*/

        //开启滑动删除
        remarksAdapter.enableSwipeItem();
        swipeCallback.setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        remarksAdapter.setOnItemSwipeListener(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(45);
        paint.setColor(Color.WHITE);
        Log.e("TAG", "onstart");
        releaseRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (aBoolean) {
                    //Toast.makeText(ReleaseRemarks.this, ""+ list.get(position), Toast.LENGTH_SHORT).show();
                    String cpjson = "{\"cmd\":\"141\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\"," +
                            "\"dsv\":\"" + Constant.DSV + "\",\"sid\":\"" + sid + "\",\"io\":\"1\",\"rstat\":\"3\"," +
                            "\"ftype\":\"\",\"sale\":\"0\",\"reas\":\"" + list.get(position) + "\",\"spare\":\"0\",\"sign\":\"abcd\"}";

                    chargePass(App.serverurl, cpjson);
                }
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.releaseItem_iv:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseRemarks.this);
                        builder.setTitle("用户信息修改");//设置标题
                        //实例化两个控件
                        final EditText updatetv = new EditText(ReleaseRemarks.this);
                        //将值添加到控件
                        updatetv.setText(list.get(position));
                        //将控件添加到线性布局
                        LinearLayout linearLayout = new LinearLayout(ReleaseRemarks.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);//设置线性布局的排列方式
                        linearLayout.addView(updatetv);
                        builder.setView(linearLayout);//将线性布局添加到build
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(updatetv.getText())) {
                                    Toasty.error(ReleaseRemarks.this, "输入信息不能为空", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    String sql_update = "update " + Constant.TABLE_USER + " set " + Constant.RRSTRING + "='"
                                            + updatetv.getText().toString() + "'" + " where " + Constant.RRSTRING + "=" + "'" + list.get(position) + "'";
                                    Log.e("TAG", sql_update);
                                    SQLiteDatabase db = App.dbHelper.getWritableDatabase();
                                    db.execSQL(sql_update);
                                    db.close();
                                    Toasty.success(ReleaseRemarks.this, "修改成功", Toast.LENGTH_SHORT, true).show();
                                    query_DB();
                                }
                            }
                        });
                        builder.show();
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, final int position) {

            }
        });

        handler=new MyHandler();
        //检测打印机
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ThermalPrinter.start(ReleaseRemarks.this);
                    ThermalPrinter.reset();
                    printVersion = ThermalPrinter.getVersion();
                } catch (TelpoException e) {
                    e.printStackTrace();
                } finally {
                    if (printVersion != null) {
                        Message message = new Message();
                        message.what = PRINTVERSION;
                        message.obj = "1";
                        handler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = PRINTVERSION;
                        message.obj = "0";
                        handler.sendMessage(message);
                    }
                    ThermalPrinter.stop(ReleaseRemarks.this);
                }
            }
        }).start();
    }

    //打印文本
    private class contentPrintThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(ReleaseRemarks.this);
                ThermalPrinter.reset();
                ThermalPrinter.setAlgin(ThermalPrinter.ALGIN_LEFT);
                ThermalPrinter.setLeftIndent(0);
                ThermalPrinter.setLineSpace(32);
                ThermalPrinter.setFontSize(2);
               /* if (wordFont == 4) {
                    ThermalPrinter.setFontSize(2);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (wordFont == 3) {
                    ThermalPrinter.setFontSize(1);
                    ThermalPrinter.enlargeFontSize(2, 2);
                } else if (wordFont == 2) {
                    ThermalPrinter.setFontSize(2);
                } else if (wordFont == 1) {
                    ThermalPrinter.setFontSize(1);
                }*/
                String str="\n          收费票据"
                        + "\n----------------------------"
                        + "\n车牌号："+carnum
                        + "\n订单号："+sid
                        + "\n日期："+ TimeUtils.getStrTime(String.valueOf(gettime()))
                        + "\n----------------------------"
                        + "\n凭证类型："+jfType
                        + "\n入场时间："+ DateTools.getDate(itime * 1000) + ""
                        + "\n出场时间："+DateTools.getDate(ctime * 1000) + ""
                        + "\n停车时间："+pktime
                        + "\n应收金额："+snmon+"元"
                        + "\n优惠金额："+ssmon+"元"
                        + "\n实收金额：0元（免费放行）"
                        + "\n值班员："+ Constant.username
                        + "\n地址："+ Constant.adds
                        + "\n----------------------------"
                        + "\n          欢迎您下次光临"
                        ;
                Bitmap bitmap = CreateCode(sid, BarcodeFormat.QR_CODE, 256, 256);
                if(bitmap != null){
                    ThermalPrinter.printLogo(bitmap);
                }
                ThermalPrinter.setGray(4);
                ThermalPrinter.addString(str);
                ThermalPrinter.printString();
                ThermalPrinter.walkPaper(100);
                printok=true;
            } catch (Exception e) {
                e.printStackTrace();
                Result = e.toString();
                printok=false;
                if (Result.equals("com.telpo.tps550.api.printer.NoPaperException")) {
                    nopaper = true;
                } else if (Result.equals("com.telpo.tps550.api.printer.OverHeatException")) {
                    handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0, null));
                } else {
                    handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0, null));
                }
            } finally {
                handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0, null));
                if (nopaper){
                    handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0, null));
                    nopaper = false;
                    return;
                }else {
                    handler.sendEmptyMessage(0x0123);
                }
                ThermalPrinter.stop(ReleaseRemarks.this);
            }
        }
    }

    public Bitmap CreateCode(String str, com.google.zxing.BarcodeFormat type, int bmpWidth, int bmpHeight) throws WriterException {
        Hashtable<EncodeHintType,String> mHashtable = new Hashtable<EncodeHintType,String>();
        mHashtable.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成二维矩阵,编码时要指定大小,不要生成了图片以后再进行缩放,以防模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, type, bmpWidth, bmpHeight, mHashtable);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为�?维像素数组（�?直横�?排）
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参�?�api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    //缺纸
    private void noPaperDlg() {
        android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(ReleaseRemarks.this);
        dlg.setTitle("打印缺纸");
        dlg.setMessage("打印缺纸，请放入纸后入场打印");
        dlg.setCancelable(false);
        dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThermalPrinter.stop(ReleaseRemarks.this);
                free_start_voice();
                Intent zdintent = new Intent(ReleaseRemarks.this, CaroutSuccessful.class);
                zdintent.putExtra("carnum", carnum);
                zdintent.putExtra("jfType", jfType);
                zdintent.putExtra("ctype", ctype);
                zdintent.putExtra("ctime", ctime);
                zdintent.putExtra("itime", itime);
                zdintent.putExtra("pvrefresh", pvrefresh);
                startActivity(zdintent);
                finish();
            }
        });
        dlg.show();
    }

    private void free_start_voice() {
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
        // mTts.startSpeaking(carnum+"免费出场成功！", mSynListener);
        char[] carnumber = carnum.toCharArray();
        if (carnumber.length == 7) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + "免费出场成功！", mSynListener);
        } else if (carnumber.length == 8) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + String.valueOf(carnumber[7]) + " " + "免费出场成功！", mSynListener);
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


    @OnClick({R.id.release_return, R.id.release_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.release_return:
                finish();
                //Snackbar.make(releaseReturn,"nihao",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.release_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseRemarks.this);
                builder.setTitle("放行备注添加");//设置标题
                //实例化两个控件
                final EditText remags = new EditText(ReleaseRemarks.this);
                TextView eptv = new TextView(ReleaseRemarks.this);
                //将控件添加到线性布局
                LinearLayout linearLayout = new LinearLayout(ReleaseRemarks.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);//设置线性布局的排列方式
                linearLayout.addView(eptv);
                linearLayout.addView(remags);
                builder.setView(linearLayout);//将线性布局添加到build
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (TextUtils.isEmpty(remags.getText())) {
                            Toasty.error(ReleaseRemarks.this, "输入信息不能为空", Toast.LENGTH_SHORT, true).show();
                        } else {
                            String sql = "insert into " + Constant.TABLE_USER + "(" + Constant.RRSTRING + ") values('" + remags.getText().toString() + "')";
                            //测试数据
                            //String sql="insert into "+  Constant.TABLE_UNAME+"("+Constant.UNAME+","+Constant.UTIME+") values('"+remags.getText().toString()+"',"+gettime()+"')";
                            //Log.e("TAG",sql+"时间："+gettime());
                            //获取数据库操作对象
                            SQLiteDatabase db = App.dbHelper.getWritableDatabase();
                            db.execSQL(sql);//执行sql语句
                            Toasty.success(ReleaseRemarks.this, "添加成功", Toast.LENGTH_SHORT, true).show();
                            db.close();
                            query_DB();
                        }
                    }
                });
                builder.show();
                break;
        }
    }

    private void query_DB() {
        String sql_user = "select * from " + TABLE_USER;
        getdata(sql_user);
    }

    //获取当前时间
    private long gettime() {
        Date date = new Date();
        long time = (date.getTime() / 1000);
        //Log.e("TAG",date.getTime()/1000+"");
        return time;
    }


    //执行sql语句
    private void getdata(String sql_user) {
        list.clear();
        // String sql_user="select * from "+ Constant.TABLE_USER;
        SQLiteDatabase sdb = App.dbHelper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(sql_user, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String s = cursor.getString(cursor.getColumnIndex(Constant.RRSTRING));
                Log.e("TAG", s);
                list.add(s);
            }
            releaseRecyclerView.setAdapter(remarksAdapter);
            cursor.close();
            sdb.close();
        }
    }

    //三个拖拽方法
    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
        Log.d("TAG", "move from: " + source.getAdapterPosition() + " to: " + target.getAdapterPosition());
    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

    }


    //四个滑动删除方法
    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
        Log.e("TAG", "开始：" + pos);
    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
        Log.e("TAG", "未知1：" + pos);
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        String sql = "delete from " + Constant.TABLE_USER + " where " + Constant.RRSTRING + "=" + "'" + list.get(pos) + "'";
        Log.e("TAG", sql);
        SQLiteDatabase db = App.dbHelper.getWritableDatabase();
        db.execSQL(sql);//执行sql语句
        Toasty.success(ReleaseRemarks.this, "删除成功", Toast.LENGTH_SHORT, true).show();
        //将数据从list移除
        //list.remove(pos);
        //刷新适配器
        //remarksAdapter.notifyDataSetChanged();
        db.close();
    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
        canvas.drawColor(ContextCompat.getColor(ReleaseRemarks.this, R.color.colorPrimaryDark));
        canvas.drawText("滑动删除", 50, 90, paint);
        //Log.e("TAG","未知3："+dX+"+"+dY+"+"+isCurrentlyActive);
    }

    private void chargePass(String url, String cpjson) {
        dialog1 = new ZLoadingDialog(ReleaseRemarks.this);
        dialog1.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型STAR_LOADING 五角星
                .setLoadingColor(Color.parseColor("#55BEB7"))//颜色
                .setHintText("Loading...")
                .setHintTextColor(Color.parseColor("#55BEB7"))
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .show();
        Log.e("TAG", cpjson);
        OkHttpUtils.postString().url(url)
                .content(cpjson)
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
                    int rstat = datajson.getInt("rstat");
                    //String sid=datajson.getString("sid");
                    if (rstat == 0) {
                            Constant.srmon="0元（免费放行）";
                            free_start_voice();
                            Intent zdintent = new Intent(ReleaseRemarks.this, CaroutSuccessful.class);
                            zdintent.putExtra("carnum", carnum);
                            zdintent.putExtra("jfType", jfType);
                            zdintent.putExtra("ctype", ctype);
                            zdintent.putExtra("ctime", ctime);
                            zdintent.putExtra("itime", itime);
                            zdintent.putExtra("pvrefresh", pvrefresh);
                            zdintent.putExtra("paytype", 4);
                            zdintent.putExtra("caroutprint", true);
                            startActivity(zdintent);
                            finish();
                            caroutactivity.finish();
                    } else {
                        Toasty.error(ReleaseRemarks.this, "订单无效需重新发起", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
