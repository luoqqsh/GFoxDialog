package com.xing.gfox.rxHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Hlhttp {
    private OkHttpClient.Builder okHttpClientBuilder;
    private volatile static Hlhttp singleton = null;
    private static HlhttpConfig hlhttpConfig;

    public Hlhttp(HlhttpConfig hlhttpConfig) {
        Hlhttp.hlhttpConfig = hlhttpConfig;
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(HlhttpConfig.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(HlhttpConfig.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.writeTimeout(HlhttpConfig.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    public static Hlhttp getInstance() {
        if (singleton == null) {
            synchronized (Hlhttp.class) {
                if (singleton == null) {
                    singleton = new Hlhttp(hlhttpConfig);
                }
            }
        }
        return singleton;
    }
}
