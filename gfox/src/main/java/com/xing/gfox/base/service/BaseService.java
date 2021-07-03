package com.xing.gfox.base.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BaseService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //只会调用一次，但是调用n次bind，需要n次unbind解绑
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //多次调用start方法会多次调用该方法，一次stopService即可停止掉
        //返回值
        //START_STICKY_COMPATIBILITY = 0 为了兼容版本，在service被杀后，并不保证onStartCommand会被再一次调用
        //START_STICKY = 1 service被杀后，保留启动状态，但不保存intent，之后系统会尝试重启service，并重新回调onStartCommand方法
        //如果接下来没有其他start命令，intent会为null，需要判空。
        //START_NOT_STICKY = 2 常规操作，除非死之前还有组件调用startService，否则系统不保留启动状态并重启service
        //START_REDELIVER_STICKY = 3 service被杀后，系统将组织一次重启（除非调用了stopSelf()方法），被杀死前最后一次传递的intent将重新被执行。

//        stopSelf();//停止服务
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //彻底停止后，才会回调destroy
    }
}
