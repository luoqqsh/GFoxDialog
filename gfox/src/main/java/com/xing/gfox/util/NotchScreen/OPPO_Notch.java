package com.xing.gfox.util.NotchScreen;

import android.content.Context;

public class OPPO_Notch {
    /**
     * 判断是否是刘海屏
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean hasNotch(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }


    /**
     * 获取刘海屏高度
     *
     * @param context 上下文
     * @return 结果
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

}
