package com.xing.gfox.rxHttp.interceptor;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private Headers addCommonHead;

    public HeaderInterceptor(Headers addCommonHead) {
        this.addCommonHead = addCommonHead;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder().headers(addCommonHead).build();
        return chain.proceed(newRequest);
    }
}
