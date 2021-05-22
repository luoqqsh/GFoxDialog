package com.xing.gfox_glide.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;


/**
 * @author SummerSoft.Lgr
 */
public abstract class BitmapTarget extends SimpleTarget<Bitmap> {

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        Log.e("BitmapTarget", "onLoadStarted");
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        Log.e("BitmapTarget", "onLoadCleared");
    }

    @Override
    public void removeCallback(@NonNull SizeReadyCallback cb) {
        Log.e("BitmapTarget", "removeCallback");
    }

    @Override
    public void setRequest(@Nullable Request request) {
        Log.e("BitmapTarget", "setRequest");
    }

    @Nullable
    @Override
    public Request getRequest() {
        Log.e("BitmapTarget", "getRequest");
        return null;
    }

    @Override
    public void onStart() {
        Log.e("BitmapTarget", "onStart");
    }

    @Override
    public void onStop() {
        Log.e("BitmapTarget", "onStop");
    }

    @Override
    public void onDestroy() {
        Log.e("BitmapTarget", "onDestroy");
    }
}
