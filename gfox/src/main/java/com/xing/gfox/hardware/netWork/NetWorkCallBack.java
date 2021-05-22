package com.xing.gfox.hardware.netWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

import com.xing.gfox.log.ViseLog;


/**
 * 安卓Q推荐使用这个
 */
public class NetWorkCallBack {
    private static NetWorkCallBack netWorkCallBack;

    public static NetWorkCallBack getInstance() {
        if (netWorkCallBack == null) {
            netWorkCallBack = new NetWorkCallBack();
        }
        return netWorkCallBack;
    }

    /**
     * Android10监听网络变化广播
     */
    ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
        // 可用网络接入
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            // 一般在此处获取网络类型然后判断网络类型，就知道时哪个网络可以用connected
//            ViseLog.d(networkCapabilities);
            // 表明此网络连接成功验证
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    // 使用WI-FI
                    ViseLog.d("当前在使用WiFi上网");
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    // 使用数据网络
                    ViseLog.d("当前在使用数据网络上网");
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                    // 使用数据网络
                    ViseLog.d("当前在使用蓝牙上网");
                } else {
                    ViseLog.d("当前在使用其他网络");
                    // 未知网络，包括蓝牙、VPN等
                }
            }
        }

        // 网络断开
        public void onLost(Network network) {
            ViseLog.d("onLost");
            // 如果通过ConnectivityManager#getActiveNetwork()返回null，表示当前已经没有其他可用网络了。
        }

        /**
         * 网络可用的回调
         * */
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            ViseLog.d("onAvailable");
        }

        /**
         * 当建立网络连接时，回调连接的属性
         * */
        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            ViseLog.d("onLinkPropertiesChanged");
        }

        //当访问的网络被阻塞或者解除阻塞时调用
        @Override
        public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
            super.onBlockedStatusChanged(network, blocked);
        }

        /**
         * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
         * */
        @Override
        public void onLosing(Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            ViseLog.d("onLosing");
        }

        /**
         * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
         * */
        @Override
        public void onUnavailable() {
            super.onUnavailable();
            ViseLog.d("onUnavailable");
        }
    };


    // 注册回调
    public void registerNetworkCallback(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            cm.registerNetworkCallback(builder.build(), callback);
        }
    }


    // 注销回调
    public void unregisterNetworkCallback(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            cm.unregisterNetworkCallback(callback);
        }
    }
}