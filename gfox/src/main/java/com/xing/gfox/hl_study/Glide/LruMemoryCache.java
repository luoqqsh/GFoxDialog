package com.xing.gfox.hl_study.Glide;

import android.os.Build;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 内存缓存
 * 默认使用LRU(缓存淘汰算法/最近最少使用算法)
 */

public class LruMemoryCache extends LruCache<Key, SResource> implements MemoryCache {
    private ResourceRemoveListener listener;

    //指定最大内存
    public LruMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(@NonNull Key key, @NonNull SResource value) {
        //获取bitmap资源大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //当在4.4以上手机复用的使用 需要通过此函数获得占用内存
            return value.getBitmap().getAllocationByteCount();
        }
        return value.getBitmap().getByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, @NonNull Key key, @NonNull SResource oldValue, @Nullable SResource newValue) {
        if (null != listener && null != oldValue) {
            this.listener.onResourceRemoved(oldValue);
        }
    }

    @Override
    public void setResourceRemoveListener(ResourceRemoveListener resourceRemoveListener) {
        this.listener = resourceRemoveListener;
    }

    @Override
    public SResource putBitmap(Key key, SResource resource) {
        return null;
    }

    @Override
    public SResource removeBitmap(Key key) {
        SResource remove = remove(key);
        return remove;
    }
}
