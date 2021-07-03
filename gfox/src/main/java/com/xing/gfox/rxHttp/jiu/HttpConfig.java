package com.xing.gfox.rxHttp.jiu;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import com.xing.gfox.rxHttp.commonApi.ObservableService;
import com.xing.gfox.util.U_gson;
import com.xing.gfox.util.U_string;

public abstract class HttpConfig extends BaseHttpMethods<ObservableService> {
    private static final String BASE_URL = "https://tool.bizhijingling.com/v1/";
    public static final String httpDefaultError = "请求失败，请稍候再试";
    public static final String apiVersion = "t_v1_";


    public HttpConfig() {
    }

    public void logoff() {

    }

    @Override
    protected String encrypt(String s) {
        //这里解密是为了调试时打印数据用
        if (U_string.isJson(s)) {
            return s;
        }
        return s;
//        return DESEncrypt.decrypt(s);
    }

    @Override
    protected void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
    }

    @Override
    protected List<Cookie> loadForRequest(HttpUrl httpUrl) {
        return new ArrayList<>();
    }

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    protected int getConnectTimeOutSecond() {
        return 10;
    }

    @Override
    protected int getReadTimeOutSecond() {
        return 10;
    }

    @Override
    protected Class<ObservableService> getServiceClass() {
        return ObservableService.class;
    }

    private boolean checkNetWork(SimpleSubscriber subscriber) {
//        if (!UIHelper.getNetWorkStatus(APP.getContext())) {
//            subscriber.onFail("网络异常，请检查您的网络");
//            return false;
//        }
        return true;
    }


    public <T> void doHttp(HttpMethodType httpMethodType,
                           String path,
                           Map<String, Object> params,
                           SimpleSubscriber<T> subscriber,
                           Type type) {
        doHttp(httpMethodType, path, params, null, null, subscriber, type);
    }

    <T> void doHttp(HttpMethodType httpMethodType,
                    String path,
                    Map<String, Object> params,
                    Map<String, RequestBody> singleFileMap,//用于单个文件上传的参数 httpMethodType==HttpPostSingleFile(UseUrl) 时不能为空
                    MultipartBody multipartBody,//用于多个文件上传的参数  httpMethodType==HttpPostMultipleFile(UseUrl)  时不能为空
                    final SimpleSubscriber<T> subscriber,
                    final Type type) {

        if (subscriber == null || type == null || httpMethodType == null) return;
//        if (!checkNetWork(subscriber)) return;
//        if (!UIHelper.getNetWorkStatus(APP.getContext())) {
//            String cacheData = HttpCacheUtil.instance().readObject(subscriber.getCacheKey());
//            if (HttpCacheUtil.isDataEnable(cacheData, type)) {
//                HttpCacheUtil.onSuccessString(cacheData, subscriber, type, true, true);
//            } else {
//                subscriber.onFail("网络异常，请检查您的网络");
//            }
//            return;
//        }
        if ((httpMethodType == HttpMethodType.HttpPostSingleFile || httpMethodType == HttpMethodType.HttpPostSingleFileUseUrl)
                && (singleFileMap == null || singleFileMap.size() == 0)) {
            throw new NullPointerException("when do post single File,singleFileMap should not be null or size == 0");
        }
        if ((httpMethodType == HttpMethodType.HttpPostMultipleFile || httpMethodType == HttpMethodType.HttpPostMultipleFileUseUrl)
                && (multipartBody == null || multipartBody.size() == 0)) {
            throw new NullPointerException("when do post multiple File,multipartBody should not be null or size == 0");
        }
        if (httpMethodType != HttpMethodType.HttpPostSingleFile
                && httpMethodType != HttpMethodType.HttpPostSingleFileUseUrl
                && httpMethodType != HttpMethodType.HttpPostMultipleFile
                && httpMethodType != HttpMethodType.HttpPostMultipleFileUseUrl) {
            //上传文件这种耗时长的，不做再次请求
            subscriber.setParamsMap(httpMethodType, path, params, type);
        }
        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
//                subscriber.onStart();
                subscriber.onSubscribe(disposable);
            }

            @Override
            public void onNext(String dataStr) {
                if (type == String.class) {
                    T data = (T) dataStr;
                    subscriber.onNext(data);
                } else {
                    T data = U_gson.instance().fromJson(dataStr, type);
                    subscriber.onNext(data);
                }
//                HttpCacheUtil.onSuccessString(dataStr, subscriber, type, false, true);
            }

            @Override
            public void onError(Throwable e) {
//                String cacheData = HttpCacheUtil.instance().readObject(subscriber.getCacheKey());
//                if (HttpCacheUtil.isDataEnable(cacheData, type)) {
//                    HttpCacheUtil.onSuccessString(cacheData, subscriber, type, true, true);
//                } else {
                subscriber.onError(e);
//                }
            }

            @Override
            public void onComplete() {
                subscriber.onComplete();
            }
        };
        Observable<String> objectObservable = null;
        if (httpMethodType == HttpMethodType.HttpPostSingleFile) {
            if (params == null) {
                objectObservable = httpService.httpPostSingleFile(path, singleFileMap);
            } else {
                objectObservable = httpService.httpPostSingleFile(path, params, singleFileMap);
            }
        } else if (httpMethodType == HttpMethodType.HttpPostSingleFileUseUrl) {
            if (params == null) {
                objectObservable = httpService.httpPostSingleFileUseUrl(path, singleFileMap);
            } else {
                objectObservable = httpService.httpPostSingleFileUseUrl(path, params, singleFileMap);
            }
        } else if (httpMethodType == HttpMethodType.HttpPostMultipleFile) {
            if (params == null) {
                objectObservable = httpService.httpPostMultipleFile(path, multipartBody);
            } else {
                objectObservable = httpService.httpPostMultipleFile(path, params, multipartBody);
            }
        } else if (httpMethodType == HttpMethodType.HttpPostMultipleFileUseUrl) {
            if (params == null) {
                objectObservable = httpService.httpPostMultipleFileUseUrl(path, multipartBody);
            } else {
                objectObservable = httpService.httpPostMultipleFileUseUrl(path, params, multipartBody);
            }
        } else if (httpMethodType == HttpMethodType.HttpGet) {
            if (params == null) {
                objectObservable = httpService.httpGet(path);
            } else {
                objectObservable = httpService.httpGet(path, params);
            }
        } else if (httpMethodType == HttpMethodType.HttpPost) {
            if (params == null) {
                objectObservable = httpService.httpPost(path);
            } else {
                objectObservable = httpService.httpPost(path, params);
            }
        } else if (httpMethodType == HttpMethodType.HttpPostForm) {
            if (params == null) {
                objectObservable = httpService.httpPostForm(path);
            } else {
                objectObservable = httpService.httpPostForm(path, params);
            }
        } else if (httpMethodType == HttpMethodType.HttpGetUseUrl) {
            if (params == null) {
                objectObservable = httpService.httpGetUseUrl(path);
            } else {
                objectObservable = httpService.httpGetUseUrl(path, params);
            }
        } else if (httpMethodType == HttpMethodType.HttpPostUseUrl) {
            if (params == null) {
                objectObservable = httpService.httpPostUseUrl(path);
            } else {
                objectObservable = httpService.httpPostUseUrl(path, params);
            }
        } else if (httpMethodType == HttpMethodType.HttpPostFormUseUrl) {
            if (params == null) {
                objectObservable = httpService.httpPostFormUseUrl(path);
            } else {
                objectObservable = httpService.httpPostFormUseUrl(path, params);
            }
        }
        if (objectObservable == null) return;
        objectObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
//                .subscribe(baseSubscriber);
    }

}
