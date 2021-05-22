package com.xing.gfox.util.permissions;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_floatWindow;


public class MeizuUtils {
    /**
     * 去魅族权限申请页面
     */
    public static void applyPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");//remove this line code for fix flyme6.3
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ViseLog.e("获取悬浮窗权限, 打开AppSecActivity失败, " + Log.getStackTraceString(e));
                // 最新的魅族flyme 6.2.5 用上述方法获取权限失败, 不过又可以用下述方法获取权限了
                U_floatWindow.commonROMPermissionApplyInternal(context);
            } catch (Exception eFinal) {
                ViseLog.e("获取悬浮窗权限失败, 通用获取方法失败, " + Log.getStackTraceString(eFinal));
            }
        }
    }
}
