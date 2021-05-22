package com.xing.gfox.hardware.lockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.xing.gfox.liveEventBus.LiveEventBus;
import com.xing.gfox.log.ViseLog;


public class ScreenBroadcastReceiver extends BroadcastReceiver {
    private static ScreenBroadcastReceiver screenBroadcastReceiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ViseLog.showInfo("广播Action = " + action);
        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LiveEventBus.get("lockInfo", String.class).post("锁屏");
            ViseLog.showInfo("锁屏");
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            LiveEventBus.get("lockInfo", String.class).post("解锁");
            ViseLog.showInfo("解锁");
        } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
            LiveEventBus.get("lockInfo", String.class).post("开屏");
            ViseLog.showInfo("开屏");
        }
    }

    public static ScreenBroadcastReceiver getInstance() {
        if (screenBroadcastReceiver == null) {
            screenBroadcastReceiver = new ScreenBroadcastReceiver();
        }
        return screenBroadcastReceiver;
    }

    public void register(Context context) {
        ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        context.getApplicationContext().registerReceiver(screenBroadcastReceiver, filter);
    }

    public void unRegister(Context context) {
        if (screenBroadcastReceiver != null) {
            context.getApplicationContext().unregisterReceiver(screenBroadcastReceiver);
            screenBroadcastReceiver = null;
        }
    }
}
