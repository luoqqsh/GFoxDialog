package com.xing.gfox.hardware.netWork;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface NetWorkType {
    String NO = "no";//无连接
    String UNKNOWN = "unknown";//未知
    String WIFI = "wifi";//wifi
    String Mobile2G = "2g";//2g
    String Mobile3G = "3g";//3g
    String Mobile4G = "4g";//4g
    String Mobile5G = "5g";//5g
    String ETHERNET = "ethernet";//网线
    String VPN = "vpn";//vpn
    String BLUETOOTH = "bluetooth";//蓝牙
    String LOWPAN = "lowpan";//
}

