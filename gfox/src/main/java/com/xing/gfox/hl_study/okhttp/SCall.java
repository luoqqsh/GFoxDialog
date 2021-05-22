package com.xing.gfox.hl_study.okhttp;

import com.xing.gfox.hl_study.okhttp.chain.CallServiceInterceptor;
import com.xing.gfox.hl_study.okhttp.chain.ConnectionInterceptor;
import com.xing.gfox.hl_study.okhttp.chain.HeadersInterceptor;
import com.xing.gfox.hl_study.okhttp.chain.InterceptorChain;
import com.xing.gfox.hl_study.okhttp.chain.RetryInterceptor;
import com.xing.gfox.hl_study.okhttp.chain.SInterceptor;

import java.io.IOException;
import java.util.ArrayList;


public class SCall {
    SRequest request;
    SOkHttpClient client;
    /**
     * 是否执行过
     */
    boolean executed;

    //取消
    boolean canceled;

    public SCall(SRequest request, SOkHttpClient client) {
        this.request = request;
        this.client = client;
    }

    public SRequest request() {
        return request;
    }

    public SCall enqueue(SCallback callback) {
        //不能重复执行
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Execute");
            }
            executed = true;
        }
        client.dispatcher().enqueue(new AsyncCall(callback));
        return this;
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public SOkHttpClient client() {
        return client;
    }

    final class AsyncCall implements Runnable {

        private final SCallback callback;

        public AsyncCall(SCallback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            //是否已经通知过callback
            boolean signalledCallback = false;
            try {
                SResponse response = getResponse();
                if (canceled) {
                    signalledCallback = true;
                    callback.onFailure(SCall.this, new IOException("Canceled"));
                } else {
                    signalledCallback = true;
                    callback.onResponse(SCall.this, response);
                }
            } catch (IOException e) {
                if (!signalledCallback) {
                    callback.onFailure(SCall.this, e);
                }
            } finally {
                client.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.url().host;
        }
    }

    private SResponse getResponse() throws IOException {
        //添加拦截器
        ArrayList<SInterceptor> interceptors= new ArrayList<>();
        interceptors.addAll(client.interceptors());
        interceptors.add(new RetryInterceptor());
        interceptors.add(new HeadersInterceptor());
        interceptors.add(new ConnectionInterceptor());
        interceptors.add(new CallServiceInterceptor());

        InterceptorChain interceptorChain = new InterceptorChain(interceptors,
                0, this, null);
        return interceptorChain.proceed();
    }
}
