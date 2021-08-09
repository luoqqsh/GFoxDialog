package com.xing.gfox.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.Locale;

import com.xing.gfox.R;

public class U_language {
    public static String appLanguage = "";

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getSystemCountry() {
        return Locale.getDefault().getCountry();
    }

    public static String getSystemLC() {
        return getSystemLanguage() + "-" + getSystemCountry();
    }

    public static String getAppLanguage() {
        if (TextUtils.isEmpty(appLanguage)) {
            appLanguage = getSystemLC();
        }
        return appLanguage;
    }

    public static void changeLanguage(Context context, Locale locale) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //获取应用程序的所有资源对象
            Resources resources = context.getResources();
            //获取设置对象
            Configuration configuration = resources.getConfiguration();
            //如果API < 17
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.locale = locale;
            } else //如果 17 < = API < 25 Android 7.7.1
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                    configuration.setLocale(locale);
                } else {//API 25  Android 7.7.1
                    configuration.setLocale(locale);
                    configuration.setLocales(new LocaleList(locale));
                }
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
        appLanguage = locale.toLanguageTag();
    }

    /**
     * 切换语言的activity页面要重写并调用该方法
     *
     * @param context
     * @return
     */
    public static ContextThemeWrapper attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources resources = context.getResources();
            Locale locale = new Locale(getAppLanguage());
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            context = context.createConfigurationContext(configuration);
        }
        final Configuration configuration = context.getResources().getConfiguration();
        // 此处的ContextThemeWrapper是androidx.appcompat.view包下的
        // 你也可以使用android.view.ContextThemeWrapper，但是使用该对象最低只兼容到API 17
        // 所以使用 androidx.appcompat.view.ContextThemeWrapper省心
        return new ContextThemeWrapper(context, R.style.Theme_AppCompat_Empty) {
            @Override
            public void applyOverrideConfiguration(Configuration overrideConfiguration) {
                if (overrideConfiguration != null) {
                    overrideConfiguration.setTo(configuration);
                }
                super.applyOverrideConfiguration(overrideConfiguration);
            }
        };
    }

    private static Resources getApplicationResource(Context context, Locale l) {
        Resources resourceForApplication = null;
        try {
            Context applicationContext = context.getApplicationContext();
            resourceForApplication = applicationContext.getPackageManager().getResourcesForApplication(applicationContext.getPackageName());
            Configuration config = resourceForApplication.getConfiguration();
            config.locale = l;
            resourceForApplication.updateConfiguration(config, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resourceForApplication;
    }

    /**
     * 获取指定语言的字符
     *
     * @param context  上下文
     * @param stringId id
     *                 locale，不传为当前语言
     * @return
     */
    public static String getStringByApplication(Context context, int stringId) {
        return getStringByApplication(context, stringId, Locale.getDefault());
    }

    public static String getStringByApplication(Context context, int stringId, Locale locale) {
        Resources resources = getApplicationResource(context, locale);
        if (resources != null) return resources.getString(stringId);
        return "";
    }
}
