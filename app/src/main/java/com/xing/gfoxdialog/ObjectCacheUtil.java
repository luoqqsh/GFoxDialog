package com.xing.gfoxdialog;

import java.util.LinkedList;

/**
 * 用来处理View复用的保存和提取
 * Created by ChenJiaLiang on 2018/4/11.
 * Email:576507648@qq.com
 */
public class ObjectCacheUtil<T> {

    private LinkedList<T> cacheList;

    public ObjectCacheUtil() {
        cacheList = new LinkedList<>();
    }

    public T getCacheObject() {
        if (cacheList != null && cacheList.size() != 0) {
            return cacheList.removeFirst();
        }
        return null;
    }

    public void addCacheObject(T t) {
        if (cacheList != null) cacheList.add(t);
    }

    public void clear() {
        if (cacheList != null) cacheList.clear();
        cacheList = null;
    }

}
