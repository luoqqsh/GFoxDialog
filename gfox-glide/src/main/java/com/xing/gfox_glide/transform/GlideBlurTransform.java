package com.xing.gfox_glide.transform;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.xing.gfox_glide.blur.FastBlur;
import com.xing.gfox_glide.blur.RSBlur;

import java.security.MessageDigest;


/**
 * author:ChenJiaLiang
 * Date:2019/5/23
 * Description:
 */
public class GlideBlurTransform extends BitmapTransformation {

    private static final int VERSION = 1;
    private final String ID = getClass().getName() + VERSION;
    private Context mContext;
    private int mRadius;
    private int mSampling;

    public GlideBlurTransform(Context context, int radius, int sampling) {
        mContext = context.getApplicationContext();
        mRadius = radius;
        mSampling = sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int scaledWidth = width / mSampling;
        int scaledHeight = height / mSampling;

        Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) mSampling, 1 / (float) mSampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(source, 0, 0, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = RSBlur.blur(mContext, bitmap, mRadius);
            } catch (Exception e) {
                bitmap = FastBlur.blur(bitmap, mRadius, true);
            }
        } else {
            bitmap = FastBlur.blur(bitmap, mRadius, true);
        }

        return bitmap;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof GlideBlurTransform
                && ((GlideBlurTransform) o).mRadius == this.mRadius
                && ((GlideBlurTransform) o).mSampling == this.mSampling;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + mRadius * 1000 + mSampling * 100;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + mRadius + mSampling).getBytes(CHARSET));
    }
}