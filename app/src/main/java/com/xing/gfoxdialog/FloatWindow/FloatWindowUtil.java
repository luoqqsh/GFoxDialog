package com.xing.gfoxdialog.FloatWindow;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.xing.gfox.util.U_screen;


/**
 * 悬浮窗工具类
 */
public class FloatWindowUtil {

    private static WindowManager windowManager;

    public static void show(Context context, View windowView, boolean isHaveActionInView, String TAG) {
        try {
            if (windowManager == null) {
                windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            }

            if (windowManager != null) {
                windowManager.addView(windowView, getLayoutParams(context, isHaveActionInView));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hide(Context context, View windowView, String TAG) {
        try {
            if (windowView == null) return;
            if (windowManager == null) {
                windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            }
            if (windowManager != null) {
                windowManager.removeView(windowView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WindowManager.LayoutParams getLayoutParams(Context context, boolean isHaveActionInView) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 设置悬浮窗为全屏
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.alpha = 1.0f;
        if (isHaveActionInView) {
            wmParams.flags =
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        } else {
            wmParams.flags =
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        }

        wmParams.width = U_screen.getScreenWidth(context);
        wmParams.height = U_screen.getScreenHeight(context) + U_screen.getDaoHangHeight(context) * 2;
//        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return wmParams;
    }
}
