package com.xing.gfox.util;

import android.content.Context;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class U_notchScreen {
    //刘海屏、水滴屏等异型屏支持的Android系统版本：8.0-》全面屏  8.0以上-》刘海屏、水滴屏等异型屏
    public static boolean isNotchSupportVersion() {
        int curApiVersion = Build.VERSION.SDK_INT;
        return curApiVersion > 26;
    }

    //检查流行机型是否存在刘海屏
    public static boolean isNotch(Context context) {
        return isNotch_VIVO(context) || isNotch_OPPO(context) || isNotch_HUAWEI(context) || isNotch_XIAOMI(context);
    }

    //检查vivo是否存在刘海屏、水滴屏等异型屏
    public static boolean isNotch_VIVO(Context context) {
        boolean isNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("android.util.FtFeature");
            Method method = cls.getMethod("isFeatureSupport", int.class);
            isNotch = (boolean) method.invoke(cls, 0x00000020);//0x00000020：是否有刘海  0x00000008：是否有圆角
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return isNotch;
        }
    }

    //检查oppo是否存在刘海屏、水滴屏等异型屏
    public static boolean isNotch_OPPO(Context context) {
        boolean isNotch = false;
        try {
            isNotch = context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return isNotch;
        }
    }

    //检查huawei是否存在刘海屏、水滴屏等异型屏
    public static boolean isNotch_HUAWEI(Context context) {
        boolean isNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method method = cls.getMethod("hasNotchInScreen");
            isNotch = (boolean) method.invoke(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return isNotch;
        }
    }

    //检查xiaomi是否存在刘海屏、水滴屏等异型屏
    public static boolean isNotch_XIAOMI(Context context) {
        boolean isNotch = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("android.os.SystemProperties");
            Method method = cls.getMethod("getInt", String.class, int.class);
            isNotch = ((int) method.invoke(null, "ro.miui.notch", 0) == 1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return isNotch;
        }
    }

    //获取huawei刘海屏、水滴屏的宽度和高度：int[0]值为刘海宽度 int[1]值为刘海高度
    public static int[] getNotchSize_HUAWEI(Context context) {
        int[] notchSize = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class cls = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method method = cls.getMethod("getNotchSize");
            notchSize = (int[]) method.invoke(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return notchSize;
        }
    }

    //获取xiaomi刘海屏、水滴屏的宽度和高度：int[0]值为刘海宽度 int[1]值为刘海高度
    public static int[] getNotchSize_XIAOMI(Context context) {
        int[] notchSize = new int[]{0, 0};
        if (isNotch_XIAOMI(context)) {
            int resourceWidthId = context.getResources().getIdentifier("notch_width", "dimen", "android");
            if (resourceWidthId > 0) {
                notchSize[0] = context.getResources().getDimensionPixelSize(resourceWidthId);
            }
            int resourceHeightId = context.getResources().getIdentifier("notch_height", "dimen", "android");
            if (resourceHeightId > 0) {
                notchSize[1] = context.getResources().getDimensionPixelSize(resourceHeightId);
            }
        }
        return notchSize;
    }

    //获取vivo、oppo刘海屏、水滴屏的高度：官方没有给出标准的获取刘海高度的API，由于大多情况是：状态栏≥刘海，因此此处获取刘海高度采用状态栏高度
    public static int getNotchHeight(Context context) {
        int notchHeight = 0;
        if (isNotch_VIVO(context) || isNotch_OPPO(context)) {
            //若不想采用状态栏高度作为刘海高度或者可以采用官方给出的刘海固定高度：vivo刘海固定高度：27dp（need dp2px）  oppo刘海固定高度：80px
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                notchHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return notchHeight;
    }


}
