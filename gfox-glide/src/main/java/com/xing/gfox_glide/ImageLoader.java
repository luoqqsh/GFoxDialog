package com.xing.gfox_glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.File;

public interface ImageLoader {
    <T> void setSimpleImage(Object contextObject, T url, ImageView imageView);

    <T> void setSimpleImage(Object contextObject, T url, ImageView imageView, int defaultId);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, boolean isCenterCrop);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int width, int height);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, boolean isCenterCrop);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, int width, int height);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId,OnLoadImageListener<Drawable> listener);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, boolean isCenterCrop, OnLoadImageListener<Drawable> listener);

    <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, boolean isCenterCrop, boolean isShowAnim, OnLoadImageListener<Drawable> listener);

    <T> void setDefaultNoCacheImage(Object contextObject, T url, ImageView imageView);

    <T> void setDefaultNoCacheImage(Object contextObject, T url, ImageView imageView, int defaultId);

    <T> void setCircleImage(Object contextObject, T url, ImageView imageView);

    <T> void setCircleImage(Object contextObject, T url, ImageView imageView, int width, int height);

    <T> void setCircleImage(Object contextObject, T url, ImageView imageView, int defaultId);

    <T> void setCircleImage(Object contextObject, T url, ImageView imageView, int defaultId, int width, int height);

    <T> void setCircleNoCacheImage(Object contextObject, T url, ImageView imageView);

    <T> void setCircleNoCacheImage(Object contextObject, T url, ImageView imageView, int defaultId);

    <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner);

    <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int width, int height);

    <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId);

    <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId, int width, int height);

    <T> void setConnerNoCacheImage(Object contextObject, T url, ImageView imageView, int conner);

    <T> void setConnerNoCacheImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId);

    <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView);

    <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId);

    <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, int blurRadio, int blurSimpling);

    <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, int blurRadio, int blurSimpling, boolean isShowAnim);

    <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, boolean isShowAnim);

    <T> void setSimpleBlurNoCache(Object contextObject, T url, ImageView imageView);

    <T> void setSimpleBlurNoCache(Object contextObject, T url, ImageView imageView, int defaultId);

    /**
     * 获取图片原图 缓存文件 应运行于非主线程中
     */
    <T> File getImageFile(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Drawable getImageDrawable(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Drawable getImageDrawable(Object contextObject, T t, int width, int height);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Drawable getImageCircleDrawable(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Drawable getImageConnerDrawable(Object contextObject, T t, int conner);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Drawable getImageConnerDrawable(Object contextObject, T t, int conner, boolean isCenterCrop);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Drawable getImageDrawable(Object contextObject, T t,
                                  boolean isCenterCrop,
                                  boolean isCircle,
                                  boolean isUseCache,
                                  int conner,
                                  int blurRadio, int blurSimpling,
                                  int overrideWidth, int overrideHeight,
                                  final OnLoadImageListener<Drawable> listener);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Bitmap getImageBitmap(Object contextObject, T t);

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    <T> Bitmap getImageBitmap(Object contextObject, T t, int width, int height);
}
