package com.xing.gfox.hardware.netWork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

import com.xing.gfox.liveEventBus.LiveEventBus;

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
//            ViseLog.d(intent);
            NetworkInfo networkInfo1 = (NetworkInfo) intent.getExtras().get("networkInfo");
            NetStateInfo info = new NetStateInfo();
            info.setTag("onReceive");
            info.setNetWorkType(networkInfo1.getTypeName());
            if (networkInfo1.getState() == NetworkInfo.State.CONNECTED) {
                info.setState(NetWorkState.CONNECT);
            } else {
                info.setState(NetWorkState.DISCONNECT);
            }
            LiveEventBus.get("netInfo").post(info);
        }
    }

    //通过广播方式监听
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
