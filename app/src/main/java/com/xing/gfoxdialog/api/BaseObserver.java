package com.xing.gfoxdialog.api;


import com.xing.gfox.rxHttp.li.bean.ApiResponse;
import com.xing.gfox.rxHttp.li.livedata.BaseObserverCallBack;
import com.xing.gfox.rxHttp.li.livedata.IBaseObserver;

import java.net.ConnectException;
import java.net.UnknownHostException;

import okhttp3.Response;

public class BaseObserver<T> implements IBaseObserver<T> {
    private final BaseObserverCallBack<T> baseObserverCallBack;

    public BaseObserver(BaseObserverCallBack<T> baseObserverCallBack) {
        this.baseObserverCallBack = baseObserverCallBack;
    }

    @Override
    public void onChanged(T t) {
        if (t instanceof BaseApiResponse) {
            BaseApiResponse apiResponse = (BaseApiResponse) t;
            if (apiResponse.isSuccess()) {
                if (apiResponse.isSuccess()) {
                    baseObserverCallBack.onSuccess(t);
                } else {
                    baseObserverCallBack.onFail(apiResponse.getErrorMsg());
                }
            }
        } else if (t instanceof Response) {
            Response response = (Response) t;
            String text;
            switch (response.code()) {
                case 403:
                    text = "403:服务器拒绝";
                    break;
                case 404:
                    text = "404:服务器连接失败";
                    break;
                case 500:
                    text = "500:服务器错误";
                    break;
                case 504:
                    text = "504:缓存获取失败";
                    break;
                default:
                    text = String.valueOf(response.code());
                    break;
            }
            baseObserverCallBack.onFail(text);
        } else if (t instanceof Throwable) {
            ((Throwable) t).printStackTrace();
            if (t instanceof ConnectException) {
                baseObserverCallBack.onFail("异常:服务器连接失败");
            } else if (t instanceof UnknownHostException) {
                baseObserverCallBack.onFail("异常:域名解析失败");
            } else {
                baseObserverCallBack.onFail(((Throwable) t).getMessage());
            }
            baseObserverCallBack.onThrowable((Throwable) t);
        } else {
            baseObserverCallBack.onSuccess(String.valueOf(t));
        }
        baseObserverCallBack.onFinish();
    }
}
