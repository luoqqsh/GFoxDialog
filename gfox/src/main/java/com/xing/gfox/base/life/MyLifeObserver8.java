package com.xing.gfox.base.life;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.xing.gfox.log.ViseLog;


/**
 * 生命周期感知
 */
public class MyLifeObserver8 implements DefaultLifecycleObserver {
    private String TAG = MyLifeObserver8.class.getSimpleName();

    public MyLifeObserver8() {

    }
    public MyLifeObserver8(String TAG) {
        this.TAG = "MyLifeObserver8 - " + TAG;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        ViseLog.showInfo(TAG, "onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        ViseLog.showInfo(TAG, "onStart");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        ViseLog.showInfo(TAG, "onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        ViseLog.showInfo(TAG, "onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        ViseLog.showInfo(TAG, "onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        ViseLog.showInfo(TAG, "onDestroy");
    }
}
