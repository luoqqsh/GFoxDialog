package com.xing.gfox_glide.target;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;


/**
 * @author SummerSoft.Lgr
 */
public abstract class DrawableTarget extends SimpleTarget<Drawable> {
    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {

    }


    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {

    }

    @Override
    public void removeCallback(@NonNull SizeReadyCallback cb) {

    }

    @Override
    public void setRequest(@Nullable Request request) {

    }

    @Nullable
    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
