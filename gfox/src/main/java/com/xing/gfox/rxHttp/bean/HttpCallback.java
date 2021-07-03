package com.xing.gfox.rxHttp.bean;

public interface HttpCallback<T> {

    void onSuccess(T result);

    void onFail(String error);

}
