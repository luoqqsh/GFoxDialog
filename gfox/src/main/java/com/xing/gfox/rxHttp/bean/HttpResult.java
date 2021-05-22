package com.xing.gfox.rxHttp.bean;

import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {
    /** 错误码 */
    @SerializedName(Params.RES_CODE)
    private int    code;
    /** 错误信息 */
    @SerializedName(Params.RES_MSG)
    private String msg;
    /** 消息响应的主体 */
    @SerializedName(Params.RES_DATA)
    private T      data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
