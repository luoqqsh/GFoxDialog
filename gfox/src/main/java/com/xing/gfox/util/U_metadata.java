package com.xing.gfox.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.xing.gfox.base.app.AppInit;

public class U_metadata {
    /**
     * 获取 application 的 meta-data 值
     *
     * @param key The key of meta-data.
     * @return the value of meta-data in application
     */
    public static String getMetaDataInApp(@NonNull final String key) {
        String value = "";
        PackageManager pm = AppInit.getAppContext().getPackageManager();
        String packageName = AppInit.getAppContext().getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取 activity 的 meta-data 值
     *
     * @param activity The activity.
     * @param key      The key of meta-data.
     * @return the value of meta-data in activity
     */
    public static String getMetaDataInActivity(@NonNull final Activity activity,
                                               @NonNull final String key) {
        return getMetaDataInActivity(activity.getClass(), key);
    }

    /**
     * Return the value of meta-data in activity.
     *
     * @param clz The activity class.
     * @param key The key of meta-data.
     * @return the value of meta-data in activity
     */
    public static String getMetaDataInActivity(@NonNull final Class<? extends Activity> clz,
                                               @NonNull final String key) {
        String value = "";
        PackageManager pm = AppInit.getAppContext().getPackageManager();
        ComponentName componentName = new ComponentName(AppInit.getAppContext(), clz);
        try {
            ActivityInfo ai = pm.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            value = String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取 service 的 meta-data 值
     *
     * @param service The service.
     * @param key     The key of meta-data.
     * @return the value of meta-data in service
     */
    public static String getMetaDataInService(@NonNull final Service service,
                                              @NonNull final String key) {
        return getMetaDataInService(service.getClass(), key);
    }

    /**
     * Return the value of meta-data in service.
     *
     * @param clz The service class.
     * @param key The key of meta-data.
     * @return the value of meta-data in service
     */
    public static String getMetaDataInService(@NonNull final Class<? extends Service> clz,
                                              @NonNull final String key) {
        String value = "";
        PackageManager pm = AppInit.getAppContext().getPackageManager();
        ComponentName componentName = new ComponentName(AppInit.getAppContext(), clz);
        try {
            ServiceInfo info = pm.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            value = String.valueOf(info.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取 receiver 的 meta-data 值
     *
     * @param receiver The receiver.
     * @param key      The key of meta-data.
     * @return the value of meta-data in receiver
     */
    public static String getMetaDataInReceiver(@NonNull final BroadcastReceiver receiver,
                                               @NonNull final String key) {
        return getMetaDataInReceiver(receiver, key);
    }

    /**
     * Return the value of meta-data in receiver.
     *
     * @param clz The receiver class.
     * @param key The key of meta-data.
     * @return the value of meta-data in receiver
     */
    public static String getMetaDataInReceiver(@NonNull final Class<? extends BroadcastReceiver> clz,
                                               @NonNull final String key) {
        String value = "";
        PackageManager pm = AppInit.getAppContext().getPackageManager();
        ComponentName componentName = new ComponentName(AppInit.getAppContext(), clz);
        try {
            ActivityInfo info = pm.getReceiverInfo(componentName, PackageManager.GET_META_DATA);
            value = String.valueOf(info.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static Object getMetaData(Context context, String metaName, Object defaultName) {
        try {
            Bundle applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            if (defaultName instanceof String) {
                return applicationInfo.getString(metaName, (String) defaultName);
            } else if (defaultName instanceof Integer) {
                return applicationInfo.getInt(metaName, (int) defaultName);
            } else if (defaultName instanceof Boolean) {
                return applicationInfo.getBoolean(metaName, (boolean) defaultName);
            } else if (defaultName instanceof Float) {
                return applicationInfo.getFloat(metaName, (float) defaultName);
            } else if (defaultName instanceof Long) {
                return applicationInfo.getLong(metaName, (long) defaultName);
            } else {
                return defaultName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return defaultName;
        }
    }
}
