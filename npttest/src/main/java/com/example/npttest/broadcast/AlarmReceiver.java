package com.example.npttest.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.npttest.server.Heartbeat;

/**
 * Created by liuji on 2017/11/7.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override

    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, Heartbeat.class);

        context.startService(i);

    }

}

