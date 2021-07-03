package com.xing.gfox.rxHttp.li.livedata;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<T>> {

    private Type mResponseType;

    LiveDataCallAdapter(Type mResponseType) {
        this.mResponseType = mResponseType;
    }

    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public LiveData<T> adapt(Call<T> call) {
        return new MyLiveData<>(call);
    }

    private static class MyLiveData<T> extends LiveData<T> {
        private AtomicBoolean stared = new AtomicBoolean(false);
        private final Call<T> call;

        MyLiveData(Call<T> call) {
            this.call = call;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //确保执行一次
            boolean b = stared.compareAndSet(false, true);
            if (b) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Call<T> call, Response<T> response) {
                        T body1 = response.body();
                        if (body1 != null) {
                            postValue(body1);
                        } else {
                            postValue((T) response.raw());
                        }
                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable t) {
                        postValue((T)t);
                    }
                });
            }
        }
    }
}
