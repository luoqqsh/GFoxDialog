package com.xing.gfox.hl_study.Glide;

import android.graphics.Bitmap;

public class SResource {
    private Bitmap bitmap;
    //引用计数,为0时回调，图片出入内存缓存
    private int acquired;

    private ResourceListener listener;

    private Key key;

    public interface ResourceListener {
        void onResourceReleased(Key key, SResource resource);
    }

    public void setResourceListener(Key key, ResourceListener resourceListener) {
        listener = resourceListener;
        this.key = key;
    }

    /**
     * 释放
     */
    public void recycle() {
        if (acquired > 0) {
            return;
        }
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    /**
     * 引用计数-1
     */
    public void release() {
        if (--acquired == 0) {
            listener.onResourceReleased(key, this);
        }
    }

    public void acquire() {
        if (bitmap.isRecycled()) {
            throw new IllegalStateException("Acquire a recycled resource");
        }
        ++acquired;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
