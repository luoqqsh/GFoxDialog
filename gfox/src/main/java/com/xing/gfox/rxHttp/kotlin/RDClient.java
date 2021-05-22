package com.xing.gfox.rxHttp.kotlin;


import android.util.Log;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.rxHttp.LoggingInterceptor;

import java.io.IOException;
import java.net.Proxy;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/4/5 10:30
 * <p/>
 * Description: 网络请求client
 */
public class RDClient {
    // 网络请求超时时间值(s)
    private static final long DEFAULT_TIMEOUT = 15;
    // 请求地址
    private static final String BASE_URL = "https://api.mgshop.shop/";
    //private static final String BASE_URL = "http://192.168.4.80:8091/";
    // retrofit实例
    private Retrofit retrofit;

    /**
     * 私有化构造方法
     */
    private RDClient() {
        // 创建一个OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //禁止抓包工具,打测试服务器时，不拦截代理
        //BuildConfig.has_caught 是否可抓包
        builder.proxy(Proxy.NO_PROXY);
        // 设置网络请求超时时间
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //        //https相关设置，以下几种方案根据需要自己设置
//        //方法一：信任所有证书,不安全有风险
        HttpSSLUtils.SSLParams sslParams1 = HttpSSLUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
//        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());
        LoggingInterceptor interceptor = new LoggingInterceptor();
        interceptor.setLevel(LoggingInterceptor.Level.ALL);
        builder.addNetworkInterceptor(interceptor);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("deviceId", "bb1862708c28fe26")
                        .addHeader("isSensors", "true")
                        .addHeader("packageName", "com.mg.bbz")
                        .addHeader("appType", "bbz")
                        .addHeader("appVersion", "1.9.0")
                        .addHeader("isLogin", "false")
                        .addHeader("userId", "42740")
                        .build();
                return chain.proceed(request);
            }
        }).build();
        // 添加签名参数
//        builder.addInterceptor(new BasicParamsInject().getInterceptor());
//        builder.addInterceptor(getInterceptor());
        // 失败后尝试重新请求
        builder.retryOnConnectionFailure(true);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(builder.build())
//                .addConverterFactory(RDConverterFactory.create())
//                .build();
        Log.e("BASE_URL>>>>>>>>>>", BASE_URL);
    }

    /**
     * 设置公共请求头和参数
     * <p>
     * 可变参数 token、isLogin  不能放着这里
     *
     * @return 拦截器
     */
//    private CommonParamsInterceptor getInterceptor() {
//        return new CommonParamsInterceptor.Builder()
////                .addHeaderParam("token", UserInfoManager.INSTANCE.getToken() + "")
//                .addHeaderParam("appType", BuildConfig.FLAVOR)
////                .addHeaderParam("userId", UserInfoManager.INSTANCE.getUserId())
//                .addHeaderParam("deviceId", DeviceInfoUtils.getUniqueDeviceId())
//                .addHeaderParam("oldDeviceId", NetworkUtils.isConnected() ? DeviceUtils.getUniqueDeviceId() : "")
//                .addHeaderParam("appVersion", PackageUtils.getVersion(MyApplication.getInstance()))
//                .addHeaderParam("deviceType", "Android")
//                .addHeaderParam("channelId", AndroidUtils.getMarketId(MyApplication.getInstance()))
//                .addHeaderParam("osVersion", Build.VERSION.RELEASE)
//                .addHeaderParam("deviceModel", DeviceUtils.getModel())
////                .addHeaderParam("isLogin", UserInfoManager.INSTANCE.isTouristMode() ? "false" : UserInfoManager.INSTANCE.isLogin() ? "true" : "false")
//                .addHeaderParam("isSensors", String.valueOf(MyApplication.isSensors()))
//                .addHeaderParam("packageName", AppUtils.getAppPackageName())
////                .addHeaderParam("Connection", "keep-alive")
//                .build();
//    }

    public Retrofit getRetrofit() {
        return retrofit;
    }


    /**
     * 调用单例对象
     */
    public static RDClient getInstance() {
        return RDClientInstance.instance;
    }

    /**
     * 创建单例对象
     */
    public static class RDClientInstance {
        static RDClient instance = new RDClient();
    }

    ///////////////////////////////////////////////////////////////////////////
    // service
    ///////////////////////////////////////////////////////////////////////////
    private static TreeMap<String, Object> serviceMap;

    private static TreeMap<String, Object> getServiceMap() {
        if (serviceMap == null) {
            serviceMap = new TreeMap<>();
        }
        return serviceMap;
    }

    /**
     * @return 指定service实例
     */
    public static <T> T getService(Class<T> clazz) {
        if (getServiceMap().containsKey(clazz.getSimpleName())) {
            return (T) getServiceMap().get(clazz.getSimpleName());
        }

        ViseLog.w("RDClient", "need to create a new " + clazz.getSimpleName());
        T service = RDClient.getInstance().retrofit.create(clazz);
        getServiceMap().put(clazz.getSimpleName(), service);
        return service;
    }
}
