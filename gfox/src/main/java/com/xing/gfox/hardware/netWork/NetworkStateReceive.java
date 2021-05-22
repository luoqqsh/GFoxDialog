package com.xing.gfox.hardware.netWork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xing.gfox.liveEventBus.LiveEventBus;
import com.xing.gfox.log.ViseLog;

import java.util.Objects;


public class NetworkStateReceive extends BroadcastReceiver {
    private static NetworkStateReceive networkStateReceive;

    public static NetworkStateReceive getInstance() {
        if (networkStateReceive == null) {
            networkStateReceive = new NetworkStateReceive();
        }
        return networkStateReceive;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                int type2 = networkInfo.getType();
                String typeName = networkInfo.getTypeName();
                ViseLog.d(type2 + " " + typeName);
                switch (type2) {
                    case 0://移动 网络    2G 3G 4G 都是一样的 实测 mix2s 联通卡
                        LiveEventBus.get("netInfo", String.class).post("移动网络");
                        ViseLog.showInfo("移动网络");
                        break;
                    case 1: //wifi网络
                        LiveEventBus.get("netInfo", String.class).post("WIFI");
                        ViseLog.showInfo("WIFI");
                        break;
                    case 9:  //网线连接
                        LiveEventBus.get("netInfo", String.class).post("网线");
                        ViseLog.showInfo("网线");
                        break;
                }
            } else {// 无网络
                LiveEventBus.get("netInfo", String.class).post("无网络");
                ViseLog.showInfo("无网络");
            }
        }
    }

    public void register(Context context) {
        NetworkStateReceive networkStateReceive = new NetworkStateReceive();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.getApplicationContext().registerReceiver(networkStateReceive, filter);
    }

    public void unRegister(Context context) {
        if (networkStateReceive != null) {
            context.getApplicationContext().unregisterReceiver(networkStateReceive);
            networkStateReceive = null;
        }
    }
}
