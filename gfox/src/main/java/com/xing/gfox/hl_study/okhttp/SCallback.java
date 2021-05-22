package com.xing.gfox.hl_study.okhttp;

public interface SCallback {
    //失败
    void onFailure(SCall call, Throwable throwable);
    //成功
    void onResponse(SCall call, SResponse response);
}
