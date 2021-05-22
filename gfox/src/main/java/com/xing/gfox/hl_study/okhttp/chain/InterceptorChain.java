package com.xing.gfox.hl_study.okhttp.chain;

import com.xing.gfox.hl_study.okhttp.SCall;
import com.xing.gfox.hl_study.okhttp.SHttpCodec;
import com.xing.gfox.hl_study.okhttp.SHttpConnection;
import com.xing.gfox.hl_study.okhttp.SResponse;

import java.io.IOException;
import java.util.List;



//拦截器链
public class InterceptorChain {
    final List<SInterceptor> interceptors;
    final int index;
    final SCall call;
    final SHttpConnection connection;
    final SHttpCodec httpCodec = new SHttpCodec();

    public InterceptorChain(List<SInterceptor> interceptors, int index, SCall call, SHttpConnection connection) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.connection = connection;
    }

    public SResponse proceed() throws IOException {
        return proceed(connection);
    }

    public SResponse proceed(SHttpConnection connection) throws IOException {
        SInterceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors, index + 1, call, connection);
        SResponse response = interceptor.intercept(next);
        return response;
    }
}
