package com.xing.gfox_glide;

/**
 * Created by ChenJiaLiang on 2018/4/12.
 * Email:576507648@qq.com
 */

public interface OnLoadImageListener<T> {
    void onSuccess(T t);

    void onFail();
}
