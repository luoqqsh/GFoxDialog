package com.xing.gfox_glide;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


/**
 * Created by ChenJiaLiang on 2018/4/11.
 * Email:576507648@qq.com
 */
public class GlideRequestM {
    private boolean isShouldReturn;
    private Object contextObject;
    private ImageView imageView;
    private GlideRequests requestManager;
    private Context context;

    GlideRequestM(Object contextObject) {
        this.contextObject = contextObject;
    }

    GlideRequestM(Object contextObject, ImageView imageView) {
        this.contextObject = contextObject;
        this.imageView = imageView;
    }

    boolean isShouldReturn() {
        return isShouldReturn;
    }

    GlideRequests getRequestManager() {
        return requestManager;
    }

    public Context getContext() {
        return context;
    }

    GlideRequestM init() {
        if (contextObject != null && contextObject instanceof Activity) {
            Activity activity = (Activity) contextObject;
            if (activity.isFinishing()) {
                isShouldReturn = true;
                return this;
            }
            requestManager = GlideApp.with(activity);
            context = activity;
        } else if (contextObject != null && contextObject instanceof Fragment) {
            Fragment fragment = (Fragment) contextObject;
            requestManager = GlideApp.with(fragment);
            context = fragment.getContext();
        } else if (contextObject != null && contextObject instanceof Context) {
            context = ((Context) contextObject).getApplicationContext();
            requestManager = GlideApp.with(context);
        } else if (contextObject != null && contextObject instanceof View) {
            View view = (View) contextObject;
            context = view.getContext();
            requestManager = GlideApp.with(view);
        } else if (imageView != null) {
            requestManager = GlideApp.with(imageView);
            context = imageView.getContext();
        } else {
            isShouldReturn = true;
            return this;
        }
        isShouldReturn = false;
        return this;
    }
}
