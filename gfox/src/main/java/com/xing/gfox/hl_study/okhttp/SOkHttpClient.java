package com.xing.gfox.hl_study.okhttp;

import com.xing.gfox.hl_study.okhttp.chain.SInterceptor;

import java.util.ArrayList;
import java.util.List;


public class SOkHttpClient {
    //分发器
    private SDispatcher dispatcher;
    //连接池
    private SConnectionPool connectionPool;
    //重试连接次数
    private int retrys;
    //客户端拦截器集合
    private List<SInterceptor> interceptors;

    public SOkHttpClient() {
        this(new Builder());
    }

    public SOkHttpClient(Builder builder) {
        dispatcher = builder.dispatcher;
        connectionPool = builder.connectionPool;
        retrys = builder.retrys;
        interceptors = builder.interceptors;
    }

    public static final class Builder {
        /**
         * 队列 任务分发
         */
        SDispatcher dispatcher = new SDispatcher();
        SConnectionPool connectionPool = new SConnectionPool();
        //默认重试3次
        int retrys = 3;
        List<SInterceptor> interceptors = new ArrayList<>();

        public Builder retrys(int retrys) {
            this.retrys = retrys;
            return this;
        }

        public Builder addInterceptor(SInterceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }
    }

    public SCall newCall(SRequest request) {
        return new SCall(request, this);
    }

    public int retrys() {
        return retrys;
    }

    public SDispatcher dispatcher() {
        return dispatcher;
    }

    public SConnectionPool connectionPool() {
        return connectionPool;
    }

    public List<SInterceptor> interceptors() {
        return interceptors;
    }

}