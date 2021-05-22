package com.xing.gfox.util.NotchScreen;

import android.content.Context;

public class Mi_Notch {
    /**
     * 判断是否是刘海屏
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean hasNotch(Context context) {
        return true;
    }

    public static int getNotchWidth(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_width", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getNotchHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
