package com.xing.gfox.hardware.netWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.util.Arrays;

import com.xing.gfox.liveEventBus.LiveEventBus;
import com.xing.gfox.log.ViseLog;

/**
 * 安卓Q推荐使用这个
 */
public class NetWorkCallBack {
    private static NetWorkCallBack netWorkCallBack;
    private static NetStateInfo info;

    public static NetWorkCallBack getInstance() {
        if (netWorkCallBack == null) {
            netWorkCallBack = new NetWorkCallBack();
            info = new NetStateInfo();
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
            ViseLog.d(networkCapabilities);
            // 表明此网络连接成功验证
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    info.setNetWorkType("WIFI");
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    info.setNetWorkType("mobile");

                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                    // 使用数据网络
                    ViseLog.d("当前在使用蓝牙上网");
                } else {
                    ViseLog.d("当前在使用其他网络");
                    // 未知网络，包括蓝牙、VPN等
                }
                info.setTag("Android10监听网络变化广播1 onCapabilitiesChanged");
                info.setState(NetWorkState.CONNECT);
                LiveEventBus.get("netInfo").post(info);
            }
        }

        // 网络断开
        public void onLost(Network network) {
            // 如果通过ConnectivityManager#getActiveNetwork()返回null，表示当前已经没有其他可用网络了。
            info.setTag("Android10监听网络变化广播2 onLost");
            info.setState(NetWorkState.LOST);
            LiveEventBus.get("netInfo").post(info);
        }

        /**
         * 网络可用的回调
         * */
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            info.setState(NetWorkState.AVAILABLE);
            info.setTag("Android10监听网络变化广播3 onAvailable");
            LiveEventBus.get("netInfo").post(info);
        }

        /**
         * 当建立网络连接时，回调连接的属性
         * */
        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            info.setDns(Arrays.toString(linkProperties.getDnsServers().toArray(new InetAddress[0])));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.setPrivateDnsServerName(linkProperties.getPrivateDnsServerName());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                info.setIpAddress(linkProperties.getDhcpServerAddress().getHostAddress());
            }
            info.setTag("Android10监听网络变化广播4 onLinkPropertiesChanged");
            LiveEventBus.get("netInfo").post(info);
        }

        //当访问的网络被阻塞或者解除阻塞时调用
        @Override
        public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
            super.onBlockedStatusChanged(network, blocked);
            info.setBlocked(blocked);
            info.setTag("Android10监听网络变化广播5 onBlockedStatusChanged");
            LiveEventBus.get("netInfo").post(info);
        }

        /**
         * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
         * */
        @Override
        public void onLosing(Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
            info.setState(NetWorkState.LOST);
            info.setTag("Android10监听网络变化广播6 onLosing");
            LiveEventBus.get("netInfo").post(info);
        }

        /**
         * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
         * */
        @Override
        public void onUnavailable() {
            super.onUnavailable();
            info.setState(NetWorkState.LOST);
            info.setTag("Android10监听网络变化广播7 onUnavailable");
            LiveEventBus.get("netInfo").post(info);
        }
    };


    // 注册回调
    public void registerNetworkCallback(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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