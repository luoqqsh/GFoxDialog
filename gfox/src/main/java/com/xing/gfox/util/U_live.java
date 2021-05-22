package com.xing.gfox.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class U_live {
    /**
     * 判断我们的应用是否在白名单中
     *
     * @param context context
     * @return 结果
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 申请加入白名单
     *
     * @param context context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestIgnoreBatteryOptimizations(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gotoLiveSetting(Context context) {
        if (U_device.INSTANCE.isHuaWeiPhone()) {
            goHuaweiSetting(context);
        } else if (U_device.INSTANCE.isXiaoMiPhone()) {
            goXiaomiSetting(context);
        } else if (U_device.INSTANCE.isMeiZuPhone()) {
            goMeizuSetting(context);
        } else if (U_device.INSTANCE.isOppoPhone()) {
            goOPPOSetting(context);
        } else if (U_device.INSTANCE.isSamSungPhone()) {
            goSamsungSetting(context);
        } else if (U_device.INSTANCE.isVivoPhone()) {
            goVIVOSetting(context);
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    private static void showActivity(Context context, @NonNull String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private static void showActivity(Context context, @NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //操作步骤：授权管理 -> 自启动管理 -> 允许应用自启动
    private static void goXiaomiSetting(Context context) {
        showActivity(context, "com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    //操作步骤：应用启动管理 -> 关闭应用开关 -> 打开允许自启动
    private static void goHuaweiSetting(Context context) {
        try {
            showActivity(context, "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity(context, "com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }

    //操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
    private static void goOPPOSetting(Context context) {
        try {
            showActivity(context, "com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity(context, "com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity(context, "com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity(context, "com.coloros.safecenter");
                }
            }
        }
    }

    //操作步骤：权限管理 -> 自启动 -> 允许应用自启动
    private static void goVIVOSetting(Context context) {
        showActivity(context, "com.iqoo.secure");
    }

    //操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
    private static void goMeizuSetting(Context context) {
        showActivity(context, "com.meizu.safe");
    }

    //操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
    private static void goSamsungSetting(Context context) {
        try {
            showActivity(context, "com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity(context, "com.samsung.android.sm");
        }
    }

    //操作步骤：自启动管理 -> 允许应用自启动
    private void goLetvSetting(Context context) {
        showActivity(context, "com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity");
    }

    //操作步骤：权限管理 -> 自启动权限管理 -> 点击应用 -> 允许被系统启动
    private void goSmartisanSetting(Context context) {
        showActivity(context, "com.smartisanos.security");
    }
}
