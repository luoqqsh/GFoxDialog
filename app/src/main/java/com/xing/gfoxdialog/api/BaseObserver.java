package com.xing.gfoxdialog.api;


import com.xing.gfox.rxHttp.li.bean.ApiResponse;
import com.xing.gfox.rxHttp.li.livedata.BaseObserverCallBack;
import com.xing.gfox.rxHttp.li.livedata.IBaseObserver;

public class BaseObserver<T> implements IBaseObserver<T> {

    private BaseObserverCallBack<T> baseObserverCallBack;

    public BaseObserver(BaseObserverCallBack<T> baseObserverCallBack) {
        this.baseObserverCallBack = baseObserverCallBack;
    }

    @Override
    public void onChanged(T t) {
        if (t instanceof ApiResponse) {
            ApiResponse apiResponse = (ApiResponse) t;
            if (apiResponse.isSuccess()) {
                baseObserverCallBack.onSuccess(t);
            } else {
                baseObserverCallBack.onFail(apiResponse.getErrorMsg());

                if (baseObserverCallBack.showErrorMsg()) {
//                    Toast.makeText(app.getInstance().getApp(), apiResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                    PopUtil.show(apiResponse.getErrorMsg());
                }
            }
        } else {
            baseObserverCallBack.onSuccess(t);
//            baseObserverCallBack.onFail("!");
        }
        baseObserverCallBack.onFinish();
    }
}
