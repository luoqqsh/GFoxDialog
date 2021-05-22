package com.xing.gfox.rxHttp.jiu;

public interface OnHttpCallback<T> {

    void onSuccess(T result);

    void onFail(String error);

}
