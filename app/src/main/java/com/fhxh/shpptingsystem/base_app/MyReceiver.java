package com.fhxh.shpptingsystem.base_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SHUTDOWN.equals(action)) {
            Log.e("MyReceiver", "Android操作系统关机了......." );
        }
    }
}