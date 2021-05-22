package com.xing.gfox.hl_study.okhttp.chain;


import com.xing.gfox.hl_study.okhttp.SResponse;

import java.io.IOException;

public interface SInterceptor {
    SResponse intercept(InterceptorChain chain) throws IOException;
}
