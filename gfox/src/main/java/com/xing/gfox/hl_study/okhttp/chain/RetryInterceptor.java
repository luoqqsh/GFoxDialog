package com.xing.gfox.hl_study.okhttp.chain;

import android.util.Log;

import com.xing.gfox.hl_study.okhttp.SCall;
import com.xing.gfox.hl_study.okhttp.SResponse;

import java.io.IOException;


public class RetryInterceptor implements SInterceptor {
    @Override
    public SResponse intercept(InterceptorChain chain) throws IOException {
        Log.e("interceprot", "重试拦截器....");
        SCall call = chain.call;
        IOException exception = null;
        for (int i = 0; i < chain.call.client().retrys(); i++) {
            if (call.isCanceled()) {
                throw new IOException("Canceled");
            }
            try {
                SResponse response = chain.proceed();
                return response;
            } catch (IOException e) {
                exception = e;
            }
        }
        throw exception;
    }
}
