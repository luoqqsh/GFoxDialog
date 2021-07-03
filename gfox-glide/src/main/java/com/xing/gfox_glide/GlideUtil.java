package com.xing.gfox_glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.xing.gfox_glide.transform.GlideBlurTransform;
import com.xing.gfox_glide.transform.GlideCircleTransform;
import com.xing.gfox_glide.transform.GlideConnerTransform;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



/**
 * @author : Created by ChenJiaLiang on 2017/1/12.
 * Email : 576507648@qq.com
 */
public class GlideUtil implements ImageLoader {

    private static GlideUtil glideUtil;
    private int normalDefaultId;
    private int circleDefaultId;
    private int connerDefaultId;

    private GlideUtil() {
    }

    public static GlideUtil instance() {
        if (glideUtil == null) {
            glideUtil = new GlideUtil();
        }
        return glideUtil;
    }

    public void setNormalDefaultId(int normalDefaultId) {
        this.normalDefaultId = normalDefaultId;
    }

    public void setCircleDefaultId(int circleDefaultId) {
        this.circleDefaultId = circleDefaultId;
    }

    public void setConnerDefaultId(int connerDefaultId) {
        this.connerDefaultId = connerDefaultId;
    }

    public <T> void setSimpleImage(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, 0, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleImage(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, boolean isCenterCrop) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCrop, isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int width, int height) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, boolean isCenterCrop) {
        setImage(contextObject, url, imageView, defaultId, isCenterCrop, isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, int width, int height) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, OnLoadImageListener<Drawable> listener) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), listener);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, boolean isCenterCrop, OnLoadImageListener<Drawable> listener) {
        setImage(contextObject, url, imageView, defaultId, isCenterCrop, isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), listener);
    }

    public <T> void setDefaultImage(Object contextObject, T url, ImageView imageView, int defaultId, boolean isCenterCrop, boolean isShowAnim, OnLoadImageListener<Drawable> listener) {
        setImage(contextObject, url, imageView, defaultId, isCenterCrop, isShowAnim, isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), listener);
    }

    public <T> void setDefaultNoCacheImage(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), false, getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setDefaultNoCacheImage(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), false, getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setCircleImage(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, circleDefaultId, isCenterCropDefault(), isShowAnimDefault(), true, isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setCircleImage(Object contextObject, T url, ImageView imageView, int width, int height) {
        setImage(contextObject, url, imageView, circleDefaultId, isCenterCropDefault(), isShowAnimDefault(), true, isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setCircleImage(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), true, isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setCircleImage(Object contextObject, T url, ImageView imageView, int defaultId, int width, int height) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), true, isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setCircleNoCacheImage(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, circleDefaultId, isCenterCropDefault(), isShowAnimDefault(), true, false, getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setCircleNoCacheImage(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), true, false, getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner) {
        setImage(contextObject, url, imageView, connerDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int width, int height) {
        setImage(contextObject, url, imageView, connerDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId, boolean isShowAnim) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnim, isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId, int width, int height) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setConnerImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId, int width, int height, boolean isShowAnim) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnim, isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    public <T> void setConnerNoCacheImage(Object contextObject, T url, ImageView imageView, int conner) {
        setImage(contextObject, url, imageView, connerDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), false, conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setConnerNoCacheImage(Object contextObject, T url, ImageView imageView, int conner, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), false, conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, boolean isCenterCrop) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCrop, isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, int blurRadio, int blurSimpling) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), blurRadio, blurSimpling, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, int blurRadio, int blurSimpling, boolean isShowAnim) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnim, isCircleDefault(), isUseCacheDefault(), getConnerDefault(), blurRadio, blurSimpling, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, boolean isShowAnim) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnim, isCircleDefault(), isUseCacheDefault(), getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlur(Object contextObject, T url, ImageView imageView, int defaultId, boolean isShowAnim, boolean isCenterCrop) {
        setImage(contextObject, url, imageView, defaultId, isCenterCrop, isShowAnim, isCircleDefault(), isUseCacheDefault(), getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlurNoCache(Object contextObject, T url, ImageView imageView) {
        setImage(contextObject, url, imageView, normalDefaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), false, getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    public <T> void setSimpleBlurNoCache(Object contextObject, T url, ImageView imageView, int defaultId) {
        setImage(contextObject, url, imageView, defaultId, isCenterCropDefault(), isShowAnimDefault(), isCircleDefault(), false, getConnerDefault(), 15, 1, getWidthDefault(), getHeightDefault(), null);
    }

    private int getHeightDefault() {
        return -1;
    }

    private int getWidthDefault() {
        return -1;
    }

    private boolean isCenterCropDefault() {
        return true;
    }

    private boolean isShowAnimDefault() {
        return true;
    }

    private boolean isCircleDefault() {
        return false;
    }

    private boolean isUseCacheDefault() {
        return true;
    }

    private int getConnerDefault() {
        return 0;
    }

    private int getRadioDefault() {
        return 25;
    }

    private int getSimplingDefault() {
        return 1;
    }

    /**
     * 获取图片原图 缓存文件 应运行于非主线程中
     */
    @Deprecated
    public <T> File getImageFile(Object contextObject, T t) {
        try {
            GlideRequestM glideRequestM = new GlideRequestM(contextObject);
            Bitmap bitmap = getImageBitmap(contextObject, t);
            if (bitmap == null) return null;
            File file = new File(glideRequestM.getContext().getCacheDir(), "glide_get_file_ls.jpg");
            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                if (!mkdirs && !file.getParentFile().exists()) {
                    return null;
                }
            }
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile && !file.exists()) {
                    return null;
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            //这个版本4.9里有问题，没有设定大小的话，会加载失败，默认的原图大小不行
//            GlideRequestM glideRequestM = new GlideRequestM(contextObject).init();
//            if (glideRequestM.isShouldReturn()) {
//                return null;
//            }
//            return glideRequestM.getRequestManager()
//                    .asFile()
//                    .load(t).submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Drawable getImageDrawable(Object contextObject, T t) {
        return getImageDrawable(contextObject, t, isCenterCropDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Drawable getImageDrawable(Object contextObject, T t, int width, int height) {
        return getImageDrawable(contextObject, t, isCenterCropDefault(), isCircleDefault(), isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), width, height, null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Drawable getImageCircleDrawable(Object contextObject, T t) {
        return getImageDrawable(contextObject, t, isCenterCropDefault(), true, isUseCacheDefault(), getConnerDefault(), getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Drawable getImageConnerDrawable(Object contextObject, T t, int conner) {
        return getImageDrawable(contextObject, t, isCenterCropDefault(), isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Drawable getImageConnerDrawable(Object contextObject, T t, int conner, boolean isCenterCrop) {
        return getImageDrawable(contextObject, t, isCenterCropDefault(), isCircleDefault(), isUseCacheDefault(), conner, getRadioDefault(), getSimplingDefault(), getWidthDefault(), getHeightDefault(), null);
    }

    /**
     * Bitmap to Drawable
     */
    public Drawable bitmap2Drawable(Bitmap bitmap, Context mContext) {
        if (bitmap == null) return null;
        return new BitmapDrawable(mContext.getResources(), bitmap);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Drawable getImageDrawable(Object contextObject, T t,
                                         boolean isCenterCrop,
                                         boolean isCircle,
                                         boolean isUseCache,
                                         int conner,
                                         int blurRadio, int blurSimpling,
                                         int width, int height,
                                         final OnLoadImageListener<Drawable> listener) {
        try {
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject).init();
            if (glideRequestM.isShouldReturn()) {
                if (listener != null) listener.onFail();
                return null;
            }
            GlideRequests glideRequest = glideRequestM.getRequestManager();

            GlideRequest<Drawable> load = glideRequest.asDrawable().load(t);
            if (!isUseCache) {
                load = load.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);//跳过内存缓存
            }
            List<BitmapTransformation> transList = new ArrayList<>();
            if (isCenterCrop) {
                transList.add(new CenterCrop());
            }
            if (isCircle) {
                transList.add(new GlideCircleTransform());
            } else if (conner > 0) {
                transList.add(new GlideConnerTransform(conner));
            }
            if (blurRadio < 25 || blurSimpling > 1) {
                transList.add(new GlideBlurTransform(glideRequestM.getContext(), blurRadio, blurSimpling));
            }
            if (transList.size() > 0) {
                BitmapTransformation[] transformations = new BitmapTransformation[transList.size()];
                load = load.transform(transList.toArray(transformations));
            }
            transList.clear();
            transList = null;

            if (listener != null) {
                load = load.addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        listener.onFail();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        listener.onSuccess(resource);
                        return false;
                    }
                });
            }
            if (width > 0 && height > 0) {
                return load.submit(width, height).get();
            } else {
                return load.submit().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) listener.onFail();
        }
        return null;
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Bitmap getImageBitmap(Object contextObject, T t) {
        return getImageBitmap(contextObject, t, 0, 0);
    }

    /**
     * 获取图片原图 Bitmap 应运行于非主线程中
     */
    public <T> Bitmap getImageBitmap(Object contextObject, T t, int width, int height) {
        try {
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject).init();
            if (glideRequestM.isShouldReturn()) {
                return null;
            }
            GlideRequest<Bitmap> bitmapGlideRequest = glideRequestM.getRequestManager().asBitmap().load(t);
            Bitmap bitmap;
            if (width > 0 && height > 0) {
                bitmap = bitmapGlideRequest.submit(width, height).get();
            } else {
                bitmap = bitmapGlideRequest.submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> void setImage(Object contextObject, T t, final ImageView imageView,
                             int defaultId,
                             boolean isCenterCrop,
                             boolean isShowAnim,
                             boolean isCircle,
                             boolean isUseCache,
                             int conner,
                             int blurRadio, int blurSimpling,
                             int width, int height,
                             final OnLoadImageListener<Drawable> listener) {
        if (imageView == null || t == null) {
            return;
        }
        try {
            final GlideRequestM glideRequestM = new GlideRequestM(contextObject, imageView).init();
            if (glideRequestM.isShouldReturn()) {
                Log.e("Glide", "contextObject error");
                if (listener != null) listener.onFail();
                return;
            }
            GlideRequests glideRequest = glideRequestM.getRequestManager();

            GlideRequest<Drawable> load = glideRequest.asDrawable().load(t);
            if (!isUseCache) {
                load = load.diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true);//跳过内存缓存
            } else {
                load = load.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .skipMemoryCache(false);//不跳过内存缓存
            }
            List<BitmapTransformation> transList = new ArrayList<>();
            if (isCenterCrop) {
                transList.add(new CenterCrop());
            }
            if (isCircle) {
                transList.add(new GlideCircleTransform());
            } else if (conner > 0) {
                transList.add(new GlideConnerTransform(conner));
            }
            if (blurRadio < 25 || blurSimpling > 1) {
                transList.add(new GlideBlurTransform(glideRequestM.getContext(), blurRadio, blurSimpling));
            }
            if (transList.size() > 0) {
                BitmapTransformation[] transformations = new BitmapTransformation[transList.size()];
                load = load.transform(transList.toArray(transformations));
            }
            transList.clear();
            transList = null;
            if (defaultId == 0) {
                if (isCircle) {
                    defaultId = circleDefaultId;
                } else if (conner > 0) {
                    defaultId = connerDefaultId;
                } else {
                    defaultId = normalDefaultId;
                }
            }

            if (defaultId != 0) {
                load = load.placeholder(defaultId).error(defaultId).fallback(defaultId);
//                load = load.placeholder(defaultId);//默认图会在加载完图片后还存在...
//                load = load.fallback(defaultId);//这个不是加载前的占位图
//                imageView.setBackgroundResource(defaultId);
            }
            if (isShowAnim) {
//                load = load.transition(DrawableTransitionOptions.withCrossFade());
                load = load.transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)); //用这种方式能让不消失的占位图消失
            }

            if (listener != null) {
                load = load.addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("Glide", "Exception " + e);
//                        if (listener != null)
                        listener.onFail();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        //                        if (listener != null)
                        listener.onSuccess(resource);
//                        imageView.setBackground(null);
                        return false;
                    }
                });
            }
            if (width > 0 && height > 0) {
                load = load.override(width, height);
            }
            load.into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) listener.onFail();
        }
    }


    private DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(200).setCrossFadeEnabled(true).build();

    /**
     * @param context      context对象
     * @param imageUrl     图片网络地址
     * @param erroImageId  载入失败图片网络地址
     * @param placeImageId 占位图片网络地址
     * @param imageView    要载入图片的ImageView对象
     */
    public static void loadPicsFitWidth(Context context, final String imageUrl, int erroImageId, int placeImageId, final ImageView imageView) {
        Glide.with(context).load(imageUrl).skipMemoryCache(true).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                /*
                 * 在此处填写具体载入逻辑*/

                /*
                判断imageView对象是否为空
                 */
                if (imageView == null) {
                    return false;
                }
                /*
                判断imageView的填充方式,如果不是fitxy的填充方式 设置其填充方式
                 */
                if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                /*
                进行宽度为matchparent时的适应imageView的高度计算
                 */
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                float scale = (float) vw / (float) resource.getIntrinsicWidth();
                int vh = Math.round(resource.getIntrinsicHeight() * scale);
                params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                imageView.setLayoutParams(params);
                return false;
            }
        }).placeholder(placeImageId).error(erroImageId).into(imageView);
    }
}
