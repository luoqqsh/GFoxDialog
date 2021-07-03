package com.xing.gfoxdialog.api;

public class BaseApiResponse<T>   {

    private String msg;

    public String getErrorMsg() {
        return msg;
    }

    public void setErrorMsg(String message) {
        this.msg = message;
    }

    private int code;
    private String sign;
    private String content;
    private T data;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public String getSign() {
        return sign;
    }

    public String getContent() {
        return content;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == 200;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
