package com.xing.gfox.rxHttp.li;

import androidx.annotation.NonNull;

import com.xing.gfox.rxHttp.interceptor.LoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitHelper {
    protected static final int TIMEOUT_READ = 360;
    protected static final int TIMEOUT_CONNECTION = 60;
    private Retrofit retrofit;
    private String baseApiUrl = "http://172.1.1.1:8080/";

    private static RetrofitHelper instance;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            synchronized (RetrofitHelper.class) {
                instance = new RetrofitHelper();
            }
        }
        return instance;
    }

    protected RetrofitHelper() {
    }


    public <T> T create(String baseURL, Class<T> service) {
        if (retrofit == null || !baseApiUrl.equals(baseURL)) {
            baseApiUrl = baseURL;
            retrofit = initRetrofitWithLiveData(baseURL, initOkHttp());
        }
        return retrofit.create(service);
    }

    /**
     * 初始化Retrofit
     */
    @NonNull
    protected Retrofit initRetrofitWithLiveData(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseURL)
                .build();
    }

    /**
     * 初始化okhttp
     */
    @NonNull
    protected OkHttpClient initOkHttp() {
        return new OkHttpClient().newBuilder()
                .readTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(TIMEOUT_READ, TimeUnit.SECONDS)//设置请求超时时间
                .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)//设置写入超时时间
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .addInterceptor(new LoggingInterceptor())//添加打印拦截器
                .build();
    }
}
