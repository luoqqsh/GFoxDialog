package com.xing.gfox.hardware.netWork;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface NetWorkState {
    int NO = 0;//无连接
    int UNKNOWN = -1;//未知
    int CONNECT = 1;//连接
    int AVAILABLE = 2;//可用
    int LOST = 3;//断开
    int DISCONNECT = 4;//断开
}

