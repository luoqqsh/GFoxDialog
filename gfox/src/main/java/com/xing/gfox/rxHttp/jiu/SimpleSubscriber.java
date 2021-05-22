package com.xing.gfox.rxHttp.jiu;

import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.rxHttp.bean.HttpResult;

import java.lang.reflect.Type;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
//public abstract class SimpleSubscriber<T> extends Subscriber<T> implements ICallback<T> {
public abstract class SimpleSubscriber<T> implements Observer<T>, OnHttpCallback<T> {

    private boolean isShouldShowDialog;
    private FragmentActivity mActivity;
    private String cacheKey;
    private String staticKey;//友盟统计key3，当请求失败时统计，当CacheKey有值时，用CacheKey

    //用来Http请求失败，重新发起请求用的
    private Map<String, Object> paramsMap;
    private HttpMethodType httpMethodType;
    private String path;
    private Type type;

    public SimpleSubscriber() {
        this.isShouldShowDialog = false;
    }

    public SimpleSubscriber(FragmentActivity mActivity, boolean isShouldShowDialog) {
        this.mActivity = mActivity;
        this.isShouldShowDialog = isShouldShowDialog;
    }

    String getCacheKey() {
        return cacheKey;
    }

    void setCacheKey(String cacheKey, boolean isSaveCache) {
        if (isSaveCache) {
            this.cacheKey = HttpConfig.apiVersion + cacheKey;
        }
        //统计Key是都要的，缓存Key不一定有，交互型的接口不需要缓存
        setStaticKey(cacheKey);
    }

    private void setStaticKey(String staticKey) {
        this.staticKey = staticKey;
    }

    String getStaticKey() {
        return staticKey;
    }

    @Override
    public void onSubscribe(Disposable disposable) {
        if (isShouldShowDialog) showDialog();
    }

//    @Override
//    public void onStart() {
//        if (isShouldShowDialog) showDialog();
//    }

    private void showDialog() {
//        if (mActivity != null && !mActivity.isFinishing()) {
//            DialogManager.showDialog(mActivity);
//        }
    }

    private void hideDialog() {
//        if (mActivity != null && !mActivity.isFinishing()) {
//            DialogManager.hideDialog(mActivity);
//            mActivity = null;
//        }
    }

    @Override
    public void onComplete() {
        if (isShouldShowDialog) hideDialog();
    }

//    @Override
//    public void onCompleted() {
//        if (isShouldShowDialog) hideDialog();
//    }

    @Override
    public void onError(Throwable e) {
        ViseLog.showLog("onError:" + e);
        if (isShouldShowDialog) hideDialog();
        if (mActivity != null && mActivity.isFinishing()) {
            mActivity = null;
            return;
        }
        String error = "服务器异常，请稍候再试";
        if (e != null) {
            String message = e.getMessage();
            if (message != null && message.contains("Canceled")) {
                error = "操作取消";
//            } else if (e instanceof SocketTimeoutException) {
//                error = "连接超时，请检查您的网络状态";
//                if (httpMethodType != null && reCallCount < Config.maxReCallCount) {
//                    reCallCount++;
//                    HttpMethods.instance().doHttp(httpMethodType, path, paramsMap, this, type);
//                    return;
//                }
//            } else if (e instanceof ConnectException) {
//                error = "网络中断，请检查您的网络状态";
            }
        }

        onFail(error);
        ViseLog.showLog("httpResult error:" + error);
    }

    private int reCallCount;

    @Override
    public void onNext(T t) {
        if (isShouldShowDialog) hideDialog();
        if (mActivity != null && mActivity.isFinishing()) {
            mActivity = null;
            return;
        }
        if (t instanceof HttpResult) {
            HttpResult data = (HttpResult) t;
            if (data.getCode() == 200) {
                onSuccess(t);
                reCallCount = 0;
            } else if (data.getCode() == 401) {

            } else {
                if (!TextUtils.isEmpty(data.getMsg())) {
                    onFail(data.getMsg());
                }
            }
            return;
        }
//        onFail(HttpConfig.httpDefaultError);
        onSuccess(t);
        reCallCount = 0;
    }

    void setParamsMap(HttpMethodType httpMethodType, String path, Map<String, Object> params, Type type) {
        this.httpMethodType = httpMethodType;
        this.path = path;
        this.paramsMap = params;
        this.type = type;
    }
}