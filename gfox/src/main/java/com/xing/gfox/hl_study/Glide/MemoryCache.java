package com.xing.gfox.hl_study.Glide;


public interface MemoryCache {

    interface ResourceRemoveListener{
        void onResourceRemoved(SResource resource);
    }

    void setResourceRemoveListener(ResourceRemoveListener resourceRemoveListener);

    SResource putBitmap(Key key, SResource resource);

    SResource removeBitmap(Key key);
}
