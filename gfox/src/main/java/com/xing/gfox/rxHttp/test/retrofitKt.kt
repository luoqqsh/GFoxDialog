package com.xing.gfox.rxHttp.test

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.xing.gfox.log.ViseLog
import com.xing.gfox.rxHttp.interceptor.LoggingInterceptor

object retrofitKt {
    suspend fun testRetrofit() {
        val okHttpClient = OkHttpClient().newBuilder().addNetworkInterceptor(LoggingInterceptor()).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://lenovodns.com/") //设置网络请求的Url地址,必须以/结尾
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //要返回rxjava的Observable数据，需要加上这个，否则返回Call数据
//                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())//返回协程处理
                .addConverterFactory(ScalarsConverterFactory.create()) //有这个才能解析成String等基本数据类型
//                .addConverterFactory(GsonConverterFactory.create())//有这个才能进行解析Model数据类型
                .client(okHttpClient)
                .build()
        val request = retrofit.create(ApiFaceKt::class.java)
        //retrofit2请求方式
//        retrofit2.Call<String> call = request.getTvList2();
//        call.enqueue(new retrofit2.Callback<String>() {
//            @Override
//            public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {
//                ViseLog.d(response.body());
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<String> call, Throwable t) {
//                ViseLog.d(t);
//            }
//        });
        //retrofit2+rxjava请求方式
//        request.tvList3 //获取Observable对象
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribe(object : Observer<String?> {
//                    private var mDisposable: Disposable? = null
//                    override fun onSubscribe(d: Disposable) {
//                        mDisposable = d
//                    }
//
//                    override fun onNext(value: String) {
//                        ViseLog.d(value)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        ViseLog.d(e)
//                    }
//
//                    override fun onComplete() {}
//                })
        //retrofit2+协程请求方式
        try {
            val response = request.tvList5("aaa")
            ViseLog.d(response)
        } catch (e: Exception) {
            ViseLog.d(e)
        }
    }
}