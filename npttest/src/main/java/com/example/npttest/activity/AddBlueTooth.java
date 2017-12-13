package com.example.npttest.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.npttest.R;
import com.example.npttest.constant.Constant;
import com.example.npttest.manager.BluetoothPriterManager;
import com.example.npttest.util.SPUtils;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuji on 2017/9/16.
 */

public class AddBlueTooth extends Activity {
    @Bind(R.id.blt_return)
    ImageView bltReturn;
    @Bind(R.id.blt_tv)
    TextView bltTv;
    @Bind(R.id.blt_ap_list)
    ListView bltApList;
    @Bind(R.id.blt_np_list)
    ListView bltNpList;
    @Bind(R.id.blt_btn)
    Button bltBtn;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> yi_adapter;
    private ArrayAdapter<String> wei_adapter;
    public static String DEVICE_ADDRESS = "device_address";
    public static String address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bluetooth);
        ButterKnife.bind(this);
        bltTv.setText((String) SPUtils.get(AddBlueTooth.this, Constant.BLUETOOTH_NAME, ""));
        yi_adapter = new ArrayAdapter<String>(this, R.layout.device_name);
        wei_adapter = new ArrayAdapter<String>(this, R.layout.device_name);
        bltApList.setAdapter(yi_adapter);
        bltApList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                SPUtils.put(AddBlueTooth.this, Constant.BLUETOOTH_NAME, info);
                Log.e("TAG", info);
                if (!info.equals("没有已配对的设备")) {
                    address = info.substring(info.length() - 17);
                    Log.e("TAG", "address" + address);
                    if (address != null) {
                        bltTv.setText(info);
                        SPUtils.put(AddBlueTooth.this, Constant.DEVICE_ADDRESS, address);
                        new Thread() {
                            public void run() {
                                Log.e("TAG", "线程");
                                BluetoothPriterManager.getNewInstance(AddBlueTooth.this)
                                        .printTestInfo();
                            }
                        }.start();
                    }
                }
            }
        });

        bltNpList.setAdapter(wei_adapter);
        bltNpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                SPUtils.put(AddBlueTooth.this, Constant.BLUETOOTH_NAME, info);
                Log.e("TAG", "info" + info);
                if (!info.equals("未找到可用设备")) {
                    address = info.substring(info.length() - 17);
                    Log.e("TAG", "address" + address);
                    if (address != null) {
                        bltTv.setText(info);
                        SPUtils.put(AddBlueTooth.this, Constant.DEVICE_ADDRESS, address);
                        new Thread() {
                            public void run() {
                                Log.e("TAG", "线程");
                                BluetoothPriterManager.getNewInstance(AddBlueTooth.this)
                                        .printTestInfo();
                            }

                            ;
                        }.start();
                    }
                }
            }
        });

        /**
         * 注册广播
         */
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();

        if (bluetoothDevices.size() > 0) {
            for (BluetoothDevice device : bluetoothDevices) {
                yi_adapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
        } else {
            String noDevices = "没有已配对的设备";
            yi_adapter.add(noDevices);
        }


    }

    @OnClick({R.id.blt_return, R.id.blt_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.blt_return:
                finish();
                break;
            case R.id.blt_btn:
                Log.e("TAG", "可点击");
                bltBtn.setText("正在查找...");
                bltBtn.setClickable(false);
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
                break;
        }
    }


    /**
     * 判断是否开启蓝牙
     *
     * @return
     */
    private boolean checkBluetooth() {
        if (bluetoothAdapter == null) {
            return false;
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableIntent);
            return false;
            //return bluetoothAdapter.enable();
        }
        return true;
    }

    /**
     * 广播接受器
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //已找到设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    wei_adapter.add(device.getName() + "\n"
                            + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //未找到设备
                setProgressBarIndeterminateVisibility(false);
                bltBtn.setClickable(true);
                bltBtn.setText("查找设备");
                if (wei_adapter.getCount() == 0) {
                    String noDevices = "未找到可用设备";
                    wei_adapter.add(noDevices);
                }
            }
        }
    };
}
