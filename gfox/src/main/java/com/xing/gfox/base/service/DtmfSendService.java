package com.xing.gfox.base.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;

public class DtmfSendService extends Service {

    private String ACTION_SEND_DTMF = "net.bonvo.client.bonvobox.send_dtmf";
    private String TAG = "DtmfSendService";
    private SendDtmfReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void registerReceiver() {
        mReceiver = new SendDtmfReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SEND_DTMF);
        registerReceiver(mReceiver, filter);
    }

    public void sendDtmf(char keycode) {
        //https://blog.csdn.net/jinliang_890905/article/details/43458877
        //https://www.cnblogs.com/IT-Goddess/p/5725705.html
        //https://stackoverflow.com/questions/4745899/how-to-access-com-android-internal-telephony-callmanager
        //源码：http://androidxref.com/9.0.0_r3/xref/frameworks/opt/telephony/src/java/com/android/internal/telephony/CallManager.java#1216
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            final Class<?> classCallManager = classLoader.loadClass("com.android.internal.telephony.CallManager");
            Log.i(TAG, "Class loaded " + classCallManager.toString());

            Method methodGetInstance = classCallManager.getDeclaredMethod("getInstance");
            Log.i(TAG, "Method loaded " + methodGetInstance.getName());

            Object objectCallManager = methodGetInstance.invoke(null);
            Log.i(TAG, "Object loaded " + objectCallManager.getClass().getName());

            Method methodSendDtmf = classCallManager.getDeclaredMethod("sendDtmf", char.class);
            methodSendDtmf.setAccessible(true);
            Log.i(TAG, "Method loaded " + methodSendDtmf.getName());

            Log.i(TAG, "SendDtmf = " + methodSendDtmf.invoke(objectCallManager, keycode));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SendDtmfReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().contentEquals(ACTION_SEND_DTMF)) {
                char code = intent.getCharExtra("dtmfCode", '-');
                if (code != '-') {
                    Log.e(TAG, "发送dtmf=" + code);
                    sendDtmf(code);
                }
            }
        }
    }
}