package com.xing.gfox.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

public class U_wallpaper {
    /**
     * 设置系统图片壁纸
     *
     * @param context 上下文
     * @param bitmap  图片
     * @return 结果
     */
    public static boolean setSystemPicWallpaper(Context context, Bitmap bitmap) {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            wallpaperManager.setBitmap(bitmap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
