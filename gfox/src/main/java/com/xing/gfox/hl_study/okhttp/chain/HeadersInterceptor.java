package com.xing.gfox.hl_study.okhttp.chain;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import com.xing.gfox.hl_study.okhttp.SHttpCodec;
import com.xing.gfox.hl_study.okhttp.SRequest;
import com.xing.gfox.hl_study.okhttp.SResponse;

public class HeadersInterceptor implements SInterceptor {
    @Override
    public SResponse intercept(InterceptorChain chain) throws IOException {
        Log.e("interceprot","Http头拦截器....");
        SRequest request = chain.call.request();
        Map<String, String> headers = request.headers();
        headers.put(SHttpCodec.HEAD_HOST, request.url().getHost());
        headers.put(SHttpCodec.HEAD_CONNECTION, SHttpCodec.HEAD_VALUE_KEEP_ALIVE);
        if (null != request.body()) {
            String contentType = request.body().contentType();
            if (contentType != null) {
                headers.put(SHttpCodec.HEAD_CONTENT_TYPE, contentType);
            }
            long contentLength = request.body().contentLength();
            if (contentLength != -1) {
                headers.put(SHttpCodec.HEAD_CONTENT_LENGTH, Long.toString(contentLength));
            }
        }
        return chain.proceed();
    }
}
