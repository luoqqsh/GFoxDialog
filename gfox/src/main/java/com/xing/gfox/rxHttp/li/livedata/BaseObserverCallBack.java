package com.xing.gfox.rxHttp.li.livedata;

public abstract class BaseObserverCallBack<T> {

    public abstract void onSuccess(T data);

    public void onSuccess(String data) {

    }

    public void onFail(String msg) {
    }

    public void onThrowable(Throwable t) {
    }

    public void onFinish() {

    }
}
