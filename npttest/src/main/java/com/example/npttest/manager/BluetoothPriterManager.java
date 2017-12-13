package com.example.npttest.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.example.npttest.constant.Constant;
import com.example.npttest.util.SPUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BluetoothPriterManager {
    public static final String CONNECT_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private BluetoothAdapter mAdapter;
    BluetoothDevice device;
    BluetoothSocket socket;
    private static Context context;
    private static String address;
    private static BluetoothPriterManager bluetoothPriterManager;

    private BluetoothPriterManager(Context context) {
        this.context = context;
        this.address = (String) SPUtils.get(context, Constant.DEVICE_ADDRESS,"");

    }

    public static BluetoothPriterManager getInstance(Context context) {
        if (bluetoothPriterManager == null) {
            bluetoothPriterManager = new BluetoothPriterManager(context);
        }
        return bluetoothPriterManager;
    }

    public static BluetoothPriterManager getNewInstance(Context context) {
        bluetoothPriterManager = null;
        return getInstance(context);
    }

    public void sendMessage(String str) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        device = mAdapter.getRemoteDevice(address);

        try {
            // BluetoothSocket temp = device
            // .createRfcommSocketToServiceRecord(UUID
            // .fromString("00001101-0000-1000-8000-00805F9B34FB"));
            // socket = temp;
            initSocket();
            socket.connect();
            // connect();
            OutputStream os = socket.getOutputStream();
            os.write(str.getBytes("gbk"));
            os.flush();
            os.write(new byte[]{0x1b, 0x66, 0x01, 0x04});
            os.write(new byte[]{0x1b, 0x70, 0x00, 0x10, 0x15});
            os.close();
            socket.close();
            socket = null;
        } catch (IOException e) {
            Log.d("", "bluetooth send message error", e);
            e.printStackTrace();
        }

    }

    public void sendShiftMessage(String str) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        device = mAdapter.getRemoteDevice(address);

        try {
            // BluetoothSocket temp = device
            // .createRfcommSocketToServiceRecord(UUID
            // .fromString("00001101-0000-1000-8000-00805F9B34FB"));
            // socket = temp;
            initSocket();
            socket.connect();
            // connect();
            OutputStream os = socket.getOutputStream();
            os.write(str.getBytes("gbk"));
            os.flush();
            os.write(new byte[]{0x1b, 0x66, 0x01, 0x04});
            os.close();
            socket.close();
            socket = null;
        } catch (IOException e) {
            Log.d("", "bluetooth send message error", e);
            e.printStackTrace();
        }

    }

    public void sendOpenGateMessage() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        device = mAdapter.getRemoteDevice(address);

        try {
            initSocket();
            socket.connect();
            // connect();
            OutputStream os = socket.getOutputStream();
            os.write(new byte[]{0x1b, 0x70, 0x00, 0x10, 0x15});
            os.close();
            socket.close();
            socket = null;
        } catch (IOException e) {
            Log.d("", "bluetooth send message error", e);
            e.printStackTrace();
        }

    }

    private synchronized void connect() {
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/*
     *
	 * 初始化Socket
	 */

    private void initSocket() {
        if (null == socket) {
            BluetoothSocket temp = null;
            try {
                // temp
                // =device.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));

                // 以上取得socket方法不能正常使用， 用以下方法代替

                Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});

                temp = (BluetoothSocket) m.invoke(device, 1);

                // 怪异错误： 直接赋值给socket,对socket操作可能出现异常， 要通过中间变量temp赋值给socket

            } catch (SecurityException e) {

                e.printStackTrace();

            } catch (NoSuchMethodException e) {

                e.printStackTrace();

            } catch (IllegalArgumentException e) {

                e.printStackTrace();

            } catch (IllegalAccessException e) {

                e.printStackTrace();

            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            socket = temp;
        }
    }

    public void sentOpenGate() {
        //sendMessage(Common4Ipms.workLogItem2PrintString(context, workLogItem));
        sendOpenGateMessage();
    }

    public void printShiftItem() {
        //sendShiftMessage(Common4Ipms.workLog2PrintStr4Mould());
        sendShiftMessage("******************\n"+"喂喂喂\n"+"是干哥吗\n"+"社会干\n"+"我想买把枪\n"+"让子弹上膛\n"+"他得哒哒哒\n"+
                "还得冒蓝火\n"+"买枪混社会的小伙就是你\n"+"谢谢\n"+"甘继康敬上\n"+"\n"+"\n"+"\n"+"\n******************");

    }
    public void printTestInfo() {
        sendMessage("蓝牙打印机连接成功\n");
    }
}
