package com.xing.gfoxdialog.BaseApp;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.xing.gfox.base.app.AppInit;
import com.xing.gfox.util.U_thread;


public class DialogApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInit.init(this,true);
        AppInit.printInformation();
//            x5Manager.getInstance().init(this);
//            LiveEventBus.config().supportBroadcast(this);//跨进程通过广播
    }
}
