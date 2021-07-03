package com.xing.gfox.base.interfaces;

public interface HCallBack<T> {
    void onSuccess(T result);

    void onFail(String error);
}
