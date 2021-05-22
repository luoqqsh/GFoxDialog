package com.xing.gfox.util.NotchScreen;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.xing.gfox.util.U_device;

public class U_Notch {
    /**
     * 判断手机是否有刘海屏
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean hasNotch(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (U_device.INSTANCE.isVivoPhone()) {
                return VIVO_Notch.hasNotchInScreen(context);
            }
            if (U_device.INSTANCE.isOppoPhone()) {
                return OPPO_Notch.hasNotch(context);
            }
            if (U_device.INSTANCE.isXiaoMiPhone()) {
                return Mi_Notch.hasNotch(context);
            }
            if (U_device.INSTANCE.isHuaWeiPhone()) {
                return HW_Notch.hasNotch(context);
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 是否隐藏屏幕刘海，部分小米机型有这个设置选项
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean isShowNotch(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(context.getContentResolver(), "force_black", 0) == 1;
        } else return false;
    }
}
