package com.xing.gfox.rxHttp.test;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import com.xing.gfox.log.ViseLog;

public class retrofit {
    public static void testRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://lenovodns.com/") //设置网络请求的Url地址,必须以/结尾
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//要返回rxjava的Observable数据，需要加上这个，否则返回Call数据
//                .addCallAdapterFactory(CoroutineCallAdapterFactory.create())//返回协程处理
                .addConverterFactory(ScalarsConverterFactory.create())//有这个才能解析成String等基本数据类型
//                .addConverterFactory(GsonConverterFactory.create())//有这个才能进行解析Model数据类型
//                .client(new OkHttpClient())
                .build();
        ApiFace request = retrofit.create(ApiFace.class);
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
        request.getTvList3()//获取Observable对象
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String value) {
                        ViseLog.d(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ViseLog.d(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
