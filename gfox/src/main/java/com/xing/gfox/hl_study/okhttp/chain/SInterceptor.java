package com.xing.gfox.hl_study.okhttp.chain;

import java.io.IOException;

import com.xing.gfox.hl_study.okhttp.SResponse;

public interface SInterceptor {
    SResponse intercept(InterceptorChain chain) throws IOException;
}
