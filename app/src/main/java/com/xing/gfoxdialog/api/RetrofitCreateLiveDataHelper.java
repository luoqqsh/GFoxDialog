package com.xing.gfoxdialog.api;


import androidx.annotation.NonNull;

import com.xing.gfox.rxHttp.LoggingInterceptor;
import com.xing.gfox.rxHttp.li.RetrofitHelper;
import com.xing.gfox.rxHttp.li.livedata.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetrofitCreateLiveDataHelper extends RetrofitHelper {
    private static RetrofitCreateLiveDataHelper instance;

    public static RetrofitCreateLiveDataHelper getInstance() {
        if (instance == null) {
            synchronized (RetrofitCreateLiveDataHelper.class) {
                instance = new RetrofitCreateLiveDataHelper();
            }
        }
        return instance;
    }

    /**
     * 初始化Retrofit
     */
    @NonNull
    @Override
    protected Retrofit initRetrofitWithLiveData(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseURL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
//                .addConverterFactory(new GsonConverterFactory.create())
                .build();
    }

    /**
     * 初始化okhttp
     */
    @NonNull
    @Override
    protected OkHttpClient initOkHttp() {
        return new OkHttpClient().newBuilder()
                .readTimeout(APIConstant.TIMEOUT_CONNECTION, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(APIConstant.TIMEOUT_READ, TimeUnit.SECONDS)//设置请求超时时间
                .writeTimeout(APIConstant.TIMEOUT_READ, TimeUnit.SECONDS)//设置写入超时时间
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .addInterceptor(new LoggingInterceptor())//添加打印拦截器
                .build();
    }
}
