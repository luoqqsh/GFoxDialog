package com.xing.gfox.rxHttp.bean;

import java.io.Serializable;

/**
 * copyright：""
 * author：anthui
 * Version：1.0
 * creation date：2018/7/18
 * describe：
 */
public class ErrBean implements Serializable {

    //成功返回的 错误码
    public static final int CODE_SUCCESS = 200;
    //需要重新登录的错误码
    public static final int CODE_SHOULD_LOGIN = 401;
    //网络错误
    public static final int CODE_ERR_NET = -1;

    private int code;

    private ErrBean() {
    }

    public ErrBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
}
