package com.xing.gfox.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xing.gfox.media.U_media;

public class U_bitmap {
    public static GradientDrawable getRoundRectDrawable(int radius, int color, boolean isFill, int strokeWidth) {
        //左上、右上、右下、左下的圆角半径
        float[] radiu = {radius, radius, radius, radius, 20, radius, radius, radius};
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(radiu);
        drawable.setColor(isFill ? color : Color.TRANSPARENT);
        drawable.setStroke(isFill ? 0 : strokeWidth, color);
        return drawable;
    }

    /**
     * 把bitmap转换成流（String）
     *
     * @param bitmap bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @param format   The format of bitmap.
     * @return bytes
     */
    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
        return drawable == null ? null : U_convert.bitmap2Bytes(drawable2Bitmap(drawable), format, 100);
    }

    /**
     * Bytes to drawable.
     *
     * @param bytes The bytes.
     * @return drawable
     */
    public static Drawable bytes2Drawable(Context context, byte[] bytes) {
        return bytes == null ? null : bitmap2Drawable(context, U_convert.bytes2Bitmap(bytes));
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        canvas.drawColor(Color.TRANSPARENT);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * 通过文件路径来获取Bitmap
     *
     * @param pathName
     * @return
     */
    public static Bitmap getBitmapFromFile(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 通过输入流InputStream来获取Bitmap
     *
     * @param inputStream
     * @return
     */
    public static Bitmap getBitmapFromStream(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap getBitmapFromDrawableId(Context context, int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    /**
     * 主动释放ImageView的图片资源
     *
     * @param imageView
     */
    public static void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
            recycleBitmapDrawable(bd);
        }
    }

    private static void recycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            recycleBitmap(bitmap);
        }
        bitmapDrawable = null;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static void recycleBackgroundBitmap(ImageView view) {
        if (view != null) {
            BitmapDrawable bd = (BitmapDrawable) view.getBackground();
            recycleBitmapDrawable(bd);
        }
    }

    /**
     * 保存图片到相册目录下
     *
     * @param bitmap  图片bitmap
     * @param context 上下文
     * @return 图片路径
     */
    public static String bitmapToDCIM(Bitmap bitmap, Context context) {
        return bitmapToDCIM(bitmap, context, true);
    }

    public static String bitmapToDCIM(Bitmap bitmap, Context context, boolean isUpdateGalley) {
        //更改的名字
        String imageName = "dd" + U_time.getNowTimeStr(U_time.HH_mm_ss) + ".png";
        bitmapToFile(bitmap, U_file.CAMERA + File.separator + imageName);
        if (isUpdateGalley) {
            U_media.updateMedia(context, U_file.CAMERA + File.separator + imageName);
        }
        return U_file.CAMERA + File.separator + imageName;
    }

    /**
     * 保存图片到指定文件
     *
     * @param bitmap   图片bitmap
     * @param filePath 保存文件路径
     * @return 图片路径
     */
    public static void bitmapToFile(Bitmap bitmap, String filePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把assets里的图片转成bitmap
     *
     * @param context 上下文
     * @param assets  图片名称，根目录下
     * @return bitmap
     */
    public static Bitmap getBitmapFromAssets(Context context, String assets) {
        try {
            InputStream is = context.getAssets().open(assets);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从文件获取bitmap
     *
     * @return 图片路径
     */
    public static Bitmap getBitmapFromFile(File file) {
        return getBitmapFromPath(file.getAbsolutePath());
    }

    public static Bitmap getBitmapFromPath(String path) {
        if (U_file.isFileExist(path)) {
            return BitmapFactory.decodeFile(path);
        }
        return null;
    }

    public static Bitmap getBitmapFromResource(Context context, int resource) {
        return BitmapFactory.decodeResource(context.getResources(), resource);
    }

    //把imageview的图片保存到文件，相册目录
    public static void imgViewToCamera(ImageView imageView, Context context) throws IOException {
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageView中的图像
        bitmapToDCIM(bitmap, context);
        imageView.setDrawingCacheEnabled(false);//关闭catch
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 截屏
     *
     * @param activity          The activity.
     * @param isDeleteStatusBar True to delete status bar, false otherwise.
     * @return the bitmap of screen
     */
    public static Bitmap screenShot(@NonNull final FragmentActivity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        boolean drawingCacheEnabled = decorView.isDrawingCacheEnabled();
        boolean willNotCacheDrawing = decorView.willNotCacheDrawing();
        decorView.setDrawingCacheEnabled(true);
        decorView.setWillNotCacheDrawing(false);
        Bitmap bmp = decorView.getDrawingCache();
        if (bmp == null || bmp.isRecycled()) {
            decorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            decorView.layout(0, 0, decorView.getMeasuredWidth(), decorView.getMeasuredHeight());
            decorView.buildDrawingCache();
            bmp = Bitmap.createBitmap(decorView.getDrawingCache());
        }
        if (bmp == null || bmp.isRecycled()) return null;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            Resources resources = activity.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = resources.getDimensionPixelSize(resourceId);
            ret = Bitmap.createBitmap(
                    bmp,
                    0,
                    statusBarHeight,
                    dm.widthPixels,
                    dm.heightPixels - statusBarHeight
            );
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        decorView.setWillNotCacheDrawing(willNotCacheDrawing);
        decorView.setDrawingCacheEnabled(drawingCacheEnabled);
        return ret;
    }

    /**
     * 将图片保存到相册
     * 适配安卓Q文件存储
     *
     * @param bitmap   bitmap
     * @param fileName 文件名，包含后缀
     * @param context  上下文
     * @return
     */
    public static boolean bitmapToDCIM(Context context, Bitmap bitmap, String fileName) {
        Uri uri;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, fileName);
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //RELATIVE_PATH是相对路径不是绝对路径
            //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Img");
        } else {
            contentValues.put(MediaStore.Images.Media.DATA, U_file.DCIM + File.separator + fileName);
        }
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return true;
    }

    /**
     * bitmap保存到文件（保存到应用内部存储）
     *
     * @param bitmap   bitmap
     * @param filename 文件名，包含后缀
     * @param context  上下文
     * @return 保存的文件
     */
    public static File bitmapToFile(Context context, Bitmap bitmap, String filename) {
        String extStorageDirectory = context.getExternalFilesDir("").getAbsolutePath();
        File file = new File(extStorageDirectory, filename);//创建文件，第一个参数为路径，第二个参数为文件名
        Uri uri;
        try (OutputStream outStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();
            // 实现相册更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, "com.lx0.recognize.fileprovider", file);
            } else {
                //适配Android N版本之前
                uri = Uri.fromFile(file);
            }
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            return null;
        }
        return file;
    }

    //通过url获取bitmap
    public static Bitmap downloadImg(String downloadUrl) {
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
