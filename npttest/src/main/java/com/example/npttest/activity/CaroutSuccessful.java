package com.example.npttest.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.npttest.App;
import com.example.npttest.R;
import com.example.npttest.constant.Constant;
import com.example.npttest.manager.ActivityManager;
import com.example.npttest.tool.DateTools;
import com.example.npttest.tool.TimeDifferTools;
import com.example.npttest.util.TimeUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.kyleduo.switchbutton.SwitchButton;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.ThermalPrinter;

import java.util.Date;
import java.util.Hashtable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


/**
 * Created by liuji on 2017/9/19.
 */

public class CaroutSuccessful extends NoStatusbarActivity implements CompoundButton.OnCheckedChangeListener {


    @Bind(R.id.carout_suful_carnum)
    TextView caroutSufulCarnum;
    @Bind(R.id.carout_suful_cartype)
    TextView caroutSufulCartype;
    @Bind(R.id.carout_suful_pztype)
    TextView caroutSufulPztype;
    @Bind(R.id.textView8)
    TextView textView8;
    @Bind(R.id.carout_suful_ptime)
    TextView caroutSufulPtime;
    @Bind(R.id.carout_suful_itime)
    TextView caroutSufulItime;
    @Bind(R.id.carout_suful_outtime)
    TextView caroutSufulOuttime;
    @Bind(R.id.carout_suful_submit)
    Button caroutSufulSubmit;
    @Bind(R.id.carout_suful_sbtn)
    SwitchButton caroutSufulSbtn;
    private String carnum, cartype, jfType;
    private int ctype;
    private long ctime, itime;
    private Boolean pvrefresh;
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
    private boolean booprint=false;
    private int paytype;
    private String spaytype;
    private boolean caroutprint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carout_successful);
        ButterKnife.bind(this);
        SpeechUtility.createUtility(CaroutSuccessful.this, SpeechConstant.APPID + "=59df2c0c");
        Intent intent = getIntent();
        carnum = intent.getStringExtra("carnum");
        ctype = intent.getIntExtra("ctype", 0);
        jfType = intent.getStringExtra("jfType");
        ctime = intent.getLongExtra("ctime", 0);
        itime = intent.getLongExtra("itime", 0);
        pvrefresh = intent.getBooleanExtra("pvrefresh", false);
        paytype=intent.getIntExtra("paytype",1);
        caroutprint=intent.getBooleanExtra("caroutprint",false);
        getpaytype();
        Log.e("TAG", pvrefresh + "");
        String pktime =new TimeDifferTools(this).getDistanceTime(itime * 1000, ctime * 1000);
        judge();
        caroutSufulCarnum.setText(carnum);
        caroutSufulCartype.setText(cartype);
        caroutSufulPztype.setText(jfType);
        caroutSufulOuttime.setText(DateTools.getDate(ctime * 1000) + "");
        caroutSufulItime.setText(DateTools.getDate(itime * 1000) + "");
        caroutSufulPtime.setText(pktime);
        caroutSufulSbtn.setOnCheckedChangeListener(this);
        ActivityManager.getInstance().addActivity(this);
        handler=new MyHandler();
        //检测打印机
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ThermalPrinter.start(CaroutSuccessful.this);
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
                    ThermalPrinter.stop(CaroutSuccessful.this);
                }
            }
        }).start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getpaytype(){
        switch (paytype){
            case 1:
                spaytype=getString(R.string.cash);
                break;
            case 2:
                spaytype=getString(R.string.WeChat);
                break;
            case 3:
                spaytype=getString(R.string.Alipay);
                break;
            case 4:
                spaytype=getString(R.string.no);
                break;
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOPAPER:
                    noPaperDlg();
                    break;
                case OVERHEAT:
                    AlertDialog.Builder overHeatDialog = new AlertDialog.Builder(CaroutSuccessful.this);
                    overHeatDialog.setTitle(getString(R.string.reminder));
                    overHeatDialog.setMessage(R.string.print_too_hot);
                    overHeatDialog.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    overHeatDialog.show();
                    break;
                case CANCELPROMPT:
                    if (progressDialog != null && !CaroutSuccessful.this.isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    break;

                case PRINTVERSION:
                    if (msg.obj.equals("1")) {
                        Log.e("TAG",printVersion);
                    } else{
                        //Toast.makeText(InputCarnum.this, "", Toast.LENGTH_LONG).show();
                        Log.e("TAG","未找到打印机");
                    }
                    break;
                case 0x0123:
                    App.outRefresh = false;
                    App.chaRefresh=false;
                    if (pvrefresh) {
                        App.pvRefresh = false;
                    } else {
                        startActivity(new Intent(CaroutSuccessful.this, IndexActivity.class));
                    }
                    finish();
                    break;
                default:
                    Toast.makeText(CaroutSuccessful.this, getString(R.string.printer_is_abnormal) , Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }
    }

    //缺纸
    private void noPaperDlg() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(CaroutSuccessful.this);
        dlg.setTitle(R.string.print_out_of_paper);
        dlg.setMessage(R.string.enter_the_paper_admission_printing);
        dlg.setCancelable(false);
        dlg.setPositiveButton(getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ThermalPrinter.stop(CaroutSuccessful.this);
                finish();
            }
        });
        dlg.show();
    }

    //获取当前时间
    private long gettime() {
        Date date = new Date();
        long time = (date.getTime() / 1000);
        //Log.e("TAG",date.getTime()/1000+"");
        return time;
    }

    //打印文本
    private class contentPrintThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                ThermalPrinter.start(CaroutSuccessful.this);
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
                String str="\n          " +
                        getString(R.string.billing_bills)
                        + "\n----------------------------"
                        + "\n" +
                        getString(R.string.number_plate)+carnum
                        + "\n" +
                        getString(R.string.order_number)+ Constant.sid
                        + "\n" +
                        getString(R.string.date)+ TimeUtils.getStrTime(String.valueOf(gettime()))
                        + "\n----------------------------"
                        + "\n" +
                        getString(R.string.billing_type_)+jfType
                        + "\n" +
                        getString(R.string.admission_time_)+DateTools.getDate(itime * 1000) + ""
                        + "\n" +
                        getString(R.string.playing_time_)+DateTools.getDate(ctime * 1000) + ""
                        + "\n" +
                        getString(R.string.parking_time_)+ Constant.pktime
                        + "\n" +
                        getString(R.string.amount_receivable_)+ Constant.snmon+"元"
                        + "\n" +
                        getString(R.string.discounted_price)+ Constant.ssmon+"元"
                        + "\n" +
                        getString(R.string.amount_received)+ Constant.srmon
                        + "\n" +
                        getString(R.string.payment_types)+spaytype
                        + "\n" +
                        getString(R.string.watchman)+ Constant.username
                        + "\n" +
                        getString(R.string.address) + Constant.adds
                        + "\n----------------------------"
                        + "\n          " +
                        getString(R.string.welcome_to_the_next_visit)
                        ;
                Bitmap bitmap = CreateCode(Constant.sid, BarcodeFormat.QR_CODE, 256, 256);
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
                ThermalPrinter.stop(CaroutSuccessful.this);
            }
        }
    }

    @OnClick(R.id.carout_suful_submit)
    public void onViewClicked() {
        if (booprint){
            if (printVersion==null){
                Toasty.info(CaroutSuccessful.this,getString(R.string.check_the_printer),Toast.LENGTH_SHORT,true).show();
            }else if (!caroutprint){
                Toasty.info(CaroutSuccessful.this,getString(R.string.not_find_the_order_charge_record),Toast.LENGTH_SHORT,true).show();
            }else {
                progressDialog = ProgressDialog.show(CaroutSuccessful.this, getString(R.string.print), getString(R.string.printing));
                new contentPrintThread().start();
            }
        }else {
            App.outRefresh = false;
            App.chaRefresh=false;
            if (pvrefresh) {
                App.pvRefresh = false;
                App.zcRefresh=true;
            } else {
                startActivity(new Intent(CaroutSuccessful.this, IndexActivity.class));
            }
            finish();
        }

        //finish();
    }

    private void judge() {
        switch (ctype) {
            case 1:
                cartype = getString(R.string.motorcycle);
                break;
            case 2:
                cartype = getString(R.string.compacts);
                break;
            case 3:
                cartype = getString(R.string.Intermediate);
                break;
            case 4:
                cartype = getString(R.string.large_vehicle);
                break;
            case 5:
                cartype = getString(R.string.transporter);
                break;
            case 6:
                cartype = getString(R.string.spare_car);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b){
            booprint=true;
        }else {
            booprint=false;
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
}
