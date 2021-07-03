package com.xing.gfox.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

import com.xing.gfox.log.ViseLog;

public class U_appstore {

    private static final String GOOGLE_PLAY_APP_STORE_PACKAGE_NAME = "com.android.vending";

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @return 跳转到应用商店的 Intent
     */
    public static Intent getAppStoreIntent(Context context) {
        return getAppStoreIntent(context, context.getPackageName(), false);
    }

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @param isIncludeGooglePlayStore 是否包括 Google Play 商店
     * @return 跳转到应用商店的 Intent
     */
    public static Intent getAppStoreIntent(Context context, boolean isIncludeGooglePlayStore) {
        return getAppStoreIntent(context, context.getPackageName(), isIncludeGooglePlayStore);
    }

    /**
     * 获取跳转到应用商店的 Intent
     *
     * @param packageName 包名
     * @return 跳转到应用商店的 Intent
     */
    public static Intent getAppStoreIntent(Context context, String packageName) {
        return getAppStoreIntent(context, packageName, false);
    }

    /**
     * 获取跳转到应用商店的 Intent
     * <p>优先跳转到手机自带的应用市场</p>
     *
     * @param packageName              包名
     * @param isIncludeGooglePlayStore 是否包括 Google Play 商店
     * @return 跳转到应用商店的 Intent
     */
    public static Intent getAppStoreIntent(Context context, String packageName, boolean isIncludeGooglePlayStore) {
        if (U_device.INSTANCE.isSamSungPhone()) {// 三星单独处理跳转三星市场
            Intent samsungAppStoreIntent = getSamsungAppStoreIntent(context, packageName);
            if (samsungAppStoreIntent != null) return samsungAppStoreIntent;
        }
        if (U_device.INSTANCE.isLePhone()) {// 乐视单独处理跳转乐视市场
            Intent leecoAppStoreIntent = getLeecoAppStoreIntent(context, packageName);
            if (leecoAppStoreIntent != null) return leecoAppStoreIntent;
        }

        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> resolveInfos = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfos.size() == 0) {
            ViseLog.e("No app store!");
            return null;
        }
        Intent googleIntent = null;
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.packageName;
            if (!GOOGLE_PLAY_APP_STORE_PACKAGE_NAME.equals(pkgName)) {
                if (isAppSystem(context, pkgName)) {
                    intent.setPackage(pkgName);
                    return intent;
                }
            } else {
                intent.setPackage(GOOGLE_PLAY_APP_STORE_PACKAGE_NAME);
                googleIntent = intent;
            }
        }
        if (isIncludeGooglePlayStore && googleIntent != null) {
            return googleIntent;
        }

        intent.setPackage(resolveInfos.get(0).activityInfo.packageName);
        return intent;
    }

    private static boolean go2NormalAppStore(Context context, String packageName) {
        Intent intent = getNormalAppStoreIntent(context);
        if (intent == null) return false;
        intent.setData(Uri.parse("market://details?id=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    private static Intent getNormalAppStoreIntent(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        intent.setData(uri);
        if (getAvailableIntentSize(context, intent) > 0) {
            return intent;
        }
        return null;
    }

    private static Intent getSamsungAppStoreIntent(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        intent.setData(Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getAvailableIntentSize(context, intent) > 0) {
            return intent;
        }
        return null;
    }

    private static Intent getLeecoAppStoreIntent(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setClassName("com.letv.app.appstore", "com.letv.app.appstore.appmodule.details.DetailsActivity");
        intent.setAction("com.letv.app.appstore.appdetailactivity");
        intent.putExtra("packageName", packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getAvailableIntentSize(context, intent) > 0) {
            return intent;
        }
        return null;
    }

    private static int getAvailableIntentSize(Context context, Intent intent) {
        return context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size();
    }

    private static boolean isAppSystem(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
