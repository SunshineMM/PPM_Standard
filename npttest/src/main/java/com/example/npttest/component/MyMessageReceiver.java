package com.example.npttest.component;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.example.npttest.App;
import com.example.npttest.activity.CaroutSuccessful;
import com.example.npttest.constant.Constant;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.zyao89.view.zloading.ZLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.example.npttest.activity.AliPay.alipayactivity;
import static com.example.npttest.activity.AliPay.alipaycontext;
import static com.example.npttest.activity.CaroutChargeActivity.caroutactivity;
import static com.example.npttest.activity.WechatPay.Wechatactivity;
import static com.example.npttest.activity.WechatPay.wechatcontext;


/**
 * @author: 正纬
 * @since: 15/4/9
 * @version: 1.1
 * @feature: 用于接收推送的通知和消息
 */
public class MyMessageReceiver extends MessageReceiver {

    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    ZLoadingDialog dialog1;
    SynthesizerListener mSynListener;
    private String title;
    private String content;

    /**
     * 推送通知的回调方法
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        if ( null != extraMap ) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG,"@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            Log.i(REC_TAG,"@收到通知 && 自定义消息为空");
        }
        Log.e("TAG","我是广播收到一条推送通知 ： " + title + ", summary:" + summary);
       // MainApplication.setConsoleText("收到一条推送通知 ： " + title + ", summary:" + summary);
    }

    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html?spm=5176.product30047.6.620.wjcC87#h3-3-4-basiccustompushnotification-api
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("TAG","我是广播 onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
        //MainApplication.setConsoleText("onNotificationReceivedInApp ： " + " : " + title + " : " + summary);
    }

    /**
     * 推送消息的回调方法
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e("TAG","我是广播 收到一条推送消息 ： " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        //MainApplication.setConsoleText(cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        content=cPushMessage.getContent();
        title=cPushMessage.getTitle();
        if (content.equals("success#wxpay,sid#"+Constant.sid)){
            wechatcarout_start_voice();
            Intent intent=new Intent(wechatcontext, CaroutSuccessful.class);
            intent.putExtra("carnum", Constant.carnum);
            intent.putExtra("jfType", Constant.jfType);
            intent.putExtra("ctype", Constant.ctype);
            intent.putExtra("ctime", Constant.ctime);
            intent.putExtra("itime", Constant.itime);
            intent.putExtra("pvrefresh", Constant.pvrefresh);
            intent.putExtra("paytype", 2);
            intent.putExtra("caroutprint", true);
            wechatcontext.startActivity(intent);
            Wechatactivity.finish();
            caroutactivity.finish();
            //wechatcontext.startActivity(new Intent(wechatcontext, CaroutSuccessful.class));
        }else if (content.equals("success#alipay,sid#"+Constant.sid)){
            Log.e("TAG","支付宝支付成功");
            alicarout_start_voice();
            Intent intent1=new Intent(alipaycontext, CaroutSuccessful.class);
            intent1.putExtra("carnum", Constant.carnum);
            intent1.putExtra("jfType", Constant.jfType);
            intent1.putExtra("ctype", Constant.ctype);
            intent1.putExtra("ctime", Constant.ctime);
            intent1.putExtra("itime", Constant.itime);
            intent1.putExtra("pvrefresh", Constant.pvrefresh);
            intent1.putExtra("paytype", 3);
            intent1.putExtra("caroutprint", true);
            alipaycontext.startActivity(intent1);
            alipayactivity.finish();
            caroutactivity.finish();
        }else if (title.equals("176")){
            try {
                JSONObject jsonObject=new JSONObject(content);
                JSONObject datajsonObject=jsonObject.getJSONObject("data");
                int elot=datajsonObject.getInt("elot");
                int number=datajsonObject.getInt("number");
                int tlot=datajsonObject.getInt("tlot");
                App.surpluscar= String.valueOf(elot+tlot);
                Constant.pvcar= String.valueOf(number);
                Log.e("TAG","剩余车位：**************************"+App.surpluscar);
                //App.surpluscar= String.valueOf(elot+tlot);
                /*if (pvcarTv!=null){
                    pvcarTv.setText(String.valueOf(number));
                }*/
                //Constant.pvcar=String.valueOf(number);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //chargePass(App.serverurl);
    }

   /* private void chargePass(String url) {
        String cpjson = "{\"cmd\":\"141\",\"type\":\"" + Constant.TYPE + "\",\"code\":\"" + Constant.CODE + "\"," +
                "\"dsv\":\"" + Constant.DSV + "\",\"sid\":\"" + Constant.sid + "\",\"io\":\"1\",\"rstat\":\"0\"," +
                "\"ftype\":\"" + Constant.cdtp + "\",\"sale\":\"0\",\"reas\":\"000\",\"spare\":\"0\",\"sign\":\"abcd\"}";

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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String reasonjson = jsonObject.getString("reason");//操作成功
                    JSONObject resultjson = jsonObject.getJSONObject("result");
                    JSONObject datajson = resultjson.getJSONObject("data");
                    int rstat = datajson.getInt("rstat");
                    App.wmon = datajson.getDouble("emon");
                    //String sid=datajson.getString("sid");
                    Log.e("TAG","title:*********"+title);
                    if (rstat == 0) {
                       else {
                            Log.e("TAG","都不是");
                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }*/

    private void wechatcarout_start_voice() {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(wechatcontext, null);
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
        //mTts.startSpeaking(carnum+"出场成功！本次收费"+srmon+"元", mSynListener);
        char[] carnumber = Constant.carnum.toCharArray();
        if (carnumber.length == 7) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + "，" + "出场成功，本次收费" + Constant.srmon + "，停车时间" + Constant.pktime, mSynListener);
        } else if (carnumber.length == 8) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + String.valueOf(carnumber[7]) + "，" + "出场成功，本次收费" + Constant.srmon + "，停车时间" + Constant.pktime, mSynListener);
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

    private void alicarout_start_voice() {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(alipaycontext, null);
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
        //mTts.startSpeaking(carnum+"出场成功！本次收费"+srmon+"元", mSynListener);
        char[] carnumber = Constant.carnum.toCharArray();
        if (carnumber.length == 7) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + "，" + "出场成功，本次收费" + Constant.srmon + "，停车时间" + Constant.pktime, mSynListener);
        } else if (carnumber.length == 8) {
            mTts.startSpeaking(String.valueOf(carnumber[0]) + " " + String.valueOf(carnumber[1]) + " " + String.valueOf(carnumber[2])
                    + " " + String.valueOf(carnumber[3]) + " " + String.valueOf(carnumber[4]) + " " + String.valueOf(carnumber[5]) + " " + String.valueOf(carnumber[6]) + " " + String.valueOf(carnumber[7]) + "，" + "出场成功，本次收费" + Constant.srmon + "，停车时间" + Constant.pktime, mSynListener);
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

    /**
     * 从通知栏打开通知的扩展处理
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("TAG","我是广播 onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        //MainApplication.setConsoleText("onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        //maincontext.startActivity(new Intent(context, Test1.class));
    }

    /**
     * 通知删除回调
     * @param context
     * @param messageId
     */
    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.e("TAG", "我是广播 onNotificationRemoved ： " + messageId);
        //MainApplication.setConsoleText("onNotificationRemoved ： " + messageId);
    }

    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("TAG","我是广播 onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
        //MainApplication.setConsoleText("onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }
}