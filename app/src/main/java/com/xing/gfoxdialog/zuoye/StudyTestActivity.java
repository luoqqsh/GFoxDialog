package com.xing.gfoxdialog.zuoye;

import android.os.Bundle;

import com.xing.gfox.base.activity.HLBaseActivity;
import com.xing.gfox.hl_study.Glide.ActiveResource;
import com.xing.gfox.hl_study.Glide.Key;
import com.xing.gfox.hl_study.Glide.LruMemoryCache;
import com.xing.gfox.hl_study.Glide.MemoryCache;
import com.xing.gfox.hl_study.Glide.SResource;
import com.xing.gfox.hl_study.okhttp.SCall;
import com.xing.gfox.hl_study.okhttp.SCallback;
import com.xing.gfox.hl_study.okhttp.SOkHttpClient;
import com.xing.gfox.hl_study.okhttp.SRequest;
import com.xing.gfox.hl_study.okhttp.SResponse;
import com.xing.gfoxdialog.R;

public class StudyTestActivity extends HLBaseActivity {

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setTitleText("好好学习，天天向上");
        findViewById(R.id.okhttpTest).setOnClickListener(view -> okhttpTest());
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.pink;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_study;
    }

    /*********************************************************************************/
    private LruMemoryCache lruMemoryCache;
    private ActiveResource activeResource;

    public SResource GlideTest(Key key) {
        //内存缓存
        lruMemoryCache = new LruMemoryCache(10);
        lruMemoryCache.setResourceRemoveListener(resourceRemoveListener);

        //活动资源缓存
        activeResource = new ActiveResource(resourceListener);

        /**
         * 第一次 从活动资源中查找是否正在使用的图片
         */
        SResource resource = activeResource.get(key);
        if (null != resource) {
            resource.acquire();
            return resource;
        }

        /**
         * 第二步 从内存缓存中查找
         */
        resource = lruMemoryCache.get(key);
        if (null != resource) {
            //1. 为什么内存缓存移除？
            //因为lru可能移除此图片，也有可能recycle掉此图片
            //如果不移除，则下次使用此图片从活动资源中能找到，但是这个图片可能被recycle掉了
            resource.acquire();
            activeResource.active(key, resource);
            lruMemoryCache.removeBitmap(key);
            return resource;
        }
        return null;
    }

    SResource.ResourceListener resourceListener = new SResource.ResourceListener() {
        @Override
        public void onResourceReleased(Key key, SResource resource) {

        }
    };

    MemoryCache.ResourceRemoveListener resourceRemoveListener = new MemoryCache.ResourceRemoveListener() {
        @Override
        public void onResourceRemoved(SResource resource) {

        }
    };

    /*********************************************************************************/
    private void okhttpTest() {
        SOkHttpClient dnHttpClient = new SOkHttpClient();
        String url = "http://www.baidu.com";
        SRequest request = new SRequest.Builder()
                .url(url)
                .get()  //默认为GET请求，可以不写
                .build();
        SCall call = dnHttpClient.newCall(request);
        call.enqueue(new SCallback() {
            @Override
            public void onFailure(SCall call, Throwable throwable) {

            }

            @Override
            public void onResponse(SCall call, SResponse response) {

            }
        });
    }
    /*********************************************************************************/
}
