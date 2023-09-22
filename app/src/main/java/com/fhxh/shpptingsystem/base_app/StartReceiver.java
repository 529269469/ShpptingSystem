package com.fhxh.shpptingsystem.base_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fhxh.shpptingsystem.ui.LoginActivity;

/**
 * Created by  on 2018/6/4/004.
 * 开机监听
 */

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
        }
    }
}
