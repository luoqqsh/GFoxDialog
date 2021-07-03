package com.xing.gfox.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.permissions.HuaweiUtils;
import com.xing.gfox.util.permissions.MeizuUtils;
import com.xing.gfox.util.permissions.MiuiUtils;
import com.xing.gfox.util.permissions.OppoUtils;
import com.xing.gfox.util.permissions.QikuUtils;
import com.xing.gfox.util.permissions.RomUtils;

public class U_floatWindow {

    /**
     * 检查悬浮窗权限
     *
     * @param context 上下文
     * @return 是否有权限
     */
    public static boolean checkFloatWindow(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, 24, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                ViseLog.e(Log.getStackTraceString(e));
                return false;
            }
        } else {
            ViseLog.e("Below API 19 cannot invoke!");
            return false;
        }
    }

    /**
     * 去设置悬浮窗，需要先实例化后调用
     * hl_permissionsOther p = hl_permissionsOther.getInstance(listener);
     *
     * @param context 上下文
     */
    public static void gotoSetFloatWindow(Context context) {
        if (U_device.INSTANCE.isVivoPhone()) {
            //部分VIVO机型，悬浮窗设置无效，还是手动开吧----如VIVO X9--6.0--25--
            U_permissionsOther.gotoAppSetPage(context);
            return;
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (U_device.INSTANCE.is360Phone()) {
                QikuUtils.applyPermission(context);
            } else if (U_device.INSTANCE.isHuaWeiPhone()) {
                HuaweiUtils.applyPermission(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                MeizuUtils.applyPermission(context);
            } else if (RomUtils.checkIsMiuiRom()) {
                MiuiUtils.applyMiuiPermission(context);
            } else if (U_device.INSTANCE.isOppoPhone()) {
                OppoUtils.applyOppoPermission(context);
            } else {
                commonROMPermissionApplyInternal(context);
            }
        } else {
            commonROMPermissionApplyInternal(context);
        }
    }

    public static void commonROMPermissionApplyInternal(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WindowManager windowManager;

    public static void showFloatWindow(Context context, View windowView, boolean isHaveActionInView) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        windowManager.addView(windowView, getLayoutParams(context, isHaveActionInView));
    }

    public static void hideFloatWindow(Context context, View windowView) {
        if (windowView == null) return;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        windowManager.removeView(windowView);
    }

    private static WindowManager.LayoutParams getLayoutParams(Context context, boolean isHaveActionInView) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 设置悬浮窗为全屏
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.alpha = 1.0f;
        if (isHaveActionInView) {
            wmParams.flags =
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        } else {
            wmParams.flags =
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |//锁屏显示悬浮窗
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        }

        wmParams.width = U_screen.getScreenWidth(context);
        wmParams.height = U_screen.getScreenHeight(context) + U_screen.getDaoHangHeight(context) * 2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return wmParams;
    }
}
