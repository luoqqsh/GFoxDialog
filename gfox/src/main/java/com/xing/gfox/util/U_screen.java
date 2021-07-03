package com.xing.gfox.util;

import static com.xing.gfox.util.U_context.getAppCompActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.xing.gfox.base.app.AppInit;
import com.xing.gfox.R;
import com.xing.gfox.log.ViseLog;


public class U_screen {

    public static int dp2px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static int sp2px(Context context, int spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度px
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度px
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }

    /**
     * 获取设备宽度
     */
    public static int getDeviceWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(sizePoint);
            } else {
                display.getSize(sizePoint);
            }
            return sizePoint.x;
        }
        return 0;
    }

    /**
     * 获取设备高度
     */
    public static int getDeviceHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            Point sizePoint = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(sizePoint);
            } else {
                display.getSize(sizePoint);
            }
            return sizePoint.y;
        }
        return 0;
    }

    /**
     * 获取导航栏高度(下面虚拟按键)
     *
     * @param context 上下文
     * @return 导航栏高度px
     */
    public static int getDaoHangHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    // 获得状态栏高度
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getStatusBarHeightByReadR(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

    //获取屏幕密度dpi
    public static int getDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ViseLog.d("TAG", "Density is " + displayMetrics.density + " densityDpi is " + displayMetrics.densityDpi + " height: " + displayMetrics.heightPixels +
                " width: " + displayMetrics.widthPixels);
        return displayMetrics.densityDpi;
    }

    @SuppressLint("RestrictedApi")
    public static void hideSupportActionBar(Context context) {
        if (getAppCompActivity(context) != null) {
            ActionBar ab = getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.hide();
            }
        }
        U_context.getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void showSupportActionBar(Context context) {
        if (getAppCompActivity(context) != null) {
            ActionBar ab = getAppCompActivity(context).getSupportActionBar();
            if (ab != null) {
                ab.setShowHideAnimationEnabled(false);
                ab.show();
            }
        }
        U_context.getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    //屏幕旋转，保持比例不变
    public static int getWidthFromScreenW(Context context) {
        return getScreenWidth(context);
    }

    public static int getHeightFromWidthAtPort(Context context) {
        return (int) (getScreenWidth(context) * getScreenWHScale(context));
    }

    public static int getHeightFromWidthAtLand(Context context) {
        return (int) (getScreenHeight(context) * getScreenHWScale(context));
    }

    public static float getScreenWHScale(Context context) {
        return getScreenWidth(context) * 1.0f / getScreenHeight(context);
    }

    public static float getScreenHWScale(Context context) {
        return getScreenHeight(context) * 1.0f / getScreenWidth(context);
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar2(Context context) {
        return getDeviceHeight(context) - getScreenHeight(context) > 0;
    }

    /**
     * 判断普通机型是否显示导航栏(不适合刘海屏手机)
     *
     * @param context 上下文
     * @return 是否显示导航栏
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 判断全面屏是否启用虚拟键盘
     * 需要在onCreate方法中加入如下代码方能生效
     * //设置底部导航栏颜色
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     * getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
     * }
     *
     * @param activity 上下文
     * @return 结果
     */
    public static boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        for (int i = 0; i < vp.getChildCount(); i++) {
            vp.getChildAt(i).getContext().getPackageName();
            if (vp.getChildAt(i).getId() != -1 && "navigationBarBackground".equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取屏幕相关数据
     *
     * @param context 上下文
     * @return 屏幕参数
     */
    public static String getScreenInfo(Context context) {
        return "屏幕(不含导航栏):" + getScreenWidth(context) + "," + getScreenHeight(context) + "\n" +
                "设备(物理分辨率):" + getDeviceWidth(context) + "," + getDeviceHeight(context) + "\n" +
                "导航栏(下方):" + getDaoHangHeight(context) + "," + (checkDeviceHasNavigationBar(context) ? "显示导航栏" : "不存在(隐藏)导航栏") + "\n" +
                "状态栏(上方):" + getStatusBarHeight(context) + "\n" +
                "dpi:" + context.getResources().getConfiguration().densityDpi + "\n" +
                "res分辨率:" + context.getResources().getString(R.string.fenbianlv) + "\n";
    }

    /**
     * 设置容器大小
     *
     * @param view   容器
     * @param width  宽
     * @param height 高
     */
    public static void setViewSize(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            lp.height = height;
            lp.width = width;
            view.setLayoutParams(lp);
        }
    }

    public static int getWidthByScaleWH(float scale, int height) {
        return (int) (height * scale);
    }

    public static int getHeightByScaleWH(float scale, int width) {
        return (int) (width / scale);
    }

    public static boolean isFullScreen(@NonNull FragmentActivity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
    }

    /**
     * 设置横屏
     *
     * @param activity 上下文
     */
    public static void setLandscape(@NonNull FragmentActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置竖屏
     *
     * @param activity 上下文
     */
    public static void setPortrait(@NonNull FragmentActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 是否横屏
     *
     * @return 结果
     */
    public static boolean isLandscape() {
        return AppInit.getAppContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 是否竖屏
     *
     * @return 结果
     */
    public static boolean isPortrait() {
        return AppInit.getAppContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity 上下文
     * @return 角度
     */
    public static int getScreenRotation(@NonNull FragmentActivity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static double getScreenInch(Activity context) {
        double inch = 0;

        try {
            int realWidth = 0, realHeight = 0;
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (android.os.Build.VERSION.SDK_INT < 17
                    && android.os.Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            inch = U_math.formatDouble(Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi)), 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return inch;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 禁止截屏
     *
     * @param mActivity
     */
    public static void canNotCutScreen(FragmentActivity mActivity) {
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
    }
}

