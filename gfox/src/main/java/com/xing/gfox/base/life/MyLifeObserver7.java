package com.xing.gfox.base.life;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 生命周期感知
 */
public class MyLifeObserver7 implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStop() {
//        ViseLog.d("onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    private void onAny() {
//        ViseLog.d("onStop");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate() {
//        ViseLog.d("onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestory() {
//        ViseLog.d("onDestory");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
//        ViseLog.d("onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
//        ViseLog.d("onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onStart() {
//        ViseLog.d("onStart");
    }
}
