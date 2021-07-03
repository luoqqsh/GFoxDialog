package com.xing.gfox.hl_study.okhttp.chain;

import android.util.Log;

import java.io.IOException;

import com.xing.gfox.hl_study.okhttp.SHttpConnection;
import com.xing.gfox.hl_study.okhttp.SHttpUrl;
import com.xing.gfox.hl_study.okhttp.SOkHttpClient;
import com.xing.gfox.hl_study.okhttp.SRequest;
import com.xing.gfox.hl_study.okhttp.SResponse;

public class ConnectionInterceptor implements SInterceptor {
    @Override
    public SResponse intercept(InterceptorChain chain) throws IOException {
        Log.e("interceprot", "连接拦截器....");
        SRequest request = chain.call.request();
        SOkHttpClient client = chain.call.client();
        SHttpUrl url = request.url();
        String host = url.getHost();
        int port = url.getPort();
        SHttpConnection httpConnection = client.connectionPool().get(host, port);
        if (httpConnection == null) {
            httpConnection = new SHttpConnection();
        } else {
            Log.e("call", "使用连接池......");
        }
        httpConnection.setRequest(request);
        SResponse response = chain.proceed(httpConnection);
        if (response.isKeepAlive()) {   //长连接
            client.connectionPool().put(httpConnection);
        }
        return null;
    }
}
