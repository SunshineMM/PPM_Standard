package com.example.npttest.util;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;

import com.example.npttest.R;

/**
 * Created by Administrator on 2017/7/24.
 */

public class LicenseKeyboardUtil_cario {
    private Context ctx;
    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字字母键盘
    private String provinceShort[];
    private String letterAndDigit[];
    private EditText edits[];
    public static int currentEditText_cario = 0;//默认当前光标在第一个EditText
    private String license = "";
    public static int etsize_cario=6;
    public LicenseKeyboardUtil_cario(Context ctx, EditText edits[]) {
        this.ctx = ctx;
        this.edits = edits;
        k1 = new Keyboard(ctx, R.xml.province_short_keyboard);
        k2 = new Keyboard(ctx, R.xml.lettersanddigit_keyboard);
        keyboardView = (KeyboardView) ((Activity)ctx).findViewById(R.id.keyboard_view);
       // keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(true);
        //设置键盘按键监听器
        keyboardView.setOnKeyboardActionListener(listener);
        provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "港", "澳", "台", "宁"};

        letterAndDigit = new String[]{"0","1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "Z", "X", "C", "V", "B", "N", "M"};
        //edits[currentEditText].setBackgroundColor(Color.parseColor("#ff0000"));
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }
        @Override
        public void swipeLeft() {
        }
        @Override
        public void swipeDown() {
        }
        @Override
        public void onText(CharSequence text) {
        }
        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            if(primaryCode == 112){ //xml中定义的删除键值为112
                edits[currentEditText_cario].setText("");//将当前EditText置为""并currentEditText-1
                //edits[currentEditText].setBackgroundColor(Color.parseColor("#ffffff"));
                currentEditText_cario--;
                if(currentEditText_cario < 1){
                    //切换为省份简称键盘
                    keyboardView.setKeyboard(k1);
                }
                if(currentEditText_cario < 0){
                    currentEditText_cario = 0;
                }
                initcolor();
                edits[currentEditText_cario].setBackgroundResource(R.drawable.keyboard_bg_red);
            }else if(primaryCode == 66){ //xml中定义的完成键值为66
                hideKeyboard();
                /*Intent intent = new Intent();
                //String license = "";
                for (int i=0;i<8;i++){
                    license += edits[i].getText().toString();
                }
                intent.putExtra(CarinActivity.INPUT_LICENSE_KEY, license);
                intent.setAction(CarinActivity.INPUT_LICENSE_COMPLETE);
                ctx.sendBroadcast(intent);*/
            } else if (primaryCode==77){
                hideKeyboard();
            }else { //其它字符按键
                if (currentEditText_cario == 0) {
                    //如果currentEditText==0代表当前为省份键盘,
                    // 按下一个按键后,设置相应的EditText的值
                    // 然后切换为字母数字键盘
                    //currentEditText+1
                    edits[0].setText(provinceShort[primaryCode]);
                    currentEditText_cario = 1;
                    //切换为字母数字键盘
                    keyboardView.setKeyboard(k2);
                    initcolor();
                    edits[currentEditText_cario].setBackgroundResource(R.drawable.keyboard_bg_red);
                }else{
                    //第二位必须大写字母
                    if(currentEditText_cario == 1 && !letterAndDigit[primaryCode].matches("[A-Z]{1}")){
                        return ;
                    }
                    edits[currentEditText_cario].setText(letterAndDigit[primaryCode]);
                    //edits[currentEditText].setBackgroundColor(Color.parseColor("#1CB9F0"));
                    currentEditText_cario++;
                    if (currentEditText_cario > etsize_cario) {
                        currentEditText_cario = etsize_cario;
                    }
                    initcolor();
                    edits[currentEditText_cario].setBackgroundResource(R.drawable.keyboard_bg_red);
                }
            }
        }
    };

    private void initcolor(){
        if (edits.length==7){
            edits[0].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[1].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[2].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[3].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[4].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[5].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[6].setBackgroundResource(R.drawable.keyboard_bg_white);
        }else if(edits.length==8) {
            edits[0].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[1].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[2].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[3].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[4].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[5].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[6].setBackgroundResource(R.drawable.keyboard_bg_white);
            edits[7].setBackgroundResource(R.drawable.keyboard_bg_white);
        }
    }

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        if (currentEditText_cario==0){
            keyboardView.setKeyboard(k1);
        }else {
            keyboardView.setKeyboard(k2);
        }
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        if (currentEditText_cario==0){
            keyboardView.setKeyboard(k1);
        }else {
            keyboardView.setKeyboard(k2);
        }
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }
}