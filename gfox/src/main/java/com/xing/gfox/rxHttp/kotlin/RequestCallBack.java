package com.xing.gfox.rxHttp.kotlin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RequestCallBack<T> implements Callback<T> {

    public abstract void onSuccess(Call<T> call, Response<T> response);

    //请求成功，但数据失败
    public void onFailed(Call<T> call, Response<T> response) {
        onFailed(call, response);
//        ViseLog.d(response.body());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        onSuccess(call, response);
//        ViseLog.d(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(call, t);
//        ViseLog.d(t);
    }
}
