package com.xing.gfox.util;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import java.util.List;
import com.xing.gfox.log.ViseLog;

/**
 * 特殊权限申请
 */
public class U_permissionsOther {

    /**
     * 检查是否有安装未知应用权限
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        } else {
            return false;
        }
    }

    /**
     * 去设置安装未知应用
     *
     * @param mActivity 上下文
     */
    public static void gotoSetUnknownApp(FragmentActivity mActivity) {
        if (mActivity == null) return;
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        mActivity.startActivityForResult(intent, 1000);
    }

    /**
     * 是否启用辅助功能服务
     *
     * @param mContext             context
     * @param serviceCanonicalName 服务类名
     * @return 结果
     */
    public static boolean checkAccessibilitySettingsOn(Context mContext, String serviceCanonicalName) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = mContext.getPackageName() + "/" + serviceCanonicalName;
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            ViseLog.e("Error finding setting, default accessibility to not found: " + e.getMessage());
            return false;
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
            ViseLog.e("***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    /**
     * 跳转到辅助功能设置页面
     *
     * @param context context
     */
    public static void gotoSetAccessibility(Context context) {
        context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    /**
     * 去应用设置详情页，主要是手动设置锁屏权限和部分机型的电话权限
     *
     * @param context 上下文
     */
    public static void gotoAppSetPage(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    /**
     * 检查是否有修改系统设置权限
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean checkSetSystem(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        } else {
            return true;
        }
    }

    /**
     * 去设置允许修改系统设置权限
     *
     * @param context 上下文
     */
    public static void gotoSetSystem(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }

    /**
     * 有权查看使用情况的应用
     *
     * @param context 上下文
     * @return 结果
     */
    private static boolean isNoOption(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } else {
            return false;
        }
    }

    /**
     * 是否启用权查看使用情况的应用
     *
     * @param context 上下文
     * @return 结果
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean checkUsage(Context context) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        return queryUsageStats != null && !queryUsageStats.isEmpty();
    }

    /**
     * 跳转权查看使用情况的应用页面
     *
     * @param context 上下文
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void gotoSetUsage(Context context) {
        if (isNoOption(context)) {
            context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
    }

    /**
     * 打开开发者模式界面 https://blog.csdn.net/ouzhuangzhuang/article/details/84029295
     */
    public static void gotoDevelopmentMode(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.View");
                context.startActivity(intent);
            } catch (Exception e1) {
                try {
                    //部分小米手机采用这种方式跳转
                    Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 打开系统语言设置页面
     *
     * @param context
     */
    public static void gotoSetLanguage(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开正在运行的服务界面 https://www.jianshu.com/p/dd491235d113
     */
    public static void gotoServiceRunning(Context context) {
        try {
            ComponentName componentName;
            if (U_device.INSTANCE.isVivoPhone()) {
                componentName = new ComponentName("com.android.settings", "com.vivo.settings.VivoSubSettingsForImmersiveBar");
            } else {
                componentName = new ComponentName("com.android.settings", "com.android.settings.CleanSubSettings");
            }
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(componentName);
            intent.setAction("android.intent.action.View");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否支持计步器传感器
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean isSupportStepCountSensor(Context context) {
        // 获取传感器管理器的实例
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            return countSensor != null || detectorSensor != null;
        }
        return false;
    }

    public static void checkAutoRevokePermission(Context context) {
        // 判断是否开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                !context.getPackageManager().isAutoRevokeWhitelisted()) {
            gotoSetAutoRevokePermission(context);
        }
    }

    public static void gotoSetAutoRevokePermission(Context context) {
        // 跳转设置页
        Intent intent = new Intent(Intent.ACTION_AUTO_REVOKE_PERMISSIONS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}